package com.seba;

import java.util.Scanner;

import com.seba.model.Coin;
import com.seba.model.Product;
import com.seba.service.VendingMachine;

public class Main {
	public static void main(String[] args) {
		VendingMachine vm = new VendingMachine();
		try (Scanner scanner = new Scanner(System.in)) {
			boolean running = true;

			while (running) {
				System.out.println("\n--- Vending Machine ---");
				System.out.printf("Current balance: %.2f\n", vm.getBalance());
				System.out.println("1. Insert Coin");
				System.out.println("2. Select Product");
				System.out.println("3. Cancel Transaction");
				System.out.println("4. Operator: Reset Machine");
				System.out.println("5. Operator: Print Stats");
				System.out.println("0. Exit");
				System.out.print("Choose an option: ");

				int choice = vmReadInt(scanner);
				switch (choice) {
					case 1:

						System.out.println("\n--- Available coins ---");
						System.out.println("1. 0.05 (5 cents)");
						System.out.println("2. 0.10 (10 cents)");
						System.out.println("3. 0.20 (20 cents)");
						System.out.println("4. 0.50 (50 cents)");
						System.out.println("5. 1.00 (1 dollar)");
						System.out.println("6. 2.00 (2 dollars)");
						System.out.println("7. Cancel insert coin");

						var coinChoice = vmReadInt(scanner);
						switch (coinChoice) {
							case 1 -> vm.insertCoin(Coin.FIVE_CENTS);
							case 2 -> vm.insertCoin(Coin.TEN_CENTS);
							case 3 -> vm.insertCoin(Coin.TWENTY_CENTS);
							case 4 -> vm.insertCoin(Coin.FIFTY_CENTS);
							case 5 -> vm.insertCoin(Coin.ONE_DOLLAR);
							case 6 -> vm.insertCoin(Coin.TWO_DOLLARS);
							case 7 -> System.out.println("Coin insertion cancelled.");
							default -> System.out.println("Invalid coin choice.");
						}
						break;
					case 2:
						System.out.println("Products:");
						for (int i = 0; i < Product.values().length; i++) {
							Product p = Product.values()[i];
							System.out.printf("%d. %s - %.2f\n", i + 1, p.getName(), p.getPrice());
						}
						System.out.print("Select product number: ");
						int prodChoice = vmReadInt(scanner);
						try {
							vm.selectProduct(Product.fromNumber(prodChoice));
						} catch (IllegalArgumentException e) {
							System.out.println("Invalid product choice.");
						}
						break;
					case 3:
						vm.cancelAction();
						break;
					case 4:
						vm.adminReset();
						break;
					case 5:
						vm.printStats();
						break;
					case 0:
						running = false;
						System.out.println("Goodbye!");
						break;
					default:
						System.out.println("Invalid option.");
				}
			}
		}
	}

	private static int vmReadInt(Scanner scanner) {
		while (!scanner.hasNextInt()) {
			System.out.println("Please enter a valid number.");
			scanner.next();
		}
		int num = scanner.nextInt();
		scanner.nextLine();
		return num;
	}
}