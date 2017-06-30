package com.zchen.tcp;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;
import com.zchen.tcp.server.IMessageHandler;

/**
 * 处理客户端请求
 * @author zengchen
 */
public class ServerCallBack implements IMessageHandler{

	@Override
	public ResponseObj receiveMsg(RequestObj request){
		System.out.println(request);
		
		ResponseObj response = new ResponseObj();
		response.list = request.list;
		response.map = request.map;
		response.bytes = request.bytes;
		return response;
	}

}
