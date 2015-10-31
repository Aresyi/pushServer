package com.ydj.pushserver.business.bo;

public enum GroupRoleType {

	/** 待成员(已申请，未审批) **/
	applicator,

	/** 群组成员 **/
	member,
	
	/** 管理员 **/
	manager,

	/** 创建者 **/
	creator,
}
