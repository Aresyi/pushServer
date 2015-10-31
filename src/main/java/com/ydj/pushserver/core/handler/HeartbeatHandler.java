package com.ydj.pushserver.core.handler;


//import java.net.SocketTimeoutException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
//import org.jboss.netty.channel.DefaultExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;

import com.ydj.pushserver.business.bo.Business;
import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.core.ChannelCache;
import com.ydj.pushserver.core.bean.MyChannel;


/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-12 下午04:38:51
 * @version : 1.0
 * @description :
 *
 */
public class HeartbeatHandler extends IdleStateAwareChannelHandler {

	private static Log log = LogFactory.getLog(HeartbeatHandler.class);
	
	@Override
	public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
			throws Exception {
		try {
			Channel channel = ChannelCache.getChannelGroup().find(e.getChannel().getId());
			if (e.getState() == IdleState.WRITER_IDLE) {
				if (channel != null) {
					MyChannel myChannel = (MyChannel)channel ;
					String msg = "C:"+Business.getSysCastCount(myChannel.getUid()).toString();
					ResponseProcess.response(channel, msg);
					
					log("Writer heartbeat to uid{"+myChannel.getUid()+"}, channel{"+e.getChannel()+"},msg is :"+msg+". Heartbeat~~~~."+new Date());
				} else {
					log("Writer heartbeat to channel{"+e.getChannel()+"},but channel is not managed.Force to close!");
					e.getChannel().close().awaitUninterruptibly();
				}
			} else if (e.getState() == IdleState.READER_IDLE) {
				//String msg = "Force to close channel(" + ctx.getChannel().getRemoteAddress() + "), reason: time out.";
				
				//handleUpstream(ctx, new DefaultExceptionEvent(e.getChannel(), new SocketTimeoutException(msg)));
				
				//e.getChannel().close().awaitUninterruptibly();
				
				if (channel != null) {
					MyChannel myChannel = (MyChannel)channel ;
					String msg = "C:[]";//+Business.getSysCastCount(myChannel.getUid()).toString();
					ResponseProcess.response(channel, msg);
					
					log("Writer[R] heartbeat to uid{"+myChannel.getUid()+"}, channel{"+e.getChannel()+"},msg is :"+msg+". Heartbeat~~~~."+new Date());
				} else {
					log("Writer[R] heartbeat to channel{"+e.getChannel()+"},but channel is not managed.Force to close!");
					e.getChannel().close().awaitUninterruptibly();
				}

			}
			super.channelIdle(ctx, e);
		} catch (Exception e2) {
			log.error(e2);
		}
		
	}
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		log("HeartB received msg:{"+e.getMessage()+"} from {"+e.getChannel()+"}.");
		
		String s = e.getMessage().toString();
		if ("1".equals(s) || "2".equals(s)) {// 1(IOS)或者2(Android)表示客户端心跳信息，收到即可，无后续操作
			return;
		}
		super.messageReceived(ctx, e);
	}
	
	private void log(String s){
		MyLog.logInfo(s);
		log.info(s);
	}
}