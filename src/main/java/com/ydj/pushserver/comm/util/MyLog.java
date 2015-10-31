package com.ydj.pushserver.comm.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.ydj.pushserver.startup.SysProperties;

/**
 * @author : Ares.yi
 * @createTime : 2012-10-17 下午02:05:08
 * @version : 1.0
 * @description : 简单log，开发测试使用
 * 
 * 开发测试时，设置isOpen = true；
 * 准备发布时，设置isOpen = false；
 */
public class MyLog  {
	
	private static int isOpen = Integer.valueOf(SysProperties.getProperty("mylog.isopen", "1"));
	
	private MyLog(){}

	/**
	 * 开关设置
	 * @return
	 */
	private static boolean isOpen(){
		return (1 == isOpen);
	}
	
	/**
	 * 打印信息
	 * @param <T>
	 * @param t
	 */
	public static <T> void logInfo(T t){
		logInfo(t,isOpen());
	}
	
	/**
	 * 打印信息
	 * @param <T>
	 * @param t
	 * @param isOpen
	 */
	public static <T> void logInfo(T t , boolean isOpen){
		if(isOpen){
			System.out.println(t);
		}
	}
	
	/**
	 * 打印错误信息
	 * @param <T>
	 * @param t
	 */
	public static <T> void logError(T t){
		logError(t,isOpen());
	}
	
	/**
	 * 打印错误信息
	 * @param <T>
	 * @param t
	 * @param isOpen
	 */
	public static <T> void logError(T t , boolean isOpen){
		if(isOpen){
			System.err.println(t);
		}
	}
	
	/**
	 * 输出信息到指定路径文件中
	 * @param t
	 * @param filePath
	 * @throws IOException
	 *
	 * @author : Ares.yi
	 * @createTime : 2015年10月30日 下午4:35:41
	 */
	public static <T> void log2File(T t,String filePath) throws IOException{
		File file = new File(filePath);
		if(!file.exists()){
			file.createNewFile();
		}
		System.setOut(new PrintStream(new FileOutputStream(file,true)));
		logInfo(t);
	}
}

