/** **/
package com.ydj.pushserver.business.bo;

import java.io.Serializable;


/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-9-9 下午04:08:22
 * @version : 1.0
 * @description :
 *
 */
public class PushMessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int uid;
	
	private Object tcpMessage;
	
	private Object iosMessage;
	
	private int iosPushSourceType;
	
	public PushMessageBean(int uid, Object tcpMessage, Object iosMessage) {
		this(uid,tcpMessage,iosMessage,0);
	}
	
	public PushMessageBean(int uid, Object tcpMessage, Object iosMessage,
			int iosPushSourceType) {
		super();
		this.uid = uid;
		this.tcpMessage = tcpMessage;
		this.iosMessage = iosMessage;
		this.iosPushSourceType = iosPushSourceType;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public Object getTcpMessage() {
		return tcpMessage;
	}

	public void setTcpMessage(Object tcpMessage) {
		this.tcpMessage = tcpMessage;
	}

	public Object getIosMessage() {
		return iosMessage;
	}

	public void setIosMessage(Object iosMessage) {
		this.iosMessage = iosMessage;
	}

	public int getIosPushSourceType() {
		return iosPushSourceType;
	}

	public void setIosPushSourceType(int iosPushSourceType) {
		this.iosPushSourceType = iosPushSourceType;
	}
	
	
	

}
