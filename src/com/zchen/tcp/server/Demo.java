package com.zchen.tcp.server;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;

public class Demo {

	public static void main(String[] args) {
		TcpServer server = new TcpServer();
		server.setHandler(new IMessageHandler() {
			
			@Override
			public ResponseObj receiveMsg(RequestObj request) {
				ResponseObj response = new ResponseObj();
				response.list = request.list;
				response.map = request.map;
//				response.bytes = request.bytes;
				return response;
			}
		});
		server.start();
	}
}
