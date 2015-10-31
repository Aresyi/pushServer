/** **/
package com.ydj.pushserver.core.handler;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.UpstreamChannelStateEvent;

import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.core.ChannelCache;
import com.ydj.pushserver.core.bean.MyChannel;


/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-6 下午09:20:26
 * @version : 1.0
 * @description :
 *
 */
public class DispatchUpStreamHandler extends SimpleChannelUpstreamHandler {

	private static Log log = LogFactory.getLog(DispatchUpStreamHandler.class);
	
	/**事件分发*/
	private static final EventDispatcher eventDispatcher = new EventDispatcher() ;
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws Exception {
		log("Center received msg:{"+e.getMessage()+"} from {"+e.getChannel()+"}.");
		
		eventDispatcher.dispatchMessageEvent(ctx, e.getChannel(), e);
		super.messageReceived(ctx, e);
	}
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log("Channel {"+e.getChannel()+"} is channelOpen.");
		super.channelOpen(ctx, e);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		log("Channel {"+e.getChannel()+"} is connected.");
//		eventDispatcher.dispatchChannelEvent(ctx, e.getChannel(), e);
		super.channelConnected(ctx, e);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		log("Channel {"+e.getChannel()+"} is disconnected.");
		this.closeChannel(ctx, e);
		super.channelDisconnected(ctx, e);
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		log("Channel {"+e.getChannel()+"} is closed.");
		closeChannel(ctx, e);
		super.channelClosed(ctx, e);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		log("Channel {"+e.getChannel()+"} is exceptionCaught.");
		super.exceptionCaught(ctx, e);
		
		MyChannel myChannel = (MyChannel) ChannelCache.getChannelGroup().find(e.getChannel().getId());
		if (myChannel != null) {
			eventDispatcher.dispatchExceptionCaught(ctx, myChannel, e);
		}
		
		// 处理IOException，主动关闭channel
		if (e.getCause() != null && e.getCause() instanceof IOException) {
			closeChannel(ctx, new UpstreamChannelStateEvent(e.getChannel(), ChannelState.CONNECTED, null));
		}
		
		
	}
	
	private void closeChannel(ChannelHandlerContext ctx, ChannelStateEvent e) {
		e.getChannel().close().awaitUninterruptibly();
	}
	
	private void log(String s){
		MyLog.logInfo(s);
		log.info(s);
	}
}
