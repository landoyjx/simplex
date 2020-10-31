package com.example.simplex.model;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Order {
	private String symbol;
	private String uid;
	private String oid;
	private String direction;
	private BigDecimal price;
	private BigDecimal amount;
	private BigDecimal filled;
	
	public Order() {}
	
	public Order(String symbol, String uid, String oid, String direction, BigDecimal amount, BigDecimal price) {
		this.symbol = symbol;
		this.uid = uid;
		this.oid = oid;
		this.direction = direction;
		this.amount = amount;
		this.price = price;
		this.filled = BigDecimal.ZERO;
	}
	
	public BigDecimal unfilled() {
		return this.amount.subtract(this.filled);
	}

	public void fill(BigDecimal amount) {
		this.filled = this.filled.add(amount);
	}
}
