/** **/
package com.ydj.pushserver.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;

import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.core.bean.MyChannel;
import com.ydj.pushserver.core.bean.MyChannelGroup;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午02:57:51
 * @version : 1.0
 * @description :
 *
 */
public class ChannelCache {
	
	private static Log log = LogFactory.getLog(ChannelCache.class);

	/**存储所有Channel*/
	private static ChannelGroup group = new MyChannelGroup("push_server-group");
	
	/**存储用户id和Channel id对应关系*/
	private static ConcurrentHashMap<Integer,Integer> uIdChannelId = new ConcurrentHashMap<Integer,Integer>(80000);
	
	/**存储用户id和密钥*/
	private static ConcurrentHashMap<Integer,String> keys = new ConcurrentHashMap<Integer,String>(80000);
	
	/**特殊使用(发短信)*/
	public final static HashMap<Integer,String> SMS_UID = new LinkedHashMap<Integer,String>(10);
	
	private ChannelCache(){
	}
	
	private static void dumpInfo(){
		MyLog.logInfo("\nCurrent total {connection:" + group.size() + ",user:" + uIdChannelId.size() + "}. " + new Date()+"\n");
		log.info("Current total {connection:" + group.size() + ",user:" + uIdChannelId.size() + "}.");
		
		flushSMSUids();
	}
	
	private static Timer timer = new Timer();
	
	static{
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				dumpInfo();
			}
		}, 1000 * 1, 1000 * 15);
	}
	
	
	/**
	 * 获取ChannelGroup
	 * @return
	 */
	public static ChannelGroup getChannelGroup(){
		return group;
	}
	
	/**
	 * 获取Keys
	 * @return
	 */
	public static ConcurrentHashMap<Integer,String> getKeys(){
		return keys;
	}
	
	
	/**
	 * 获取MyChannel 
	 * @return
	 */
	public static MyChannel getMyChannel(int uid){
		Integer channelId = uIdChannelId.get(uid);
		if(channelId == null){
			return null;
		}
		Channel c = group.find(channelId);
		if(c != null){
			return ( (MyChannel) c );
		}else{
			uIdChannelId.remove(uid);
		}
		return null;
	}
	
	/**
	 * 添加Channel到ChannelGroup
	 * @param uid
	 * @param channel
	 */
	public static void addChannel(MyChannel channel){
		uIdChannelId.put(channel.getUid(), channel.getId());
		group.add(channel);
		return ;
	}
	
	/**
	 * 删除Channel
	 * @param channel
	 */
	public static void removeChannel(Channel channel){
		Channel c = group.find(channel.getId());
		if(c != null){
			int uid = ( (MyChannel) c ).getUid();
			uIdChannelId.remove(uid);
		}
	}
	
	/**
	 * 释放资源
	 */
	public static void clearRes(){
		group.clear();
		uIdChannelId.clear();
		keys.clear();
		timer.cancel();
	}
	
	
	
	/**
	 * SMS特殊使用
	 */
	private static void flushSMSUids(){
		try {
			String userDir = System.getProperty("server.home");//System.getProperty("user.dir");
			read(userDir+"/conf/sms_uids.txt","UTF-8");
			
			keys.putAll(SMS_UID);
		} catch (Exception e) {
		}
	}
	
	
    private static void read(String fileName, String encoding) {  
        try {  
            BufferedReader in = new BufferedReader(new InputStreamReader(  
                    new FileInputStream(fileName), encoding));  
  
            String str = "";  
            while ((str = in.readLine()) != null) {
            		String s[] = str.split(",");
            		//System.out.println(s[0]+"="+s[1]);
            		SMS_UID.put(Integer.valueOf(s[0]), s[1]);
            }  
            in.close();  
            return ;  
        } catch (Exception ex) {  
            ex.printStackTrace();  
        }  
    }
    
    /**
     * TEST
     * @param args
     */
    public static void main(String[] args) {
    	String userDir = System.getProperty("user.dir");
		String os = System.getProperty("os.name");
		
		System.out.println("{osName:"+os+",userDir:"+userDir+"}");
		flushSMSUids();
	}
}
