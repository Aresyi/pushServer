/** **/
package com.ydj.pushserver.startup;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-16 下午12:24:57
 * @version : 1.0
 * @description :
 *
 */
public class SystemContext {
	public static ApplicationContext context ;
	
	private final static String  HOME_DIR = System.getProperty("server.home");
	
    static void init(){
    	try {
			PropertyConfigurator.configure(new URL("file:"+HOME_DIR+"/conf/log4j.properties"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
    	
		String[] config = new String[]{
        		"file:"+HOME_DIR+"/conf/business-datasource.xml",
        		"file:"+HOME_DIR+"/conf/business-dao-context.xml",
        		"file:"+HOME_DIR+"/conf/business-job.xml",
        		"file:"+HOME_DIR+"/conf/application-mq.xml",
        		"file:"+HOME_DIR+"/conf/rmi-server.xml"
        };
		context = new ClassPathXmlApplicationContext(config);
	}
}
