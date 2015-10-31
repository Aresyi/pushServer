package com.ydj.pushserver.business.bo;

/**
 * 
 * 用户设置
 * 
 * 7 6 5 4 3 2 1 0
 * x x x x x x x x
 * 
 * a：隐私设置
 *    0位：(0：邮件可见；1：邮件不可见)
 *    1位：(0：手机可见；1：手机不可见)
 * b：提示音设置
 * 	  2位：(0：声音打开；1：声音关闭)
 * c：集中推送
 * 	  3位：(0：关闭集中推送；1：开启集中推送)
 * d：勿扰设置
 * 	  4位：(0：关闭勿扰设置；1：开启勿扰设置)
 */
public class PrivateSettingUtils {
	
	/**
	 * 是否设置了 勿扰设置
	 * 勿扰设置 第4位
	 * @param privateSettingValue
	 * @return
	 */
	public static boolean isNotFaze(int privateSettingValue){
		return (privateSettingValue >> 4) % 2 == 0 ? false : true;
	}
	
	/**
	 * 是否设置了 集中推送
	 * 集中推送 第3位
	 * @param privateSettingValue
	 * @return
	 */
	public static boolean isPushByTime(int privateSettingValue){
		return (privateSettingValue >> 3) % 2 == 0 ? false : true;
	}
	
	
	/**
	 * 是否设置了 提示音设置
	 * 提示音设置 第2位
	 * @param privateSettingValue
	 * @return
	 */
	public static boolean isAlertSound(int privateSettingValue){
		return (privateSettingValue >> 2) % 2 == 0 ? true : false;
	}
	
	public static void main(String[] args){
		int value  = Integer.valueOf("00011101", 2);
		System.out.println("[value]"+value);
		System.out.println(isAlertSound(4));
		System.out.println(isNotFaze(value));
	}
}
