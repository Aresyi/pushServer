/** **/
package com.ydj.pushserver.core;

import java.util.LinkedHashMap;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.util.HashedWheelTimer;

import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.core.handler.DispatchUpStreamHandler;
import com.ydj.pushserver.core.handler.HeartbeatHandler;
import com.ydj.pushserver.startup.SysProperties;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-6 下午09:18:51
 * @version : 1.0
 * @description :
 *
 */
public class TcpServerPipelineFactory implements ChannelPipelineFactory {

	private HashedWheelTimer idleTimer = new HashedWheelTimer();
	
	//默认30秒发一次心跳;60秒视为超时----强制断开连接
	private int writeIdleTime = 30;
	private int readIdleTime = 60;
	
	/**存放ChannelHandler列表*/
	private LinkedHashMap<String, ChannelHandler> moreHandlers = new LinkedHashMap<String, ChannelHandler>();
	
	public TcpServerPipelineFactory() {
		 writeIdleTime = Integer.parseInt(SysProperties.getProperty("writer.idle.timeSeconds"));
		 readIdleTime  = Integer.parseInt(SysProperties.getProperty("reader.idle.timeSeconds"));
		 
		 MyLog.logInfo("IdleStateHandler:{readIdleTime:"+readIdleTime+",writeIdleTime:"+writeIdleTime+"}",true);
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.ChannelPipelineFactory#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		
		pipeline.addLast("encode",new StringDecoder(CharsetUtil.UTF_8));
		pipeline.addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
		
		
		pipeline.addLast("timeout", new IdleStateHandler(idleTimer, readIdleTime , writeIdleTime , 0));
		pipeline.addLast("heartBeat", new HeartbeatHandler());
		
		pipeline.addLast("mainHandler", new DispatchUpStreamHandler());
		
		//注册各种自定义Handler
		LinkedHashMap<String, ChannelHandler> handlers = getMoreHandlers();
		for (String key : handlers.keySet()) {
			pipeline.addLast(key, handlers.get(key));
		}
		
		return pipeline;
	}

	public LinkedHashMap<String, ChannelHandler> getMoreHandlers() {
		return moreHandlers;
	}

	public void setMoreHandlers(LinkedHashMap<String, ChannelHandler> moreHandlers) {
		this.moreHandlers = moreHandlers;
	}

	
	
}
