package com.ydj.pushserver.core.handler;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelState;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.DefaultExceptionEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;

import com.ydj.pushserver.business.bo.Business;
import com.ydj.pushserver.business.bo.StringUtils;
import com.ydj.pushserver.comm.util.AbortPolicyWithReport;
import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.comm.util.NamedThreadFactory;
import com.ydj.pushserver.comm.util.StackTraceUtil;
import com.ydj.pushserver.core.ChannelCache;
import com.ydj.pushserver.core.bean.MyChannel;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午04:51:37
 * @version : 1.0
 * @description :
 *
 */
public class EventDispatcher {
	
	private Executor executor;
	private Executor channelExecutor;
	private Executor exceptionExecutor;
	
	private int maximumPoolSize = 1000;
	private int queueCapacity = 10 * 10000;

	public EventDispatcher() {
		
		this.executor = getExecutor();
		this.exceptionExecutor = Executors.newFixedThreadPool(500,new NamedThreadFactory("ExceptionEventProcessor", true));
		this.channelExecutor = Executors.newCachedThreadPool(new NamedThreadFactory("ChannelEventProcessor", true));
	}

	private ThreadPoolExecutor getExecutor() {
		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(maximumPoolSize, maximumPoolSize, 60L,
				TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(queueCapacity), new NamedThreadFactory(
						"EventDispatcher", true), new AbortPolicyWithReport("EventDispatcher-A"));
		return threadPoolExecutor;
	}

