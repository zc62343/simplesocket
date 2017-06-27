package com.zchen.tcp.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class  RequestObj {

	public String key;

	public Map<String,Object> map = new HashMap<String,Object>();
	
	public List<Object> list = new ArrayList<Object>();

	public byte[] bytes;
	
	public RequestObj() {}
	
	public RequestObj(String key) {
		super();
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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
		return "RequestObj [key=" + key + ", map=" + map.toString() + ", list=" + list.toString() + "]";
	}
    
}
