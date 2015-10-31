package com.ydj.pushserver.business.bo;

/**
 * 
 * ios push 源
 * 
 */
public enum IOSPushSourceType {

	defaultS, // 初始化未处理

	cardExchange, // 名片交换请求

	message, // 私信

	invitePerfectUserInfo // 邀请完善个人资料

}
