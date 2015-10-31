/** **/
package com.ydj.pushserver.db.springjdbc;


import com.ydj.pushserver.business.dao.UserDao;
import com.ydj.pushserver.startup.SystemContext;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-8-15 下午09:26:09
 * @version : 1.0
 * @description :
 *
 */
public class JdbcDaoFactory {

	private static UserDao userDao ;
	
	private JdbcDaoFactory(){
	}
	
	public static UserDao getUserDao(){
		return userDao;
	}
	
	static{
		userDao = getBean("userDao", UserDao.class);
	}
	
	private static <T> T getBean(String name,Class<T> clazz){
		return SystemContext.context.getBean(name,clazz);
	}
}
