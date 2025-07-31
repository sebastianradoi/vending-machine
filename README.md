# Vending Machine

A simple Java console vending machine simulator.

## How to Run

1. Make sure you have Java 17 installed
2. Build and run the application:

```bash
./gradlew build
java -cp build/classes/java/main com.seba.Main
```

## How to Use

The app shows a menu with these options:

1. **Insert Coin** - Add money (5¢, 10¢, 20¢, 50¢, $1, $2)
2. **Select Product** - Buy Coke ($1.50), Pepsi ($1.45), or Water ($0.90)
3. **Cancel Transaction** - Get your coins back
4. **Operator: Reset Machine** - Reset everything (admin)
5. **Operator: Print Coins** - Show coin inventory (admin)
0. **Exit** - Quit

### Example

1. Choose option 1 to insert coins
2. Add enough money for your product
3. Choose option 2 to select a product
4. Get your product and change