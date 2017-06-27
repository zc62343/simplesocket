package com.zchen.tcp.server;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;
import com.zchen.tcp.server.ServerThread.TcpCallBack;

public class Demo {

	public static void main(String[] args) {
		TcpServer server = new TcpServer();
		server.setCallback(new TcpCallBack() {
			@Override
			public ResponseObj execute(RequestObj request) {
				//业务代码
				return null;
			}
		});
		server.start();
	}
}
