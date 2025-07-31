package com.seba.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Product {
	COKE("Coke", 1.50),
	PEPSI("Pepsi", 1.45),
	WATER("Water", 0.90);
	private final String name;
	private final BigDecimal price;

	Product(String name, double price) {
		this.name = name;
		this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP);
	}

	public static Product fromNumber(int number) {
		return switch (number) {
			case 1 -> COKE;
			case 2 -> PEPSI;
			case 3 -> WATER;
			default -> throw new IllegalArgumentException("Invalid product number: " + number);
		};
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}
}
