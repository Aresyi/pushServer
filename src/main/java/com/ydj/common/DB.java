package com.ydj.common;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2013-11-22 下午02:57:32
 * @version : 1.0
 * @description :
 *
 */
public  enum DB{
	DB1("db1"),
	DB2("db2");
		
	public String alias;
	private DB(String alias){
		this.alias = alias;
	}
}