package com.zchen.tcp.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseObj {

	public static final int SUCCESS = 200;

	public static final int SERVER_ERROR = 500;

	public int code;

	public String msg;

	public Map<String,Object> map = new HashMap<String,Object>();
	
	public List<Object> list = new ArrayList<Object>();
	
	public byte[] bytes;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "ResponseObj [code=" + code + ", msg=" + msg + ", map=" + map.toString() + ", list=" + list.toString() + "]";
	}
	
}
