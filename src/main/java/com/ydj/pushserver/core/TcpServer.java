/** **/
package com.ydj.pushserver.core;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.comm.util.NamedThreadFactory;
import com.ydj.pushserver.core.bean.MyChannel;



/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-6 下午09:07:55
 * @version : 1.0
 * @description :
 *
 */
public class TcpServer {
	
	protected Executor bossExecutor;
	protected Executor workerExecutor;
	protected int workerCount;
	
	private ServerBootstrap bootstrap ;
	private ChannelFactory factory;
	
	/**存放Option列表*/
	private Map<String, Object> moreOptions = new HashMap<String, Object>();
	
	/**锁*/
	private final static byte[] _lock = new byte[0];
	
	
	public TcpServer() {
		this(getCachedExecutor("TRANB-BOSS-PROCESSOR"), Runtime.getRuntime().availableProcessors() + 1);
	}

	public TcpServer(Executor bossExecutor, int workerCount) {
		this(bossExecutor, getCachedExecutor("TRANB-WORKER-PROCESSOR"), workerCount);
	}
	
	public TcpServer(Executor bossExecutor, Executor workerExecutor, int workerCount) {
		if (bossExecutor == null) {
			throw new IllegalArgumentException("bossExecutor can not be null.");
		} else if (workerExecutor == null) {
			throw new IllegalArgumentException("workerExecutor can not be null.");
		} else if (workerCount <= 0) {
			throw new IllegalArgumentException("workerCount required > 0.");
		} else if (workerExecutor instanceof ThreadPoolExecutor
				&& ((ThreadPoolExecutor) workerExecutor).getMaximumPoolSize() < workerCount) {
			throw new IllegalArgumentException("the maximum pool size of workerExecutor required >= workerCount.");
		}

		this.bossExecutor = bossExecutor;
		this.workerExecutor = workerExecutor;
		this.workerCount = workerCount;

		init();
	}
	
	/**
	 * 开启服务
	 */
	public void start(int port){
		MyLog.logInfo("Start tcp server ,port:"+port,true);
		listen(port);
	}
	
	/**
	 * 停止服务
	 */
	public void stop(){
		MyLog.logInfo("Stop tcp server",true);
		synchronized (_lock) {
			try {
				_lock.notifyAll();
			} catch (Exception e) {
			}
		}
	}
	
	private void init(){
		factory = new NioServerSocketChannelFactory(bossExecutor,workerExecutor,workerCount);
		bootstrap = new ServerBootstrap(factory);
		
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		
		if(moreOptions != null && moreOptions.size() > 0){
			bootstrap.setOptions(moreOptions);
		}
		
		bootstrap.setPipelineFactory(new TcpServerPipelineFactory());
	}
	
	/**
	 * 开启TCP服务监听端口
	 * @param port
	 */
	private void listen(int port){
		
		Channel channel = bootstrap.bind(new InetSocketAddress(port));
		
		ChannelCache.addChannel(new MyChannel(channel, -1));
		
		waitShutdown();
		
		stopServer(channel);
	}

	private void waitShutdown() {
		synchronized (_lock) {
			try {
				_lock.wait();
			} catch (Exception e) {
			}
		}
	}
	
	private void stopServer(Channel channel){
		ChannelGroupFuture future = ChannelCache.getChannelGroup().close();
		channel.unbind();
		bossExecutor = null;
		workerExecutor = null;
		future.awaitUninterruptibly();
		factory.releaseExternalResources();
		
		ChannelCache.clearRes();
	}
	
	
	private static ExecutorService getCachedExecutor(String name) {
		return Executors.newCachedThreadPool(new NamedThreadFactory(name));
	}
	
	
	public Map<String, Object> getMoreOptions() {
		return moreOptions;
	}

	public void setMoreOptions(Map<String, Object> moreOptions) {
		this.moreOptions = moreOptions;
	}

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		final TcpServer server = new TcpServer();
		
		new Timer().schedule(new TimerTask() {//TEST 20s 后 自动关闭
			
			@Override
			public void run() {
				server.stop();
			}
		}, 1000 * 20);
		
		
		server.start(88);
		
	}
}
