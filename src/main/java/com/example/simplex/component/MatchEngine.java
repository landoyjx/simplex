package com.example.simplex.component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.example.simplex.model.Order;
import com.example.simplex.model.Orderbook;

@Component
public class MatchEngine {

	private Map<String, Orderbook> orderbooks;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	public MatchEngine() {
		this.orderbooks = new HashMap<String, Orderbook>();
	}

	public void placeOrder(JSONObject obj) {
		String symbol = obj.getString("symbol");
		String uid = obj.getString("uid");
		String oid = obj.getString("oid");
		String direction = obj.getString("direction");
		BigDecimal price = obj.getBigDecimal("price");
		BigDecimal amount = obj.getBigDecimal("amount");
		
		Orderbook ob = orderbooks.get(symbol);
		if (ob == null) {
			ob = new Orderbook(symbol, this);
			orderbooks.put(symbol, ob);
		}

		ob.placeOrder(new Order(symbol, uid, oid, direction, amount, price));

	}
	
	public Orderbook getOrderbook(String symbol) {
		return orderbooks.get(symbol);
	}

	public void SendResult(String topic, String json) {
		kafkaTemplate.send(topic, json);
	}

}
