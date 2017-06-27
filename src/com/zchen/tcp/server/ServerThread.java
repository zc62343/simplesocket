package com.zchen.tcp.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;

import net.sf.json.JSONObject;

/**
 * 处理客户端数据传输
 * @author zengchen
 */
public class ServerThread implements Runnable {

	private TcpCallBack callback = null;
	private Socket client = null;

	private static final int BUFFER_SIZE = 64 * 1024;//缓冲区大小
	
	public ServerThread(Socket client, TcpCallBack callback) {
		this.client = client;
		this.callback = callback;
	}

	@Override
	public void run() {
		DataInputStream dis = null;
		DataOutputStream dos = null;
		ResponseObj response = new ResponseObj();
		try {
			dis = new DataInputStream(new BufferedInputStream(client.getInputStream(),BUFFER_SIZE));
			dos = new DataOutputStream(new BufferedOutputStream(client.getOutputStream(),BUFFER_SIZE));
			
			int dataLen = dis.readInt();
			byte[] bytes = new byte[dataLen];
			int len = dis.read(bytes,0,dataLen);
			while(len < dataLen){
				int iret = dis.read(bytes,len,dataLen-len);
				if(iret == -1) break;
				len += iret;
			}
			String data = new String(bytes, 0, len, "UTF-8");
			RequestObj request = new RequestObj();
			JSONObject obj = JSONObject.fromObject(data);
			request.key = obj.getString("key");
			request.map = JsonUtil.parseMap(obj.getJSONObject("map"));
			request.list = JsonUtil.parseList(obj.getJSONArray("list"));
			
			byte[] streamByte = null;
			int ioSize = dis.readInt();//流数量
			if(ioSize > 0){
				int streamLen = dis.readInt();
				streamByte = new byte[streamLen];
				int read = dis.read(streamByte);
				while(read < streamLen){
					int result = dis.read(streamByte,read,streamLen - read);
					if(result == -1) break;
					read += result;
				}
			}
			
			request.bytes = streamByte;
			streamByte = null;
			System.gc();
			
			try {
				response = callback.execute(request);
				response.code = ResponseObj.SUCCESS;
			} catch (Exception e) {
				response.code = ResponseObj.SERVER_ERROR;
				response.msg = e.toString();
				System.out.println("server exception：" + ExceptionUtil.getStackTrace(e));
			} finally {
				
				byte[] respBytes = response.bytes;
				response.bytes = null;
				
				JSONObject resp = JSONObject.fromObject(response);
				byte[] send = resp.toString().getBytes("UTF-8");
				dos.writeInt(send.length);
				dos.write(send);
				
				if(respBytes != null){
					dos.writeInt(1);//标识有一个字节数组需要传递
					dos.writeInt(respBytes.length);//发送文件大小
					dos.write(respBytes);
				}else{
					dos.writeInt(0);
				}
				dos.flush();
				
				respBytes = null;
				System.gc();
			}
		} catch (IOException e) {
			response.code = ResponseObj.SERVER_ERROR;
			response.msg = e.getMessage();
			e.printStackTrace();
		} finally {
			try {
				if (dis != null) dis.close();
				if (dos != null) dos.close();
				if (client != null) client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 回调接口
	 * @author zengchen
	 */
	public interface TcpCallBack {
		public ResponseObj execute(RequestObj request);
	}
}
