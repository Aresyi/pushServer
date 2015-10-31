/** **/
package com.ydj.pushserver.services.rmi.impl;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ydj.common.mq.activemq.MessageTipProducer;
import com.ydj.pushserver.business.bo.IOSMessagePushHandler;
import com.ydj.pushserver.business.bo.PushMessageBean;
import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.core.ChannelCache;
import com.ydj.pushserver.core.handler.ResponseProcess;
import com.ydj.pushserver.services.PushServices;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午10:10:13
 * @version : 1.0
 * @description :
 *
 */
public class PushServicesImpl implements PushServices {
	
	private static Log log = LogFactory.getLog(PushServicesImpl.class);
	
	private MessageTipProducer msgTipProducer;
	
	/* (non-Javadoc)
	 * @see com.tranb.pushserver.services.rmi.PushServices#pushMessage(int, java.lang.String)
	 */
	@Override
	public boolean pushMessage(final PushMessageBean pushMessageBean){
		push(pushMessageBean);
		return true;
	}
	
	
	private void push(PushMessageBean pushMessageBean){
	    int uid = pushMessageBean.getUid();
		String s = "PushMsg->Uid:"+uid+",TcpMsg:"+pushMessageBean.getTcpMessage().toString()+",Msg:"+pushMessageBean.getIosMessage().toString();
		MyLog.logInfo(s);
		log.info(s);
		try {
			ResponseProcess.pushMessage2Client(uid, pushMessageBean.getTcpMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if(pushMessageBean.getTcpMessage() != null && pushMessageBean.getTcpMessage().toString().contains("D:")){//D: 代表积分，不需要走Apple官方PUSH
			}else{
				IOSMessagePushHandler.push(pushMessageBean);
			}
		} catch (Exception e) {
		}
		
		try {
		    if(pushMessageBean.getTcpMessage() != null && pushMessageBean.getTcpMessage().toString().indexOf("D:") == -1){//D: 代表积分，不推送
		        msgTipProducer.sendMessage(uid);//web消息推送
		    }
        } catch (Exception e) {
        }
	}
	
	@Override
	public boolean keys(int uid, String key) {
		MyLog.logInfo("Login->Uid:"+uid+",Key:"+key);
		log.info("Login->Uid:"+uid+",Key:"+key);
		try {
			ChannelCache.getKeys().put(uid, key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public int getConnectCout() {
		MyLog.logInfo("GetConnectCout->");
		log.info("GetConnectCout->");
		
		return ChannelCache.getChannelGroup().size();
	}


    public MessageTipProducer getMsgTipProducer() {
        return msgTipProducer;
    }

    public void setMsgTipProducer(MessageTipProducer msgTipProducer) {
        this.msgTipProducer = msgTipProducer;
    }

}
