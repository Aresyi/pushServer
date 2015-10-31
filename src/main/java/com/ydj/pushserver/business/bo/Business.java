package com.ydj.pushserver.business.bo;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.ydj.pushserver.business.dao.UserDao;
import com.ydj.pushserver.business.job.GroupAdminJob;
import com.ydj.pushserver.db.springjdbc.JdbcDaoFactory;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-29 下午06:54:14
 * @version : 1.0
 * @description :
 *
 */
public class Business {

	private static UserDao userDao = JdbcDaoFactory.getUserDao();
	
	/**
	 * 人脉通系统总消息
	 * @param uid
	 * @param v
	 * @return
	 */
	public static JSONObject getSumMessage(int uid,float v){
		JSONObject result = new JSONObject();
		try {
			result = userDao.scan(uid);
			
			result.put("sysCastCount", getSysCastCount(uid));
			if(GroupAdminJob.ADMIN_UIDS.contains(uid)){
				result.put("GNApplyCount",userDao.countApply(uid));
			}
			
			// 私信
			result.put("messageCount",userDao.countUnread(userDao.getTopicIdList(uid), uid));
			// 商业人脉请求
			result.put("applyCount", userDao.unreadCount(uid));
		} catch (Exception e) {
		}

		return result;
	}
	
	/**
	 * 群组消息
	 * @param uid
	 * @return
	 */
	public static List<JSONObject> getSysCastCount(int uid){
		try {
			return userDao.readSysCastNotify(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<JSONObject>();
		}
		
	}
}
