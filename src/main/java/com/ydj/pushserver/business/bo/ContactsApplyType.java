package com.ydj.pushserver.business.bo;

/**
 * 
 * 用户申请交换名片响应处理结果类型
 * 
 */
public enum ContactsApplyType {

	undo, // 初始化未处理

	accept, // 同意交换名片请求

	ignore, // 忽略请求

	viewed // 同意信息已查看

}
