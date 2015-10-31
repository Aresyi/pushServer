/** **/
package com.ydj.pushserver.business.dao;

import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午09:17:31
 * @version : 1.0
 * @description :
 *
 */
public interface UserDao {

	public Set<Integer> getGroupAdmin();
	
	public JSONObject scan(int uid) throws Exception;
	
	public List<JSONObject> readSysCastNotify(int uid)throws Exception;
	
	public List<Integer> getTopicIdList(int recieveUid) throws Exception ;
	
	/**
	 * 未处理请求
	 */
	public int countApply(int uid) throws Exception ;
	
	/**
	 * 统计未读信息
	 * 
	 * @param topic
	 * @param recieveUid
	 * @return
	 * @throws Exception
	 */
	public int countUnread(List<Integer> topicIdList, int recieveUid)
			throws Exception;
	
	public int unreadCount(int uid) throws Exception;
	
	
	public JSONObject getDeviceInfo(int uid);
	
	public int getSettingValue(int uid);
	
	public int getNotifyCenterCount(int uid);
}
