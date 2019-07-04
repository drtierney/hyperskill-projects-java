package bot;

import java.util.Scanner;

public class SimpleBot {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Hello! My name is Delta.");
        System.out.println("I was created in 2019.");
        System.out.println("Please tell me your name.");

        // reading a name
        String yourName = scanner.nextLine();

        System.out.printf("What a great name you have, %s!%n", yourName);
        System.out.println("Let me guess your age.");
        System.out.println("Enter remainders of dividing your age by 3, 5 and 7.");

        // reading all remainders
        int divAgeBy3 = scanner.nextInt();
        int divAgeBy5 = scanner.nextInt();
        int divAgeBy7 = scanner.nextInt();
        int yourAge = ((divAgeBy3 % 3) * 70 + (divAgeBy5 % 5) * 21 + (divAgeBy7 % 7) * 15) % 105;

        System.out.println("Your age is " + yourAge + "; that's a good time to start programming!");

        System.out.println("Now I will prove to you that I can count to any number you want.");
        int num = scanner.nextInt();

        for (int i = 0; i < num+1; i++){
            System.out.println(i + "!");
        }
        System.out.println("Completed, have a nice day!");
    }
}
