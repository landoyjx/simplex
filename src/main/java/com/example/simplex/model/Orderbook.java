package com.example.simplex.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.example.simplex.component.MatchEngine;

import lombok.Getter;

public class Orderbook {

	@Getter
	private String symbol;
	
	private MatchEngine engine;

	@Getter
	private TreeMap<BigDecimal, List<Order>> bidsMap;

	@Getter
	private TreeMap<BigDecimal, List<Order>> asksMap;

	public Orderbook() {}

	public Orderbook(String symbol, MatchEngine engine) {
		this.symbol = symbol;
		this.engine = engine;
		this.asksMap = new TreeMap<>();
		this.bidsMap = new TreeMap<>(Collections.reverseOrder());
	}
	
	public void placeOrder(Order o) {
		TreeMap<BigDecimal, List<Order>> toMatchMap = null;
		TreeMap<BigDecimal, List<Order>> opMatchMap = null;
		int stopCmp = 0;
		
		if (o.getDirection().equals("buy"))
		{
			toMatchMap = this.asksMap;
			opMatchMap = this.bidsMap;
			stopCmp = 1;
		}
		else
		{
			toMatchMap = this.bidsMap;
			opMatchMap = this.asksMap;
			stopCmp = -1;
		}
		
		Set<BigDecimal> prices = toMatchMap.keySet();
		Iterator<BigDecimal> it = prices.iterator();
		while (it.hasNext()) {
			BigDecimal price = it.next();
			if (price.compareTo(o.getPrice()) == stopCmp)
				break;

			List<Order> list = toMatchMap.get(price);

			Iterator<Order> io = list.iterator();
			while (io.hasNext()) {
				Order m = io.next();
				if (m.unfilled().compareTo(o.unfilled()) == 1) {
					matchOrder(o, m, price, o.unfilled());
					break; // finished
				} else {
					matchOrder(o, m, price, m.unfilled());
					io.remove(); // continue
				}
			}
			if (list.isEmpty()) {
				toMatchMap.remove(price);
			}
		}
		
		// no order in price
		if (toMatchMap.get(o.getPrice()) != null && toMatchMap.get(o.getPrice()).isEmpty()) {
			toMatchMap.remove(o.getPrice());
		}

		// left order
		if (o.unfilled().compareTo(BigDecimal.ZERO) != 0) {
			List<Order> list = opMatchMap.get(o.getPrice());
			if (list == null) {
				list =  new ArrayList<Order>();
			}
			list.add(o);
			opMatchMap.put(o.getPrice(), list);
			
			// publish order update
			OrderUpdate ou = new OrderUpdate(o, o.getPrice(), o.unfilled());
			ou.setType(OrderUpdateType.CREATED);
			engine.SendResult("orderUpdate", JSON.toJSONString(ou));
		}
	}

	private void matchOrder(Order o, Order m, BigDecimal price, BigDecimal amount) {
		m.fill(amount);
		o.fill(amount);
		
		// publish match update
		MatchUpdate update = new MatchUpdate(o.getSymbol(), price, amount.negate(), o.getDirection());
		engine.SendResult("matchUpdate", JSON.toJSONString(update));
		
		
		// publish order update
		OrderUpdate ou = new OrderUpdate(m, price, amount);
		engine.SendResult("orderUpdate", JSON.toJSONString(ou));
	}

}
