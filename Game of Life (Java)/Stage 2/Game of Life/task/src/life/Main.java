package life;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] lineArr = line.split("\\s+");
        int n = Integer.parseInt(lineArr[0]);
        int s = Integer.parseInt(lineArr[1]);
        int m = Integer.parseInt(lineArr[2]);

        Universe universe = new Universe();
        Generation generation = new Generation(universe, n, s, m);
        universe.setCurrentState(generation.generationZero());

        for (int i = 0; i < m; i++) {
            generation.nextGeneration();
        }

        universe.showCurrentState();
    }
}
