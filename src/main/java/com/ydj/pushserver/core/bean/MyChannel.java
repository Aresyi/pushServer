/** **/
package com.ydj.pushserver.core.bean;

import java.net.SocketAddress;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelConfig;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;

import com.ydj.pushserver.core.ChannelCache;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午12:26:21
 * @version : 1.0
 * @description :
 *
 */
public class MyChannel implements Channel {
	
	/**用户UID*/
	private int uid;
	
	/**连接通道*/
	private Channel channel;

	
	public MyChannel(Channel channel , int uid){
		if (channel == null) {
			throw new IllegalArgumentException("channel can not be null.");
		}
		this.uid = uid;
		this.channel = channel;
	}
	

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Channel o) {
		return channel.compareTo(o);
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getId()
	 */
	@Override
	public Integer getId() {
		return channel.getId();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getFactory()
	 */
	@Override
	public ChannelFactory getFactory() {
		return channel.getFactory();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getParent()
	 */
	@Override
	public Channel getParent() {
		return channel.getParent();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getConfig()
	 */
	@Override
	public ChannelConfig getConfig() {
		return channel.getConfig();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getPipeline()
	 */
	@Override
	public ChannelPipeline getPipeline() {
		return channel.getPipeline();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#isOpen()
	 */
	@Override
	public boolean isOpen() {
		return channel.isOpen();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#isBound()
	 */
	@Override
	public boolean isBound() {
		return channel.isBound();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#isConnected()
	 */
	@Override
	public boolean isConnected() {
		return channel.isConnected();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getLocalAddress()
	 */
	@Override
	public SocketAddress getLocalAddress() {
		return channel.getLocalAddress();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getRemoteAddress()
	 */
	@Override
	public SocketAddress getRemoteAddress() {
		return channel.getRemoteAddress();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#write(java.lang.Object)
	 */
	@Override
	public ChannelFuture write(Object message) {
		ChannelFuture future = channel.write(message);
		return future;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#write(java.lang.Object, java.net.SocketAddress)
	 */
	@Override
	public ChannelFuture write(Object message, SocketAddress remoteAddress) {
		ChannelFuture future = channel.write(message, remoteAddress);
		return future;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#bind(java.net.SocketAddress)
	 */
	@Override
	public ChannelFuture bind(SocketAddress localAddress) {
		return channel.bind(localAddress);
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#connect(java.net.SocketAddress)
	 */
	@Override
	public ChannelFuture connect(SocketAddress remoteAddress) {
		return channel.connect(remoteAddress);
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#disconnect()
	 */
	@Override
	public ChannelFuture disconnect() {
		return channel.disconnect();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#unbind()
	 */
	@Override
	public ChannelFuture unbind() {
		return channel.unbind();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#close()
	 */
	@Override
	public ChannelFuture close() {
		ChannelCache.removeChannel(channel);
		return channel.close();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getCloseFuture()
	 */
	@Override
	public ChannelFuture getCloseFuture() {
		return channel.getCloseFuture();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getInterestOps()
	 */
	@Override
	public int getInterestOps() {
		return channel.getInterestOps();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#isReadable()
	 */
	@Override
	public boolean isReadable() {
		return channel.isReadable();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#isWritable()
	 */
	@Override
	public boolean isWritable() {
		return channel.isWritable();
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#setInterestOps(int)
	 */
	@Override
	public ChannelFuture setInterestOps(int interestOps) {
		return channel.setInterestOps(interestOps);
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#setReadable(boolean)
	 */
	@Override
	public ChannelFuture setReadable(boolean readable) {
		return channel.setReadable(readable);
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#getAttachment()
	 */
	@Override
	public Object getAttachment() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jboss.netty.channel.Channel#setAttachment(java.lang.Object)
	 */
	@Override
	public void setAttachment(Object attachment) {
	}

}
