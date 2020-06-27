package life;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        String[] lineArr = line.split("\\s+");
        int n = Integer.parseInt(lineArr[0]);
        int s = Integer.parseInt(lineArr[1]);

        char[][] universe = generateUniverse(n, s);

        showUniverseState(universe);
    }

    private static char[][] generateUniverse(int n, int s){
        Random random = new Random(s);
        char[][] universe = new char[n][n];
        for (int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++){
                boolean isAlive = random.nextBoolean();
                universe[i][j]= isAlive ? 'O' : ' ';
            }
        }
        return universe;
    }

    private static void showUniverseState(char[][] universe){
        for (char[] row : universe){
            System.out.println(row);
        }
    }
}
