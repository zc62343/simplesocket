package com.zchen.tcp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zchen.tcp.server.ServerThread.TcpCallBack;

/**
 * Tcp服务器
 * @author zengchen
 * @mark 使用方式：
 * </br>TcpServer server = new TcpServer();
 * </br>server.setCallback(new TcpCallBack() {
 * </br>	public ResponseObj execute(RequestObj request) {
 * </br>		//业务代码
 * </br>		return null;
 * </br>	}
 * </br>});
 * </br>server.start();
 */
public class TcpServer {

	private TcpCallBack callback;
	
	/**
	 * 默认线程数
	 */
	private static final int defaultThreads = 2;
	/**
	 * 默认端口
	 */
	private static final int defaultPort = 15732;

	public void start() {
		start(defaultPort, defaultThreads);
	}
	
	public void start(int port) {
		start(port, defaultThreads);
	}
	
	public TcpCallBack getCallback() {
		return callback;
	}

	public void setCallback(TcpCallBack callback) {
		this.callback = callback;
	}

	public void start(int port,int threads) {
		
		if(callback == null){
			return;
		}
		
		ServerSocket server = null;
		Socket client = null;
		ExecutorService executorPool = Executors.newFixedThreadPool(threads);
		try {
			server = new ServerSocket(port);
			while((client = server.accept()) != null){//接收客户端连接
				executorPool.submit(new ServerThread(client,callback));
			}
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} finally {
			try {
				if(client != null) client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if(server != null) server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}


