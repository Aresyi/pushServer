/** **/
package com.ydj.pushserver.startup;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import com.ydj.pushserver.core.TcpServer;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 上午11:32:30
 * @version : 1.0
 * @description :
 *
 */
public class Bootstrap {
	
	private static Bootstrap daemon = null;
	
	private TcpServer server = null;
	private int port = 88;
	
	private void initSys(){//注意顺序
		setServerHome();
		SysProperties.init();
		SystemContext.init();
		server = new TcpServer();
	}

	private void setServerHome(){
		String userDir = System.getProperty("user.dir");
		String os = System.getProperty("os.name");
		
		System.out.println("{osName:"+os+",userDir:"+userDir+"}");
		
		String value = "";
		try {
			value = new File(userDir, "..").getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(os.toLowerCase().contains("win")){// TEST NEED
			value = userDir;
		}
		
		System.setProperty("server.home", value);
	}
	
	private void start(){
		outputMessage("[INFO] Push Server Start");
		port = Integer.valueOf(SysProperties.getProperty("listen.port"));
		try {
			server.start(port);
		} catch (Exception e) {
			e.printStackTrace();
			outputMessage("[ERROR] Push Server Shutdown");
			System.exit(-1);
		}
		
	}
	
	private void stop() {
		outputMessage("[INFO] Push Server Shutdown");
		try {
			server.stop();
		} catch (Exception e) {
			e.printStackTrace();
			outputMessage("[ERROR] Push Server Shutdown");
			System.exit(-1);
		}
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		if (daemon == null) {
            daemon = new Bootstrap();
            try {
                daemon.initSys();
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(1);
                return;
            }
        }

        try {
            String command = "start";
            if (args.length > 0) {
                command = args[args.length - 1];
            }

            if (command.equals("start")) {
                daemon.start();
            } else if (command.equals("stop")) {
                daemon.stop();
            } else {
                System.out.println("Bootstrap: command \"" + command + "\" does not exist.");
                System.exit(1);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
	}
	
	
	

	public static void outputMessage(String message) {

		String separated = "************************************************************************************************************************";
		String whitespace = " ";
		message = new StringBuilder(message).append(" at ").append(new Date())
				.append(".").toString();

		System.out.println(separated);
		System.out.print("*");

		int i = 1;
		int flag = separated.length() - message.length();
		for (; i < flag / 2; i++)
			System.out.print(whitespace);

		System.out.print(message);

		for (int j = 1; j < flag - i; j++)
			System.out.print(whitespace);

		System.out.println("*");
		System.out.println(separated);

	}

}
