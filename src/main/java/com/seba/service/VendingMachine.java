package com.seba.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.seba.exceptions.NotEnoughCoinsForChange;
import com.seba.model.Coin;
import com.seba.model.Product;

public class VendingMachine {
	private Map<Product, Long> products;
	private Map<Coin, Long> totalCoins;
	private List<Coin> insertedCoins;
	private BigDecimal balance;

	public VendingMachine() {
		initMachine();
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void insertCoin(Coin coin) {
		insertedCoins.add(coin);
		balance = balance.add(coin.getValue());
		totalCoins.compute(coin, (k, v) -> v == null ? 1 : v + 1);
		System.out.printf("Inserted coin: %.2f. Current balance: %.2f%n", coin.getValue(), balance);
	}

	public void selectProduct(Product product) {
		if (products.get(product) == 0) {
			System.out.printf("Selected product %s is out of stock.%n", product.getName());
			return;
		}
		if (balance.compareTo(product.getPrice()) < 0) {
			System.out.printf("Insufficient balance. Please insert more coins. Current balance: %.2f, Product price: %.2f%n", balance, product.getPrice());
			return;
		}
		dispenseProduct(product);
	}

	public void cancelAction() {
		if (insertedCoins.isEmpty()) {
			System.out.println("No coins inserted to cancel.");
			return;
		}
		insertedCoins.forEach(c -> {
			totalCoins.compute(c, (k, v) -> v - 1);
		});
		System.out.println("Action cancelled. Returning inserted coins: " +
				insertedCoins.stream()
						.map(Coin::getValue)
						.map(value -> String.format("%.2f", value))
						.collect(Collectors.joining(", ", "[", "]")));
		balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		insertedCoins.clear();
	}

	public void adminReset() {
		System.out.println("Admin reset initiated. Resetting vending machine...");
		System.out.printf("Total credit before reset: %.2f$\n", getTotalCash()
		);
		resetMachine();
		System.out.println("Admin reset successful. Vending machine is ready for use.");
	}

	private double getTotalCash() {
		return totalCoins.entrySet().stream()
				.mapToDouble(v -> v.getKey().getValue().multiply(BigDecimal.valueOf(v.getValue())).doubleValue())
				.sum();
	}

	public void printStats() {
		System.out.println("\nCurrent coins in the machine:");
		totalCoins.forEach((coin, count) -> {
			System.out.printf(" - %s: %d%n", coin.name(), count);
		});

		System.out.printf("\nTotal cash: %.2f$%n", getTotalCash());

		System.out.println("\nAvailable products:");
		products.forEach((product, count) -> {
			System.out.printf(" - %s (%.2f$): %d%n", product.getName(), product.getPrice(), count);
		});
	}

	protected void initMachine() {
		this.products = new EnumMap<>(Product.class);
		this.products.putAll(Map.of(
				Product.COKE, 5L,
				Product.PEPSI, 5L,
				Product.WATER, 5L
		));
		this.totalCoins = new EnumMap<>(Coin.class);
		totalCoins.putAll(Map.of(
				Coin.FIVE_CENTS, 10L,
				Coin.TEN_CENTS, 10L,
				Coin.TWENTY_CENTS, 10L,
				Coin.FIFTY_CENTS, 10L,
				Coin.ONE_DOLLAR, 5L,
				Coin.TWO_DOLLARS, 5L
		));
		this.insertedCoins = new ArrayList<>();
		this.balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
	}

	private void resetMachine() {
		initMachine();
		System.out.println("Vending machine has been reset.");
	}

	private void dispenseProduct(Product product) {
		products.computeIfPresent(product, (k, v) -> v - 1);
		balance = balance.subtract(product.getPrice());

		if (balance.compareTo(BigDecimal.ZERO) > 0) {
			try {
				var change = calculateChange();
				System.out.printf("Dispensed product: %s. Remaining balance: %.2f%n", product.getName(), balance);
				System.out.println("Change returned: " + change.stream()
						.map(Coin::getValue)
						.map(value -> String.format("%.2f", value))
						.collect(Collectors.joining(", ")));
				System.out.println("Thank you for your purchase!");

			} catch (NotEnoughCoinsForChange e) {
				products.computeIfPresent(product, (k, v) -> v + 1);
				cancelAction();
			}

		}
	}

	private List<Coin> calculateChange() throws NotEnoughCoinsForChange {
		var totalCoinsSnapshot = new EnumMap<>(totalCoins);
		var localBalance = this.balance;

		List<Coin> change = new ArrayList<>();
		List<Coin> sortedAvailableCoins = Arrays.stream(Coin.values())
				.sorted(Comparator.reverseOrder())
				.toList();
		for (Coin coin : sortedAvailableCoins) {
			while (localBalance.compareTo(BigDecimal.ZERO) != 0 && localBalance.compareTo(coin.getValue()) >= 0) {
				if (totalCoins.get(coin) == 0) {
					break;
				}
				change.add(coin);
				localBalance = localBalance.subtract(coin.getValue());
				totalCoins.computeIfPresent(coin, (k, v) -> v - 1);
			}
			if (localBalance.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
		}
		if (localBalance.compareTo(BigDecimal.ZERO) > 0) {
			this.totalCoins = totalCoinsSnapshot;
			throw new NotEnoughCoinsForChange();
		}
		this.balance = localBalance;
		return change;
	}

	Map<Coin, Long> getTotalCoins() {
		return totalCoins;
	}

	Map<Product, Long> getProducts() {
		return products;
	}

}
