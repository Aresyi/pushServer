/** **/
package com.ydj.pushserver.business.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONObject;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.ydj.common.DB;
import com.ydj.common.MultiDataSourceDaoSupport;
import com.ydj.pushserver.business.bo.CommonUtils;
import com.ydj.pushserver.business.bo.GroupRoleType;
import com.ydj.pushserver.business.bo.JSONPropertyRowMapper;
import com.ydj.pushserver.business.dao.UserDao;
import com.ydj.pushserver.comm.util.MyLog;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午09:19:04
 * @version : 1.0
 * @description :
 *
 */
public class UserDaoImpl extends MultiDataSourceDaoSupport implements UserDao {


	@Override
	public Set<Integer> getGroupAdmin() {
		final Set<Integer> ADMIN_UIDS_temp = new HashSet<Integer>();
		this.getJdbcTemplate().query("SELECT DISTINCT uid FROM group_member WHERE role >= ? ",
						new Object[]{GroupRoleType.manager.ordinal()},
						new RowMapper<JSONObject>(){
					public JSONObject mapRow(ResultSet rs,int arg1) throws SQLException {
						ADMIN_UIDS_temp.add(rs.getInt("uid"));
						return null;
					}
				});
		return ADMIN_UIDS_temp;
	}

	@Override
	public JSONObject scan(int uid) throws Exception {

		try {
			return getJdbcTemplate()
					.queryForObject(
							"SELECT sysNotifyCount,applyCount,messageCount,commentCount,praiseCount,bargainCount,purchaseCount,spreadCount FROM notify_center WHERE uid=?",
							new Object[] { uid }, new JSONPropertyRowMapper());
		} catch (IncorrectResultSizeDataAccessException e) {
			return new JSONObject();
		}
	}
	
	@Override
	public List<JSONObject> readSysCastNotify(int uid)throws Exception {
		 //this.getJdbcTemplate().update("update sys_cast set count = 0 where sid=? and type=?",new Object[]{groupId,SysCastType.groupCast.ordinal()});
		 return this.getJdbcTemplate().query("select type,sid,count from sys_cast where uid=? and count!=0", new Object[]{uid},new JSONPropertyRowMapper());
	}
	
	@Override
	public List<Integer> getTopicIdList(int recieveUid) throws Exception {

//		try {
//			return getJdbcTemplate()
//					.queryForList(
//							"SELECT id FROM message_topic WHERE recieveUid=? AND deleteBy<>?",
//							new Object[] { recieveUid, recieveUid },
//							Integer.class);
//		} catch (IncorrectResultSizeDataAccessException e) {
//			return null;
//		}
		
		
		try {
			return getJdbcTemplate(DB.DB1)
					.queryForList(
							"SELECT topicId FROM message_topic_"+( 1000 + recieveUid % 128 )+" WHERE sendUid=? AND unRead=1",
							new Object[] { recieveUid },
							Integer.class);
		} catch (IncorrectResultSizeDataAccessException e) {
			return null;
		}
		
	}
	
	/**
	 * 未处理请求
	 */
	@SuppressWarnings("deprecation")
	public int countApply(int uid) throws Exception {
		return this.getJdbcTemplate().queryForInt(
				"SELECT COUNT(id) FROM group_member WHERE groupId IN(SELECT groupId FROM group_member WHERE uid=? AND role>=?) AND role=?",
				new Object[]{uid,GroupRoleType.manager.ordinal(),GroupRoleType.applicator.ordinal()});
	}
	
	@SuppressWarnings("deprecation")
	public int countUnread(List<Integer> topicIdList, int recieveUid)throws Exception {

		if (null == topicIdList || topicIdList.isEmpty()) {
			return 0;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append(
				"SELECT COUNT(id) FROM message_reply WHERE unRead=1 AND UID<>? AND deleteBy<>? AND parentId IN(")
				.append(CommonUtils.collectionToString(topicIdList))
				.append(")");
		
		return getJdbcTemplate(DB.DB2).queryForInt(sql.toString(),
				new Object[] { recieveUid, recieveUid });
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public int unreadCount(int uid) throws Exception {
		try{
			return this.getJdbcTemplate().queryForInt("select applyCount from notify_center where uid=?", new Object[]{uid});
		}catch(Exception e){
			return 0;
		}

//		return getJdbcTemplate()
//				.queryForInt(
//						"SELECT COUNT(id) FROM contacts_apply WHERE toUid=? AND typeOf=?",
//						new Object[] { uid, ContactsApplyType.undo.ordinal() });
		
	}

	@Override
	public JSONObject getDeviceInfo(int uid) {
		JSONObject deviceInfo = null;
		try {
			deviceInfo = getJdbcTemplate(DB.DB1)
					.queryForObject(
							"SELECT deviceStoken,curPackage,pushSetting FROM account_extension WHERE uid=? limit 1",
							new Object[] { uid },
							new RowMapper<JSONObject>() {
								public JSONObject mapRow(ResultSet rs, int i)
										throws SQLException {
									JSONObject obj = new JSONObject();
									obj.put("deviceStoken",
											rs.getString("deviceStoken"));
									obj.put("curPackage",
											rs.getInt("curPackage"));
									obj.put("pushSetting",
											rs.getInt("pushSetting"));
									return obj;
								}
							});
		} catch (IncorrectResultSizeDataAccessException e) {
		}
		return deviceInfo;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getSettingValue(int uid) {
		try {
			return (getJdbcTemplate(DB.DB1)
					.queryForInt(
							"SELECT privacySetting FROM user_info WHERE uid=? limit 1",
							new Object[] { uid }));
		} catch (Exception e) {
			MyLog.logError("getSettingValue(),but uid no exit:"+uid, true);
			return 0;
		}
		
	}
	
	
	 /**
	  * 系统通知总数
	  * @param uid
	  * @return
	  */
	public int getNotifyCenterCount(int uid){
			try {
				return getJdbcTemplate()
						.queryForObject(
								"SELECT (sysNotifyCount+applyCount+messageCount+commentCount+praiseCount) as cou FROM notify_center WHERE uid=? limit 1",
								new Object[] { uid }, Integer.class);
			} catch (Exception e) {
				return 0;
			}
	}
	
	
	
}
