package com.zchen.tcp.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常工具类
 * @author zengchen
 */
public class ExceptionUtil {

	public static String getStackTrace(Exception e) {  
        StringWriter sw = null;  
        PrintWriter pw = null;  
        try {  
            sw = new StringWriter();  
            pw = new PrintWriter(sw);  
            // 将出错的栈信息输出到printWriter中  
            e.printStackTrace(pw); 
            pw.flush();  
            sw.flush();  
        } finally {  
            try {  
            	if (sw != null) sw.close();  
            } catch (IOException e1) {  
                e1.printStackTrace();  
            }  
            if (pw != null) {  
                pw.close();  
            }  
        }  
        return sw.toString();  
    }  
}
