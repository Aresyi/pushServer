package com.ydj.pushserver.services;

import com.ydj.pushserver.business.bo.PushMessageBean;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午10:04:20
 * @version : 1.0
 * @description :
 *
 */
public interface PushServices {

	/**
	 * 推送信息给指定用户
	 * @param pushMessageBean
	 * @return
	 */
	public boolean pushMessage(PushMessageBean pushMessageBean);
	
	
	/**
	 * 告知密钥
	 * @param uid
	 * @param key
	 * @return
	 */
	public boolean keys(int uid,String key);
	
	/**
	 * 获取当前连接数
	 * @return
	 */
	public int getConnectCout();
}
