package com.example.simplex.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class MatchUpdate {
	private String matchId; // trade id
	private String symbol;
	private BigDecimal price;
	private BigDecimal amount;
	private String direction;

	public MatchUpdate() {}

	public MatchUpdate(String symbol, BigDecimal price, BigDecimal amount, String direction) {
		this.matchId = UUID.randomUUID().toString();
		this.symbol = symbol;
		this.price = price;
		this.amount = amount;
		this.direction = direction;
	}
}
