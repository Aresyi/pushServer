package com.ydj.pushserver.business.bo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.JdbcUtils;


public class JSONPropertyRowMapper implements RowMapper<JSONObject> {

	public JSONPropertyRowMapper(List<Integer> UIDS) {
		super();
		this.UIDS = UIDS;
	}
	public JSONPropertyRowMapper() {
		this(null);
	}

	public JSONObject mapRow(ResultSet rs, int rowNumber) throws SQLException {

		JSONObject json = new JSONObject();
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();

		for (int index = 1; index <= columnCount; index++) {
			String column = JdbcUtils.lookupColumnName(rsmd, index);
			Object value = rs.getObject(column);
			if (null != value && !CommonUtils.isEmptyString(value.toString()))
				json.put(column, "null".equalsIgnoreCase(value.toString()) ? ""
						: value);
		}

		if (null != UIDS) {
			try {
				int uid;
				if ((uid = json.getInt("UID")) != 0)
					UIDS.add(uid);
			} catch (Exception e) {
				throw new IllegalArgumentException("");
			}
		}

		return json;
	}

	public List<JSONObject> getUName(JdbcDaoSupport dao, List<JSONObject> list)
			throws Exception {

		
		if (null == UIDS || UIDS.isEmpty() || UIDS.size() != list.size())
			;
		else {
			final Map<String, String> map = new HashMap<String, String>();
			dao.getJdbcTemplate()
					.query(new StringBuilder(
							"SELECT uid,chineseName,vAuth FROM user_info WHERE uid IN(")
							.append(CommonUtils.collectionToString(UIDS))
							.append(")").toString(), new RowCallbackHandler() {

						public void processRow(ResultSet rs)
								throws SQLException {
							map.put(rs.getInt("uid") + "_Name",
									rs.getString("chineseName"));

							map.put(rs.getInt("uid") + "_vAuth",
									rs.getInt("vAuth") + "");
							// map.put(rs.getInt("uid") + "_Avatar",
							// rs.getString("avatar"));
						}
					});
			for (JSONObject json : list) {
				json.put("UName", map.get(json.getInt("UID") + "_Name"));
				json.put("vAuth", map.get(json.getInt("UID") + "_vAuth"));
				// json.put("avatar", map.get(json.getInt("UID") + "_Avatar"));
			}
		}

		return list;
	}
	
	public List<JSONObject> getUName2(JdbcDaoSupport dao, List<JSONObject> list)throws Exception {

		
		if (null == UIDS || UIDS.isEmpty())
			;
		else {
			final Map<String, String> map = new HashMap<String, String>();
			dao.getJdbcTemplate()
					.query(new StringBuilder(
							"SELECT uid,chineseName,vAuth FROM user_info WHERE uid IN(")
							.append(CommonUtils.collectionToString(UIDS))
							.append(")").toString(), new RowCallbackHandler() {
		
						public void processRow(ResultSet rs)
								throws SQLException {
							map.put(rs.getInt("uid") + "_Name",
									rs.getString("chineseName"));
							
							map.put(rs.getInt("uid") + "_vAuth",rs.getInt("vAuth")+"");
							// map.put(rs.getInt("uid") + "_Avatar",
							// rs.getString("avatar"));
						}
					});
			
			for (JSONObject json : list) {
				json.put("UName2", map.get(json.getInt("UID2") + "_Name"));
				json.put("vAuth2", map.get(json.getInt("UID2") + "_vAuth"));
				// json.put("avatar", map.get(json.getInt("UID") + "_Avatar"));
			}
		}
		
		return list;
	}

