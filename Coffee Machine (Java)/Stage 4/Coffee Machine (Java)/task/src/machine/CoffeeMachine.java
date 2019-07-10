package machine;

import java.util.Scanner;

public class CoffeeMachine {

    static int water = 1200;
    static int milk = 540;
    static int beans = 120;
    static int cups = 9;
    static int money = 550;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        summary();
        String action = scanner.nextLine();
        switch (action) {
            case "buy": {
                System.out.print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino: ");
                int task = scanner.nextInt();
                System.out.println();
                switch (task) {
                    case 1: {
                        water -= 250;
                        beans -= 16;
                        cups--;
                        money += 4;
                        break;
                    }
                    case 2: {
                        water -= 350;
                        milk -= 75;
                        beans -= 20;
                        cups--;
                        money += 7;
                        break;
                    }
                    case 3: {
                        water -= 200;
                        milk -= 100;
                        beans -= 12;
                        cups--;
                        money += 6;
                        break;
                    }
                }
                break;
            }
            case "fill": {
                System.out.print("Write how many ml of water do you want to add: ");
                int waterAdd = scanner.nextInt();
                System.out.print("Write how many ml of milk do you want to add: ");
                int milkAdd = scanner.nextInt();
                System.out.print("Write how many grams of coffee beans do you want to add: ");
                int beansAdd = scanner.nextInt();
                System.out.print("Write how many disposable cups of coffee do you want to add: ");
                int cupsAdd = scanner.nextInt();
                water += waterAdd;
                milk += milkAdd;
                beans += beansAdd;
                cups += cupsAdd;
                break;
            }
            case "take":{
                System.out.println("I gave you $" + money);
                money = 0;
            }
                break;
        }
        summary();
    }

    static public void summary(){
        System.out.println("The coffee machine has:");
        System.out.println(water + " of water");
        System.out.println(milk + " of milk");
        System.out.println(beans + " of coffee beans");
        System.out.println(cups + " of disposable cups");
        System.out.println(money + " of money");
        System.out.println();
        System.out.print("Write action (buy, fill, take): ");
    }
}