package com.example.simplex.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.simplex.component.MatchEngine;
import com.example.simplex.model.Orderbook;

@RestController
public class ExchangeController {
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private MatchEngine enginer;
	
	@PostMapping("/newOrder")
	public String placeOrder(@RequestBody JSONObject obj) {
		
		System.out.println("\n\n-----------------\nAccept new order request: " + obj.toJSONString());
		
		String symbol = obj.getString("symbol");
		if (StringUtils.isEmpty(symbol)) {
			return "empty symbol";
		}
		String uid = obj.getString("uid");
		if (StringUtils.isEmpty(uid)) {
			return "empty uid";
		}
		String oid = obj.getString("oid");
		if (StringUtils.isEmpty(oid)) {
			return "empty oid";
		}
		
		String direction = obj.getString("direction");
		if (!direction.equals("buy") && !direction.equals("sell")) {
			return "invalid direction: " + direction;
		}
		BigDecimal price = obj.getBigDecimal("price");
		if (price.compareTo(BigDecimal.ZERO) != 1) {
			return "invalid price: " + price.toPlainString();
		}
		BigDecimal amount = obj.getBigDecimal("amount");
		if (amount.compareTo(BigDecimal.ZERO) != 1) {
			return "invalid amount: " + amount.toPlainString();
		}
		
		kafkaTemplate.send("orderTopic", obj.toJSONString());
		
		return "ok";
	}
	
	@GetMapping("/ob/{symbol}")
	public JSONObject orderbook(@PathVariable String symbol) {
		Orderbook ob = enginer.getOrderbook(symbol);
		if (ob == null) return new JSONObject();
		
		JSONObject obj = new JSONObject();
		obj.put("asks", ob.getAsksMap());
		obj.put("bids", ob.getBidsMap());
		
		return obj;
	}
	
	@GetMapping("/asks/{symbol}")
	public String asks(@PathVariable String symbol) {
		Orderbook ob = enginer.getOrderbook(symbol);
		if (ob == null) return "empty";
		String result = JSON.toJSONString(ob.getAsksMap());
		return result;
	}
	
	@GetMapping("/bids//{symbol}")
	public String bids(@PathVariable String symbol) {
		Orderbook ob = enginer.getOrderbook(symbol);
		if (ob == null) return "empty";
		String result = JSON.toJSONString(ob.getBidsMap());
		return result;
	}
	

}
