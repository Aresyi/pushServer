/** **/
package com.ydj.pushserver.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.ydj.pushserver.comm.util.MyLog;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-21 下午05:46:04
 * @version : 1.0
 * @description :
 *
 */
public class SysProperties {

	private static Properties properties;
	
	static void init() {

		InputStream is = null;
        Throwable error = null;

        if (is == null) {
            try {
                File home = new File(System.getProperty("server.home"));
                File conf = new File(home, "conf");
                File properties = new File(conf, "sys.properties");
                is = new FileInputStream(properties);
            } catch (Throwable t) {
            	t.printStackTrace();
                // Ignore
            }
        }

        if (is == null) {
            try {
                is = SysProperties.class.getResourceAsStream
                    ("/com/ydj/pushserver/startup/sys.properties");
            } catch (Throwable t) {
                // Ignore
            }
        }

        if (is != null) {
            try {
                properties = new Properties();
                properties.load(is);
                is.close();
            } catch (Throwable t) {
                error = t;
            }
        }

        if ((is == null) || (error != null)) {
            MyLog.logError("Failed to load sys.properties.",true);
            properties=new Properties();
        }

        Enumeration<?> enumeration = properties.propertyNames();
        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            String value = properties.getProperty(name);
            MyLog.logInfo(name+"="+value,	true);
            if (value != null) {
                System.setProperty(name, value);
            }
        }
		
	}
	
    public static String getProperty(String name) {
        return properties.getProperty(name);
    }

    public static String getProperty(String name, String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }
    
    
    public static void main(String[] args) {
		MyLog.logInfo(SysProperties.getProperty("listen.port"));
	}
}
