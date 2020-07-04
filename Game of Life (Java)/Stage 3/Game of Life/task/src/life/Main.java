package life;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] lineArr = line.split("\\s+");
        int n = Integer.parseInt(lineArr[0]);

        Universe universe = new Universe();
        Generation generation = new Generation(universe, n);
        universe.setCurrentState(generation.generationZero());

        int g = 1;
        int evolutions = 10;

        while (g < evolutions){
            System.out.printf("Generation #%d\n", g++);
            System.out.printf("Alive: %d\n", generation.getAliveCount(universe.getCurrentState()));

            universe.showCurrentState();
            generation.nextGeneration();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e);
            }

            Controller.clearScreen();
        }
    }
}
