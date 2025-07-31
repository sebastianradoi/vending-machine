package com.seba.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ProductTest {

	@Test
	void fromNumber() {
		assertEquals(Product.COKE, Product.fromNumber(1));
		assertEquals(Product.PEPSI, Product.fromNumber(2));
		assertEquals(Product.WATER, Product.fromNumber(3));
		assertThrows(IllegalArgumentException.class, () -> Product.fromNumber(4));
	}
}