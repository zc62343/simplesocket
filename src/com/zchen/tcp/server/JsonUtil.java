package com.zchen.tcp.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonUtil {

	public static Map parseMap(JSONObject obj){
		Map<Object,Object> map = new HashMap<Object,Object>();
		Set<String> keys = obj.keySet();
		for(String key : keys){
			Object object = obj.get(key);
			if(object instanceof Integer){
				map.put(key, obj.getInt(key));
			}else if(object instanceof String){
				map.put(key, obj.getString(key));
			}else if(object instanceof Long){
				map.put(key, obj.getLong(key));
			}else if(object instanceof Boolean){
				map.put(key, obj.getBoolean(key));
			}else if(object instanceof JSONObject){
				map.put(key,parseMap(obj.getJSONObject(key)));
			}else if(object instanceof JSONArray){
				map.put(key,parseList(obj.getJSONArray(key)));
			}
		}
		return map;
	}
	
	public static List parseList(JSONArray arr){
		List<Object> lists = new ArrayList<Object>();
		for(int i=0; i<arr.size(); i++){
			Object object = arr.get(i);
			if(object instanceof Integer){
				lists.add(arr.getInt(i));
			}else if(object instanceof String){
				lists.add(arr.getString(i));
			}else if(object instanceof Long){
				lists.add(arr.getLong(i));
			}else if(object instanceof Boolean){
				lists.add(arr.getBoolean(i));
			}else if(object instanceof JSONArray){
				lists.addAll(parseList(arr.getJSONArray(i)));
			}else if(object instanceof JSONObject){
				lists.add(parseMap(arr.getJSONObject(i)));
			}
		}
		return lists;
	}
	
}