	public List<JSONObject> getUserInfo(JdbcDaoSupport dao,
			List<JSONObject> list) throws Exception {

		if (null == UIDS || UIDS.isEmpty() || UIDS.size() != list.size())
			;
		else {
			final Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
			dao.getJdbcTemplate()
					.query(new StringBuilder(
							"SELECT uid,chineseName,job,company,department,industry,industryUnionCode,city,sinaWeiboId,linkedInId,imQq,qqOpenId,supplyInfo,demandInfo,recruitInfo,vAuth FROM user_info WHERE uid IN(")
							.append(CommonUtils.collectionToString(UIDS))
							.append(")").toString(), new RowCallbackHandler() {
						public void processRow(ResultSet rs)
								throws SQLException {
							JSONObject jt = new JSONObject();
							jt.put("job", rs.getString("job"));
							jt.put("industry", rs.getInt("industry"));
							jt.put("company", rs.getString("company"));
							jt.put("department", rs.getString("department"));
							jt.put("industryUnionCode",
									rs.getString("industryUnionCode"));
							jt.put("city", rs.getString("city"));
							jt.put("linkedInId", rs.getString("linkedInId"));
							jt.put("sinaWeiboId", rs.getLong("sinaWeiboId"));
							jt.put("imQq", rs.getString("imQq"));
							jt.put("qqOpenId", rs.getString("qqOpenId"));
							jt.put("uName", rs.getString("chineseName"));
							jt.put("supplyInfo", rs.getString("supplyInfo"));
							jt.put("demandInfo", rs.getString("demandInfo"));
							jt.put("recruitInfo", rs.getString("recruitInfo"));
							jt.put("vAuth", rs.getInt("vAuth"));
							// jt.put("avatar", rs.getString("avatar"));
							map.put(rs.getInt("uid"), jt);
						}
					});
			JSONObject jt = null;
			for (JSONObject json : list) {
				jt = map.get(json.getInt("UID"));
				if (jt != null) {
					json.put("UName", jt.getString("uName"));
					json.put("job", jt.getString("job"));
					json.put("industry", jt.getInt("industry"));
					json.put("company", jt.getString("company"));
					json.put("industryUnionCode",
							jt.getString("industryUnionCode"));
					json.put("city", jt.getString("city"));
					json.put("linkedInId", jt.getString("linkedInId"));
					json.put("sinaWeiboId", jt.getLong("sinaWeiboId"));
					json.put("imQq", jt.getString("imQq"));
					json.put("qqOpenId", jt.getString("qqOpenId"));
					json.put("department", jt.getString("department"));
					json.put("supplyInfo", jt.getString("supplyInfo"));
					json.put("demandInfo", jt.getString("demandInfo"));
					json.put("recruitInfo", jt.getString("recruitInfo"));
					json.put("vAuth", jt.getInt("vAuth"));
					// jt = null;
				}
			}
		}

		return list;
	}

	
	public List<JSONObject> getUserSimpleInfo(JdbcDaoSupport dao,
			List<JSONObject> list) throws Exception {

		if (null == UIDS || UIDS.isEmpty() || UIDS.size() != list.size())
			;
		else {
			final Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
			dao.getJdbcTemplate()
					.query(new StringBuilder(
							"SELECT uid,chineseName,job,company,vAuth FROM user_info WHERE uid IN(")
							.append(CommonUtils.collectionToString(UIDS))
							.append(")").toString(), new RowCallbackHandler() {
						public void processRow(ResultSet rs)
								throws SQLException {
							JSONObject jt = new JSONObject();
							jt.put("job", rs.getString("job"));
							jt.put("company", rs.getString("company"));
							jt.put("uName", rs.getString("chineseName"));
							jt.put("vAuth", rs.getInt("vAuth"));
							map.put(rs.getInt("uid"), jt);
						}
					});
			JSONObject jt = null;
			for (JSONObject json : list) {
				jt = map.get(json.getInt("UID"));
				if (jt != null) {
					json.put("UName", jt.getString("uName"));
					json.put("job", jt.getString("job"));
					json.put("company", jt.getString("company"));
					json.put("vAuth", jt.getInt("vAuth"));
					// jt = null;
				}
			}
		}

		return list;
	}
	
