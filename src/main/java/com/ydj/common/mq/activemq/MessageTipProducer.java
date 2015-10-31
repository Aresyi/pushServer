package com.ydj.common.mq.activemq;


import javax.jms.Destination;

import org.apache.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;

import com.ydj.common.mq.Producer;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2014-8-15 下午05:13:58
 * @version : 1.0
 * @description : 
 */
public class MessageTipProducer implements Producer{

    private static final Logger log = Logger.getLogger(MessageTipProducer.class);
    
    private JmsTemplate jmsTemplate;
    private Destination notifyQueue;

    @Override
    public void sendMessage(Object message) {
        log.info("MessageTipProducer sendMessage:"+message.toString());
        try{
            jmsTemplate.convertAndSend(notifyQueue, message);
        }catch (Exception e) {
            log.error("MessageTipProducer excption",e);
        }catch (Error e) {
            log.error("MessageTipProducer error",e);
        }
    }

    public JmsTemplate getJmsTemplate() {
        return jmsTemplate;
    }

    public void setJmsTemplate(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public Destination getNotifyQueue() {
        return notifyQueue;
    }

    public void setNotifyQueue(Destination notifyQueue) {
        this.notifyQueue = notifyQueue;
    }

}
