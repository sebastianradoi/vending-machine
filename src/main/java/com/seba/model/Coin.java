package com.seba.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum Coin {
	FIVE_CENTS(0.05),
	TEN_CENTS(0.10),
	TWENTY_CENTS(0.20),
	FIFTY_CENTS(0.50),
	ONE_DOLLAR(1.00),
	TWO_DOLLARS(2.00);

	private final BigDecimal value;

	Coin(double value) {
		this.value = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
	}

	public BigDecimal getValue() {
		return value;
	}

}
