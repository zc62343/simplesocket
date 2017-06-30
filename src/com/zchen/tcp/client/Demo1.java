package com.zchen.tcp.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zchen.tcp.bean.RequestObj;
import com.zchen.tcp.bean.ResponseObj;

public class Demo1 {
	
	public static void main(String[] args) {
//		final RequestObj req = new RequestObj();
////		req.key = "serviceStatus";
//		req.key = "managerCenter";
//		req.map.put("type", 1);
////		req.map.put("processname", "nginx");
////		req.map.put("processstatus", 1);
//		for(int i=0; i<3; i++){
//			new Thread(){
//				@Override
//				public void run() {
//					TcpClient.send("192.168.247.30", 10000, req,new TcpClient.ClientCallbackWrap() {
//						@Override
//						public void success(ResponseObj response) {
//							System.out.println(response.map.get("result"));
//							System.out.println(response.msg);
//							List<Object> list = response.list;
//							System.out.println(list);
//							Map<String,Object> maps = response.map;
//							System.out.println(maps);
//						}
//			
//						@Override
//						public void serverError(ResponseObj response) {
//							System.out.println(response.msg);
//						}
//						
//					});
//				}
//				
//			}.start();
//		}
		
		
		final RequestObj req = new RequestObj();
		req.key = "test";
		req.map.put("type", 1);
		req.map.put("name", "sfsdf");
		req.map.put("aaa", new ArrayList<String>(){{
			add("111111");
			add("222222");
		}});
		req.map.put("bbb", new HashMap<String,Object>(){{
			put("111111",1);
			put("222222","bbbb");
		}});
		
		List<Map<String,Object>> lists = new ArrayList<Map<String,Object>>();
		lists.add(new HashMap<String,Object>(){{
			put("kkkk", "vvvvv");
			put("k2k2", 22222);
		}});
		
		req.list.addAll(lists);
		
//		try {
//			req.bytes = FileUtils.readFileToByteArray(new File("d:/jdk.exe"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		for(int i=0; i<500; i++){
			final int index = i;
			new Thread(){
				@Override
				public void run() {
					TcpClient.send("192.168.2.196", 15732, req,new TcpClient.ClientCallbackWrap() {
						@Override
						public void success(ResponseObj response) {
							System.out.println("index:"+index);
							System.out.println(response.code);
							System.out.println(response.msg);
							System.out.println(response.map);
							System.out.println(response.list);
//							try {
//								System.out.println(response.bytes.length);
//								FileUtils.writeByteArrayToFile(new File("d:/tcpfile/"+UUID.randomUUID().toString()+".exe"), response.bytes);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
						}
			
						@Override
						public void serverError(ResponseObj response) {
							
						}
					});
				}
				
			}.start();
		}
	}
}