	/**
	 * @Title: dispatchMessageEvent
	 * @Description: 分发消息事件
	 * @param ctx
	 * @param channel
	 * @param e
	 * @return void 返回类型
	 */
	public void dispatchMessageEvent(final ChannelHandlerContext ctx, final Channel channel, final MessageEvent e) {
		
		if(!(e.getMessage() instanceof String)){
			return ;
		}
		Executor threadPool = executor;
		String msg = e.getMessage().toString().trim().replace("\n", "").replace("\r", "").replace("\t", "");
		if (msg.contains("_")) {//第一次握手消息
			String array[] = msg.split("_");
			
			int uid = -1;
			String key = null;
			
			try {
				uid = Integer.valueOf(array[0]);
				key = array[1];
			} catch (Exception e2) {
				channel.close().awaitUninterruptibly();
				return ;
			}
			
			if(!ChannelCache.getKeys().containsKey(uid) || ! key.equals(ChannelCache.getKeys().get(uid))){
				channel.close().awaitUninterruptibly();
				return ;
			}
			
			
			/**
			 * 给手机号码发短信特殊使用 2013.11.20
			 */
			if(ChannelCache.SMS_UID.containsKey(uid)){
				ChannelCache.addChannel(new MyChannel(channel,uid ));
				return ;
			}
			
			
			Channel temChannel = ChannelCache.getChannelGroup().find(channel.getId());
			if( temChannel != null){
				return ;
			}
			
			String k = StringUtils.base64Decode(key);
			long t = Long.valueOf(k.substring(0,k.length() - 4));
			long now = System.currentTimeMillis();
			if( ( now - t ) > 60 * 1000 ){//密钥过期
				ResponseProcess.response(channel, "X");
				channel.close().awaitUninterruptibly();
				return ;
			}
			
			ChannelCache.addChannel(new MyChannel(channel,uid ));
			
			final MyChannel myChannel = (MyChannel) ChannelCache.getChannelGroup().find(channel.getId());
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						int uid = myChannel.getUid();
						ResponseProcess.pushMessage2Client(uid,"A:"+Business.getSumMessage(uid, -1).toString());
					} catch (Exception ex) {
						dispatchExceptionCaught(ctx, myChannel, new DefaultExceptionEvent(channel, ex));
					} finally {
					}
				}
			});
			
		}
		
		return ;
	}



	/**
	 * @Title: dispatchChannelEvent
	 * @Description: 分发通道事件(只处理通道Connected事件)
	 * @param ctx
	 * @param channel
	 * @param e
	 * @return void 返回类型
	 */
	public void dispatchChannelEvent(final ChannelHandlerContext ctx, final Channel channel,
			final ChannelStateEvent e) {
		
		if (!ChannelState.CONNECTED.equals(e.getState())) {
			return;
		}
		
		final boolean isConnected = e.getValue() != null;
		
		if(! isConnected ){
			return ;
		}
		
		channelExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					//ResponseProcess.pushMessage2Client(channel.getUid());
				} catch (Exception ex) {
				} finally {
				}
			}
		});
	}

	/**
	 * @Title: dispatchExceptionCaught
	 * @Description: 分发异常事件
	 * @param ctx
	 * @param channel
	 * @param e
	 * @return void 返回类型
	 */
	public void dispatchExceptionCaught(final ChannelHandlerContext ctx, final MyChannel channel,
			final ExceptionEvent e) {
		exceptionExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					ResponseProcess.pushMessage2Client(channel.getUid(),"Error");
				} catch (Exception ex) {
					ex.printStackTrace();
					MyLog.logError("dispatchExceptionCaught on channel {"+channel+"} error:{"+StackTraceUtil.getStackTrace(ex)+"}.");
				} finally {
				}
			}
		});
	}

	
	
	
	public void setMaximumPoolSize(int maximumPoolSize) {
		if (maximumPoolSize < Runtime.getRuntime().availableProcessors() + 1) {
			throw new IllegalArgumentException("maximumPoolSize must great than " + Runtime.getRuntime().availableProcessors());
		}

		if (this.maximumPoolSize != maximumPoolSize) {
			this.maximumPoolSize = maximumPoolSize;
			setExecutor(getExecutor());
		}
	}

	public int getMaxinumPoolSize() {
		return maximumPoolSize;
	}

	public int getQueueCapacity() {
		return queueCapacity;
	}

	public void setQueueCapacity(int queueCapacity) {
		if (queueCapacity <= 0) {
			throw new IllegalArgumentException("queueCapacity must great than 0");
		}
		if (this.queueCapacity != queueCapacity) {
			this.queueCapacity = queueCapacity;
			setExecutor(getExecutor());
		}
	}


	public void setExecutor(Executor executor) {
		if (executor == null) {
			throw new NullPointerException("executor is null.");
		}
		Executor preExecutor = this.executor;
		this.executor = executor;
		if (preExecutor instanceof ExecutorService) {
			List<Runnable> tasks = ((ExecutorService) preExecutor).shutdownNow();
			if (tasks != null && tasks.size() > 0) {
				for (Runnable task : tasks) {
					this.executor.execute(task);
				}
			}
		}
	}

	public int getExecutorActiveCount() {
		if (executor instanceof ThreadPoolExecutor) {
			return ((ThreadPoolExecutor) executor).getActiveCount();
		}
		return -1;
	}

	public long getExecutorCompletedTaskCount() {
		if (executor instanceof ThreadPoolExecutor) {
			return ((ThreadPoolExecutor) executor).getCompletedTaskCount();
		}
		return -1;
	}

	public int getExecutorLargestPoolSize() {
		if (executor instanceof ThreadPoolExecutor) {
			return ((ThreadPoolExecutor) executor).getLargestPoolSize();
		}
		return -1;
	}

	public int getExecutorPoolSize() {
		if (executor instanceof ThreadPoolExecutor) {
			return ((ThreadPoolExecutor) executor).getPoolSize();
		}
		return -1;
	}

	public long getExecutorTaskCount() {
		if (executor instanceof ThreadPoolExecutor) {
			return ((ThreadPoolExecutor) executor).getTaskCount();
		}
		return -1;
	}

	public int getExecutorQueueSize() {
		if (executor instanceof ThreadPoolExecutor) {
			return ((ThreadPoolExecutor) executor).getQueue().size();
		}
		return -1;
	}
}