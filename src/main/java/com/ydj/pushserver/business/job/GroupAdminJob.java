package com.ydj.pushserver.business.job;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.ydj.pushserver.comm.util.MyLog;
import com.ydj.pushserver.db.springjdbc.JdbcDaoFactory;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-19 下午12:27:25
 * @version : 1.0
 * @description :
 *
 */
public class GroupAdminJob  {
	
	public static Set<Integer> ADMIN_UIDS = new HashSet<Integer>();

	public void start(){

		MyLog.logInfo("GroupAdminJob exec@"+new Date(),true);

		try {
			final Set<Integer> ADMIN_UIDS_temp = JdbcDaoFactory.getUserDao().getGroupAdmin();

			synchronized (ADMIN_UIDS) {
				if(ADMIN_UIDS_temp!=null&&ADMIN_UIDS_temp.size()>0){
					ADMIN_UIDS.clear();
					ADMIN_UIDS.addAll(ADMIN_UIDS_temp);
				}
			}
			MyLog.logInfo("[Group Admin  job reload]",true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}