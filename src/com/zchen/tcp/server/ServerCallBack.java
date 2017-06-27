package com.zchen.tcp.server;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;
import com.zchen.tcp.server.ServerThread.TcpCallBack;

/**
 * 处理客户端请求
 * @author zengchen
 */
public class ServerCallBack implements TcpCallBack{

	@Override
	public ResponseObj execute(RequestObj request){
		System.out.println(request);
		
		ResponseObj response = new ResponseObj();
		response.list = request.list;
		response.map = request.map;
		response.bytes = request.bytes;
		return response;
	}

}
