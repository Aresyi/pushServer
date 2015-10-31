package com.ydj.pushserver.business.bo;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javapns.Push;
import javapns.notification.PushNotificationPayload;
import net.sf.json.JSONObject;

import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.comm.util.NamedThreadFactory;
import com.ydj.pushserver.db.springjdbc.JdbcDaoFactory;

public class IOSMessagePushHandler {

	private static Executor executor = Executors.newFixedThreadPool(500,new NamedThreadFactory("IOS", true));
	
	public static void push(PushMessageBean bean) {

		int uid = bean.getUid();
		Object content  =  bean.getIosMessage();		
		int sourceType =  bean.getIosPushSourceType();
		
		Object messageType_obj = bean.getTcpMessage();
		String  messageType = "0";
		if(messageType_obj!=null){
			String str =  messageType_obj.toString();
			
			if(str.contains("B:")){
				messageType = str.replace("B:", "");
			}else if(str.startsWith("E:")){
				str = str.replace("E:", "");
				JSONObject json = JSONObject.fromObject(str);
				messageType = json.getString("type");
			}
		}
		
		long start = System.currentTimeMillis();
		JSONObject deviceInfo = JdbcDaoFactory.getUserDao().getDeviceInfo(uid);
		
		MyLog.logInfo("---------::::::::" + uid + "///"+content);

		
		if (deviceInfo != null
				&& !CommonUtils.isEmptyString(deviceInfo
						.getString("deviceStoken"))) {
			MyLog.logInfo("---------::::::::"+ deviceInfo.getString("deviceStoken") + "///"+content);
			int curPackage = deviceInfo.getInt("curPackage");
			int privateSettingValue = deviceInfo.getInt("pushSetting");

			//处理 集中推送设置 (设置了集中推送 ，但仅仅只对名片交换请求有效)
			if(PrivateSettingUtils.isPushByTime(privateSettingValue)
					&& sourceType == IOSPushSourceType.cardExchange.ordinal())
				return ;
			
			//处理 提示音设置
			boolean isAlertSound = PrivateSettingUtils.isAlertSound(privateSettingValue);
			if(sourceType == IOSPushSourceType.message.ordinal())
				isAlertSound = true;//如若是私信，始终有提示音，提示音设置无效
			
			if(PrivateSettingUtils.isNotFaze(privateSettingValue)){
				isAlertSound = false;
			}
			
			//消息总条数
			int pushCount = 1 ;
			pushCount = JdbcDaoFactory.getUserDao().getNotifyCenterCount(uid);
//							+ JdbcDaoFactory.getUserDao().getContactsApplyCount( uid)
//							+ JdbcDaoFactory.getUserDao().getMessageCount(uid);
			long end = System.currentTimeMillis();
			MyLog.logInfo("use Time,JDBC_Count:"+(end-start));
			
			if(pushCount<1)pushCount=1;
			
			
			String p12File = "dis_output_"+curPackage+".p12";
			
			long start2 = System.currentTimeMillis();
			push(deviceInfo.getString("deviceStoken"), content,p12File,isAlertSound,pushCount,messageType);
			long end2 = System.currentTimeMillis();
			MyLog.logInfo("use Time,IOS_PUSH:"+(end2-start2));
		}

	}

	/**
	 * push
	 * 
	 * @param deviceToken
	 * @param content
	 * @param certificateFileName
	 */
	static void push(final String deviceToken, final Object content,
			final String certificateFileName,final boolean isAlertSound,final int pushCount,final String messageType) {
		
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				try {

					String certificatePath =  System.getProperty("server.home") + "/conf/p12file/" + certificateFileName;
					
					String certificatePassword = "abc123";// 此处注意导出的证书密码不能为空因为空密码会报错


//					Push.combined(content, pushCount, isAlertSound ? "default" : null, certificatePath,
//							certificatePassword, false, deviceToken); 
					
					String alertContent = "";
					JSONObject extinfo = new JSONObject();
					try{
						extinfo =  JSONObject.fromObject(content);
						alertContent = extinfo.getString("alert");
					}catch(Exception e){
						alertContent = content.toString();
					}
					
					
					PushNotificationPayload payload = new PushNotificationPayload(); 
					payload.addAlert(alertContent);
					payload.addBadge(pushCount);
					payload.addSound(isAlertSound ? "default" : null);
					payload.addCustomDictionary("stype", messageType);
					payload.addCustomDictionary("extinfo", extinfo.toString());

					
					//正式环境production参数设为true;测试环境设为false
					Push.payload(payload, certificatePath, certificatePassword, true, deviceToken);

				} catch (Exception e) {
					 e.printStackTrace();
				}
			}
		});

	}
	
	
	 
	
	public static void main(String args[]) {

//		String userDir = System.getProperty("user.dir");
//		String os = System.getProperty("os.name");
//		
//		MyLog.logInfo("{osName:"+os+",userDir:"+userDir+"}");
//		
//		String value = "";
//		try {
//			value = new File(userDir, "..").getCanonicalPath();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		if(os.toLowerCase().contains("win")){// TEST NEED
//			value = userDir;
//		}
//		
//		System.setProperty("server.home", value);
		JSONObject content = new JSONObject();
		content.put("nId", 73509);
		content.put("wId", 221);
		content.put("iId", 3);
		content.put("alert", "02传京东联姻特斯拉 签全年采购协议");

		System.out
				.println("d09635900b7d56ee46a5c9876040614e310507e09505365435d620e0251de3be");
		push("fff7c5f3de40a7863df508a97ebb4f2276776e846df70d8b7ca7dd73ef98aa6b",
				content,
				"dis_output_xixi917.p12",true,100,"6");

	}
}