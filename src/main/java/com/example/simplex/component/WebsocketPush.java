package com.example.simplex.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Component
public class WebsocketPush {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	public void doOrderUpdate(String info) {
		JSONObject obj = JSON.parseObject(info);
		String uid = obj.getString("uid");
		System.out.println("WebsocketPush publish order update, topic=" + ("/topic/oderUpdate/" + uid) + ", data=" + info);
		messagingTemplate.convertAndSend("/topic/orderUpdate/" + uid, info);
	}

	public void doMatchUpdate(String info) {
		JSONObject obj = JSON.parseObject(info);
		String symbol = obj.getString("symbol");
		System.out.println("WebsocketPush publish match update, topic=" + ("/topic/matchUpdate/" + symbol) + ", data=" + info);
		messagingTemplate.convertAndSend("/topic/matchUpdate/" + symbol, info);
	}

}
