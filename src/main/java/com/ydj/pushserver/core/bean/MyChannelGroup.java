/** **/
package com.ydj.pushserver.core.bean;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.ydj.pushserver.core.ChannelCache;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-23 上午11:15:30
 * @version : 1.0
 * @description :
 *
 */
public class MyChannelGroup extends DefaultChannelGroup implements ChannelGroup {
	
	public MyChannelGroup(String name){
		super(name);
	}

	@Override
	public boolean remove(Object o) {
		if(o instanceof Channel){
			ChannelCache.removeChannel((Channel)o);
		}
		return super.remove(o);
	}

	
}
