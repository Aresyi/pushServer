package com.ydj.pushserver.comm.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
*
* @author : Ares.yi
* @createTime : 2013-08-10 上午11:13:42 
* @version : 1.0 
* @description : StackTrace辅助类
*
*/
public class StackTraceUtil {

	/**
	 * 取出exception中的信息
	 * 
	 * @param exception
	 * @return
	 */
	public static String getStackTrace(Throwable exception) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			return sw.toString();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	public static Throwable findException(Throwable throwable, Class<?>... exClassArray) {
		Throwable t = throwable;
		int i = 0;

		while (t != null && (++i) < 10) {
			for (Class<?> exClass : exClassArray) {
				if (exClass.isInstance(t)) {
					return t;
				}
				t = t.getCause();
			}
		}

		return null;
	}
}
