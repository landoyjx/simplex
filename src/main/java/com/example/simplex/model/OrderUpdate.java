package com.example.simplex.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderUpdate {

	private String symbol;
	private String uid;
	private String oid;
	private String direction;
	private BigDecimal price; // for type price
	private BigDecimal amount; // for type amount
	private OrderUpdateType type;
	
	
	public OrderUpdate() {}
	
	public OrderUpdate(Order o, BigDecimal price, BigDecimal amount) {
		this.symbol = o.getSymbol();
		this.uid = o.getUid();
		this.oid = o.getOid();
		this.direction = o.getDirection();
		this.price = price;
		this.amount = amount;
		
		if (o.getFilled().compareTo(BigDecimal.ZERO) == 0) {
			this.type = OrderUpdateType.CREATED;
		}
		if (o.unfilled().compareTo(BigDecimal.ZERO) == 0) {
			this.type = OrderUpdateType.FILLED;
		} else {
			this.type = OrderUpdateType.PARTIALLY_FILLED;
		}
	}
	
}
