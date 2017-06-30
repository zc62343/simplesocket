package com.zchen.tcp.server;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;

/**
 * 回调接口
 * @author zengchen
 */
public interface IMessageHandler {
	public ResponseObj receiveMsg(RequestObj request);
}		