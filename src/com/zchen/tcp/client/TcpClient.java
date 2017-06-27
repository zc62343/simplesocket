package com.zchen.tcp.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;

import net.sf.json.JSONObject;

/**
 * tcp客户端，用于请求tcp服务器
 * @author zengchen
 */
public class TcpClient {
	
	/**
	 * 发送Map<String,Object> 参数请求
	 * @param ip 服务端IP
	 * @param port 服务端 port
	 * @param params 参数
	 * @param is io流
	 * @return
	 */
	public static void send(String ip, int port,RequestObj req,InputStream is,ClientCallback callback) {
		BufferedInputStream bis = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			bis = new BufferedInputStream(is,BUFFER_SIZE);
			int len = 0;
			byte[] buf = new byte[10240];
			while((len = bis.read(buf)) != -1){
				baos.write(buf, 0, len);
				baos.flush();
			}
			req.bytes = baos.toByteArray();
			send(ip, port, req,callback);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bis != null) bis.close();
				if(baos != null) baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static final int BUFFER_SIZE = 64 * 1024;//缓冲区大小
	
	/**
	 * 发送Map<String,Object> 参数请求
	 * @param <V>
	 * @param <K>
	 * @param <E>
	 * @param ip 服务端IP
	 * @param port 服务端 port
	 * @param req 参数
	 * @param streamByte 字节数组
	 * @return
	 */
	public static void send(String ip, int port,RequestObj obj,ClientCallback callback) {
		ResponseObj response = new ResponseObj();
		if(obj == null){
			callback.failed(response);
			return;
		}
		
		Socket client = null;
		DataOutputStream dos = null;
		DataInputStream dis = null;
		try {
			SocketAddress serverAddr = new InetSocketAddress(ip, port);
			client = new Socket();
			client.connect(serverAddr, 500);
			if(!client.isConnected()) {
				callback.failed(response);
				return;//未连接上，直接返回
			}
			dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream(),BUFFER_SIZE));
			dis = new DataInputStream(new BufferedInputStream(client.getInputStream(),BUFFER_SIZE));
			
			byte[] streamByte = obj.bytes;
			obj.bytes = null;//置空
			
			JSONObject req = JSONObject.fromObject(obj);
			byte[] send = req.toString().getBytes("UTF-8");
			
			dos.writeInt(send.length);//发送长度
			dos.write(send);//发送参数
			
			if(streamByte != null){
				dos.writeInt(1);//标识有一个字节数组需要传递
				dos.writeInt(streamByte.length);//发送文件大小
				dos.write(streamByte);
			}else{
				dos.writeInt(0);
			}
			dos.flush();
			streamByte = null;
			System.gc();
			
			int respLen = dis.readInt();//接收参数长度
			byte[] bytes = new byte[respLen];
			int len = dis.read(bytes);
			while(len < respLen){
				int iret = dis.read(bytes,len,respLen-len);
				if(iret == -1) break;
				len += iret;
			}
			String resp = new String(bytes,0,len,"UTF-8");
			JSONObject retJson = JSONObject.fromObject(resp);
			response.code = retJson.getInt("code");
			response.msg = retJson.getString("msg");
			response.map = JsonUtil.parseMap(retJson.getJSONObject("map"));
			response.list = JsonUtil.parseList(retJson.getJSONArray("list"));
			
			byte[] respBytes = null;
			int ioSize = dis.readInt();//流数量
			if(ioSize > 0){
				int streamLen = dis.readInt();
				respBytes = new byte[streamLen];
				int read = dis.read(respBytes);
				while(read < streamLen){
					int result = dis.read(respBytes,read,streamLen - read);
					if(result == -1) break;
					read += result;
				}
			}
			response.bytes = respBytes;
			
			respBytes = null;
			System.gc();
			
			if(response.code == 200){
				callback.success(response);
			}else if(response.code == 500){
				callback.serverError(response);
			}else{
				callback.failed(response);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(dis != null) dis.close();
				if(dos != null) dos.close();
				if(client != null) client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public interface ClientCallback{
		
		void success(ResponseObj response);
		
		void serverError(ResponseObj response);
		
		void failed(ResponseObj response);
		
	}
	
	public static class ClientCallbackWrap implements ClientCallback{
		
		public void success(ResponseObj response){};
		
		public void serverError(ResponseObj response){};
		
		public void failed(ResponseObj response){};
		
	}
}