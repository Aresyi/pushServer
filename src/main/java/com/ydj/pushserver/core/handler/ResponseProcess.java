/** **/
package com.ydj.pushserver.core.handler;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.core.ChannelCache;
import com.ydj.pushserver.core.bean.MyChannel;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-16 下午02:57:19
 * @version : 1.0
 * @description :
 *
 */
public class ResponseProcess {
	
	private static Log log = LogFactory.getLog(ResponseProcess.class);
	
	/**
	 * 推送信息
	 * @param uid
	 * @param message
	 */
	public static void pushMessage2Client(int uid,Object message){
		MyLog.logInfo("Tcp pushMsg to uid:"+uid+",msg:"+ message.toString());
		log.info("Tcp pushMsg to uid:"+uid+",msg:"+ message.toString());
		
		MyChannel myChannel = ChannelCache.getMyChannel(uid);
		if(myChannel == null){
			MyLog.logInfo("This user{"+uid+"} channel does't exist.");
			log.info("This user{"+uid+"} channel does't exist.");
			
			return ;
		}
		response(myChannel, message);
	}
	
	/**
	 * 应答
	 * @param channel
	 * @param message
	 */
	public static void response(Channel channel,Object message){
		String msg = message.toString()+"\r\n";
		ChannelBuffer buffer = ChannelBuffers.buffer(msg.getBytes().length * 3);  
		buffer.writeBytes(msg.getBytes());  
		channel.write(buffer);
	}
	
	
}
