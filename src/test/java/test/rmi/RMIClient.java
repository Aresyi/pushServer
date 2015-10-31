package test.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.ydj.pushserver.business.bo.PushMessageBean;
import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.services.PushServices;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-16 上午11:52:47
 * @version : 1.0
 * @description :
 *
 */
public class RMIClient {

	/**
	 * TEST
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			PushServices push = (PushServices)Naming.lookup("rmi://127.0.0.1:9898/pushServices");
			MyLog.logInfo(push.pushMessage(new PushMessageBean(22,"rmi test" ,"rmi test")));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
}
