package com.seba.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.seba.model.Coin;
import com.seba.model.Product;

class VendingMachineTest {

	private VendingMachine vendingMachine;

	@BeforeEach
	void setUp() {
		vendingMachine = new VendingMachine();
	}

	@Test
	void shouldIncreaseCoinCountAndBalanceWhenCoinInserted() {
		var oneDollar = Coin.ONE_DOLLAR;
		var beforeCoins = vendingMachine.getTotalCoins().get(oneDollar);
		vendingMachine.insertCoin(oneDollar);
		var afterCoins = vendingMachine.getTotalCoins().get(oneDollar);

		assertEquals(beforeCoins + 1, afterCoins);
		assertEquals(0, vendingMachine.getBalance().compareTo(BigDecimal.ONE));
	}

	@Test
	void shouldDecreaseProductStockWhenProductSelected() {
		var pepsi = Product.PEPSI;
		var beforeStock = vendingMachine.getProducts().get(pepsi);

		vendingMachine.insertCoin(Coin.TWO_DOLLARS);
		vendingMachine.selectProduct(pepsi);

		var afterStock = vendingMachine.getProducts().get(pepsi);
		assertEquals(beforeStock - 1, afterStock);
		assertEquals(0, vendingMachine.getBalance().compareTo(BigDecimal.ZERO));
	}

	@Test
	void shouldReturnInsertedCoinsOnCancel() {
		var oneDollar = Coin.ONE_DOLLAR;
		var beforeCoins = vendingMachine.getTotalCoins().get(oneDollar);

		vendingMachine.insertCoin(oneDollar);
		vendingMachine.cancelAction();

		var afterCoins = vendingMachine.getTotalCoins().get(oneDollar);
		assertEquals(beforeCoins, afterCoins);
		assertEquals(0, vendingMachine.getBalance().compareTo(BigDecimal.ZERO));
	}

	@Test
	void shouldNotDispenseProductIfOutOfStock() {
		var coke = Product.COKE;
		IntStream.rangeClosed(1, 5).forEach(value -> {
			vendingMachine.insertCoin(Coin.TWO_DOLLARS);
			vendingMachine.selectProduct(coke);
		});
		var afterStock = vendingMachine.getProducts().get(coke);
		assertEquals(0, afterStock);

		vendingMachine.insertCoin(Coin.TWO_DOLLARS);

		var beforeCoins = vendingMachine.getTotalCoins().get(Coin.TWO_DOLLARS);
		vendingMachine.selectProduct(coke);

		var afterCoins = vendingMachine.getTotalCoins().get(Coin.TWO_DOLLARS);
		assertEquals(beforeCoins, afterCoins);
		assertEquals(0, vendingMachine.getBalance().compareTo(BigDecimal.valueOf(2.00)));
	}

	@Test
	void shouldNotDispenseProductIfInsufficientBalance() {
		var water = Product.WATER;
		var beforeStock = vendingMachine.getProducts().get(water);

		vendingMachine.insertCoin(Coin.FIFTY_CENTS);
		vendingMachine.selectProduct(water);

		assertEquals(beforeStock, vendingMachine.getProducts().get(water));
	}

	@Test
	void shouldReturnChangeWhenOverpaid() {
		var coke = Product.COKE;

		var beforeFiftyCents = vendingMachine.getTotalCoins().get(Coin.FIFTY_CENTS);

		vendingMachine.insertCoin(Coin.TWO_DOLLARS);
		vendingMachine.selectProduct(coke);

		var afterFiftyCents = vendingMachine.getTotalCoins().get(Coin.FIFTY_CENTS);

		assertEquals(beforeFiftyCents - 1, afterFiftyCents);
	}

	@Test
	void shouldCancelEvenWithNoCoinsInserted() {
		vendingMachine.cancelAction();
		assertEquals(0, vendingMachine.getBalance().compareTo(BigDecimal.ZERO));
	}

	@Test
	void shouldResetMachineOnAdminReset() {
		var beforeInsertCoins = vendingMachine.getTotalCoins().get(Coin.ONE_DOLLAR);
		var beforeResetWaterStock = vendingMachine.getProducts().get(Product.WATER);

		vendingMachine.insertCoin(Coin.ONE_DOLLAR);

		var beforePurchaseCoins = vendingMachine.getTotalCoins().get(Coin.ONE_DOLLAR);
		vendingMachine.selectProduct(Product.WATER);

		var afterPurchaseWaterStock = vendingMachine.getProducts().get(Product.WATER);
		assertEquals(beforeResetWaterStock - 1, afterPurchaseWaterStock);

		var afterPurchaseCoins = vendingMachine.getTotalCoins().get(Coin.ONE_DOLLAR);
		assertEquals(beforePurchaseCoins, afterPurchaseCoins);

		vendingMachine.adminReset();
		var afterResetCoins = vendingMachine.getTotalCoins().get(Coin.ONE_DOLLAR);
		assertEquals(beforeInsertCoins, afterResetCoins);

		var afterResetWaterStock = vendingMachine.getProducts().get(Product.WATER);
		assertEquals(beforeResetWaterStock, afterResetWaterStock);
	}

	@Test
	void shouldThrowNotEnoughCoinsForChangeAndCancelPurchase() {
		vendingMachine.getTotalCoins().keySet().forEach(coin -> vendingMachine.getTotalCoins().put(coin, 0L));
		var coke = Product.COKE;
		var beforeStock = vendingMachine.getProducts().get(coke);

		vendingMachine.insertCoin(Coin.TWO_DOLLARS);
		vendingMachine.selectProduct(coke);

		assertEquals(beforeStock, vendingMachine.getProducts().get(coke));
		assertEquals(0, vendingMachine.getBalance().compareTo(BigDecimal.ZERO));
	}
}