package test.rmi;

import java.util.Timer;
import java.util.TimerTask;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ydj.pushserver.business.bo.PushMessageBean;
import com.ydj.pushserver.services.PushServices;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-16 上午11:52:47
 * @version : 1.0
 * @description :
 *
 */
public class RMISpringClient {
	static ApplicationContext cxt ;
	static PushServices push ;
	
	static{
		cxt = new ClassPathXmlApplicationContext("file:test/test/rmi/rmi-client.xml");
//		cxt = new ClassPathXmlApplicationContext("file:/9tong/push-server/bin/test/rmi/rmi-client.xml");
		push = cxt.getBean("pushServicesClient", PushServices.class);
	}
	
	
	private static void invokRMI(){
		new Thread(
				new Runnable() {
					
					@Override
					public void run() {
						System.out.println(push.pushMessage(new PushMessageBean(100002, "rmi spring test", "rmi spring test")));						
					}
				}
		).start();
		
	}
	
	
	/**
	 * TEST
	 * @param args
	 */
	public static void main(String[] args) {
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				invokRMI();
			}
		}, 1000 * 1, 300);
	}
}
