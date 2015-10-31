package com.ydj.common.mq.activemq;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.apache.log4j.Logger;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

/**
 * 
 * @author : Ares.yi
 * @createTime : 2014-8-15 下午05:13:58
 * @version : 1.0
 * @description : 简单封装/解析消息数据(可以使用Hessian序列化)
 */
public class MyMessageConverter implements MessageConverter {

	private static final Logger log = Logger
			.getLogger(MyMessageConverter.class);

	@SuppressWarnings("unchecked")
	@Override
	public Object fromMessage(Message message) throws JMSException,
			MessageConversionException {
		log.info("MyMessageConverter fromMessage...");
		
		if (message instanceof ObjectMessage) {
            HashMap<String, byte[]> map = (HashMap<String, byte[]>) ((ObjectMessage) message).getObjectProperty("Map");
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(map.get("POJO"));
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object returnObject = ois.readObject();
                return returnObject;
            } catch (IOException e) {
                log.error("fromMessage(Message)", e);

            } catch (ClassNotFoundException e) {
                log.error("fromMessage(Message)", e);
            }

            return null;
        } else {
            throw new JMSException("MyMessageConverter fromMessage:[" + message + "] is not Map");
        }
	}

	@Override
	public Message toMessage(Object message, Session session)
			throws JMSException, MessageConversionException {
		log.info("MyMessageConverter toMessage...");

		ActiveMQObjectMessage objMsg = (ActiveMQObjectMessage) session
				.createObjectMessage();
		
		HashMap<String, byte[]> map = new HashMap<String, byte[]>();
		
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(message);
			map.put("POJO", bos.toByteArray());
			
			objMsg.setObjectProperty("Map", map);

		} catch (IOException e) {
			log.error("MyMessageConverter toMessage error :", e);
		}
		
		return objMsg;
	}

}