	/**
	 * 供需聘处理
	 * 
	 * @param dao
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public List<JSONObject> getSDRUserBasicInfo(JdbcDaoSupport dao,
			List<JSONObject> list, int pageNumber) throws Exception {

		if (null == UIDS || UIDS.isEmpty() || UIDS.size() != list.size())
			;
		else {
			final Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
			dao.getJdbcTemplate()
					.query(new StringBuilder(
							"SELECT uid,chineseName,industry,industryUnionCode,city,vAuth FROM user_info WHERE uid IN(")
							.append(CommonUtils.collectionToString(UIDS))
							.append(")").toString(), new RowCallbackHandler() {
						public void processRow(ResultSet rs)
								throws SQLException {
							JSONObject jt = new JSONObject();
							jt.put("industry", rs.getInt("industry"));
							jt.put("industryUnionCode",
									rs.getString("industryUnionCode"));
							jt.put("city", rs.getString("city"));
							jt.put("uName", rs.getString("chineseName"));
							jt.put("vAuth", rs.getInt("vAuth"));
							map.put(rs.getInt("uid"), jt);
						}
					});
			JSONObject jt = null;
			int STATE_MAX = 40;
			long currentTime = System.currentTimeMillis() - 1 * 60 * 60 * 1000l;
			for (JSONObject json : list) {

				if (pageNumber / STATE_MAX == 0)
					json.put("state", 1);// 今天
				else if (pageNumber / STATE_MAX == 1)
					json.put("state", 2);// 昨天
				else if (pageNumber / STATE_MAX > 1)
					json.put("state", 3);// 更早

				if (currentTime < json.getLong("modifyTime"))
					json.put("state", 4);// 刚刚

				jt = map.get(json.getInt("UID"));
				if (jt != null) {
					json.put("UName", jt.getString("uName"));
					json.put("industry", jt.getInt("industry"));
					json.put("industryUnionCode",
							jt.getString("industryUnionCode"));
					json.put("city", jt.getString("city"));
					json.put("vAuth", jt.getInt("vAuth"));
				}
			}
		}

		return list;
	}

	public List<JSONObject> getUserBasicInfo(JdbcDaoSupport dao,
			List<JSONObject> list) throws Exception {

		if (null == UIDS || UIDS.isEmpty() || UIDS.size() != list.size())
			;
		else {
			final Map<Integer, JSONObject> map = new HashMap<Integer, JSONObject>();
			dao.getJdbcTemplate()
					.query(new StringBuilder(
							"SELECT uid,chineseName,industry,industryUnionCode,city,vAuth FROM user_info WHERE uid IN(")
							.append(CommonUtils.collectionToString(UIDS))
							.append(")").toString(), new RowCallbackHandler() {
						public void processRow(ResultSet rs)
								throws SQLException {
							JSONObject jt = new JSONObject();
							jt.put("industry", rs.getInt("industry"));
							jt.put("industryUnionCode",
									rs.getString("industryUnionCode"));
							jt.put("city", rs.getString("city"));
							jt.put("uName", rs.getString("chineseName"));
							jt.put("vAuth", rs.getInt("vAuth"));
							map.put(rs.getInt("uid"), jt);
						}
					});
			JSONObject jt = null;
			for (JSONObject json : list) {
				jt = map.get(json.getInt("UID"));
				if (jt != null) {
					json.put("UName", jt.getString("uName"));
					json.put("industry", jt.getInt("industry"));
					json.put("industryUnionCode",
							jt.getString("industryUnionCode"));
					json.put("city", jt.getString("city"));
					json.put("vAuth", jt.getInt("vAuth"));
				}
			}
		}

		return list;
	}

	public JSONObject getUName(JdbcDaoSupport dao, final JSONObject json)
			throws Exception {

		if (null == UIDS || UIDS.size() != 1)
			;
		else {
			dao.getJdbcTemplate().query(
					"SELECT chineseName FROM user_info WHERE uid=?",
					new Object[] { UIDS.get(0) }, new RowCallbackHandler() {

						public void processRow(ResultSet rs)
								throws SQLException {
							json.put("UName", rs.getString("chineseName"));
						}
					});
		}

		return json;
	}
	
	public JSONObject getUserSimpleInfo(JdbcDaoSupport dao, final JSONObject json)throws Exception {

		if (null == UIDS || UIDS.size() != 1)
			;
		else {
			dao.getJdbcTemplate().query(
					"SELECT  uid,chineseName,job,company,vAuth  FROM user_info WHERE uid=?",
					new Object[] { UIDS.get(0) }, new RowCallbackHandler() {
		
						public void processRow(ResultSet rs)
								throws SQLException {
							json.put("UName", rs.getString("chineseName"));
							json.put("job", rs.getString("job"));
							json.put("company", rs.getString("company"));
							json.put("vAuth", rs.getInt("vAuth"));
						}
					});
		}
		
		return json;
	}

	
	private List<Integer> UIDS;

}
