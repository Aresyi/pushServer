package com.ydj.common.mq;


/**
 * 
 * @author : Ares.yi
 * @createTime : 2014-8-15 下午04:59:54
 * @version : 1.0
 * @description :
 *
 */
public interface Producer {

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(Object message);
}