package life;

import java.util.Objects;
import java.util.Random;

public class Generation {
    private Universe universe;
    private int n; // universe size; n x n
    private int s; // seed for random
    private int m; // number of generations
    private Random random;

    public Generation(Universe universe, int n, int s, int m) {
        this.universe = universe;
        this.n = n;
        this.s = s;
        this.m = m;
        this.random = new Random(s);
    }

    public char[][] generationZero() {
        char[][] universe = new char[this.n][this.n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                universe[i][j] = random.nextBoolean() ? 'O' : ' ';
            }
        }
        return universe;
    }

    public void nextGeneration() {
        //An alive cell survives if has two or three alive neighbors;
        //otherwise, it dies of boredom (<2) or overpopulation (>3)
        //A dead cell is reborn if it has exactly three alive neighbors
        char[][] currentGen = universe.getCurrentState();
        int len = currentGen.length;
        char[][] nextGen = new char[len][len];
        for (int row = 0; row < len; row++) {
            for (int col = 0; col < len; col++) {
                int neighborCount = getNeighbourCount(currentGen, row, col);
                //System.out.printf("cell [%d][%d] = %d neighbors\n", row, col, neighborCount);
                // Check if cell alive
                if (currentGen[row][col] == 'O') {
                    // Cell survives
                    if (neighborCount == 2 || neighborCount == 3) {
                        nextGen[row][col] = 'O';
                    } else {
                        // Cell dies
                        nextGen[row][col] = ' ';
                    }
                }
                // Check dead cell
                if (currentGen[row][col] == ' ') {
                    if (neighborCount == 3) {
                        // Cell is reborn
                        nextGen[row][col] = 'O';
                    } else {
                        // Cell remains dead
                        nextGen[row][col] = ' ';
                    }
                }
            }
        }
        universe.setCurrentState(nextGen);
    }

    private int getNeighbourCount(char[][] universe, int row, int col) {
        int count = 0;
        int size = universe.length;
        //System.out.printf("Checking Neighbours of [%d][%d]:\n", row, col);

        //Check N
        if (row == 0) { //Top row, check bottom row
            //System.out.printf("N1 - [%d][%d] = %b\n", size - 1,col, isAlive(universe,size - 1, col));
            if (isAlive(universe, size - 1, col)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("N2 - [%d][%d] = %b\n", row - 1, col, isAlive(universe, row - 1, col));
            if (isAlive(universe, row - 1, col)) {
                count++;
            }
        }

        //Check E
        if (col == (size - 1)) { //Right col, check left col
            //System.out.printf("E1 - [%d][%d] = %b\n", row, 0, isAlive(universe, row, 0));
            if (isAlive(universe, row, 0)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("E2 - [%d][%d] = %b\n", row, col + 1, isAlive(universe, row, col + 1));
            if (isAlive(universe, row, col + 1)) {
                count++;
            }
        }

        //Check S
        if (row == size - 1) { //Bottom row, check top row
            //System.out.printf("S1 - [%d][%d] = %b\n", 0, col, isAlive(universe, 0, col));
            if (isAlive(universe, 0, col)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("S2 - [%d][%d] = %b\n", row + 1, col, isAlive(universe, row + 1, col));
            if (isAlive(universe, row + 1, col)) {
                count++;
            }
        }

        //Check W
        if (col == 0) { //Left col, check right col
            //System.out.printf("W1 - [%d][%d] = %b\n", row, size - 1, isAlive(universe, row, size - 1));
            if (isAlive(universe, row, size - 1)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("W2 - [%d][%d] = %b\n", row, col - 1, isAlive(universe, row, col - 1));
            if (isAlive(universe, row, col - 1)) {
                count++;
            }
        }

        //Check NE
        if (row == 0 && (col == size - 1)) { //Top right corner, check bottom left corner
            //System.out.printf("NE1 - [%d][%d] = %b\n", size - 1, 0, isAlive(universe, size - 1, 0));
            if (isAlive(universe, size - 1, 0)) {
                count++;
            }
        } else if (col == size - 1) { //Right col, check left col
            //System.out.printf("NE2 - [%d][%d] = %b\n", row - 1, 0, isAlive(universe, row - 1, 0));
            if (isAlive(universe, row - 1, 0)) {
                count++;
            }
        } else if (row == 0) { //Top row, check bottom row
            //System.out.printf("NE3 - [%d][%d] = %b\n", size - 1, col + 1, isAlive(universe, size - 1, col + 1));
            if (isAlive(universe, size - 1, col + 1)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("NE4 - [%d][%d] = %b\n", row - 1, col + 1, isAlive(universe, row - 1, col + 1));
            if (isAlive(universe, row - 1, col + 1)) {
                count++;
            }
        }

        //Check SE
        if (row == size - 1 && col == size - 1) { //Bottom right corner, check top left corner
            //System.out.printf("SE1 - [%d][%d] = %b\n", 0, 0, isAlive(universe, 0, 0));
            if (isAlive(universe, 0, 0)) {
                count++;
            }
        } else if (col == size - 1) { //Right col, check left col
            //System.out.printf("SE2 - [%d][%d] = %b\n", row + 1, 0, isAlive(universe, row + 1, 0));
            if (isAlive(universe, row + 1, 0)) {
                count++;
            }
        } else if (row == size - 1) { //Bottom row, check top row
            //System.out.printf("SE3 - [%d][%d] = %b\n", 0, col + 1, isAlive(universe, 0, col + 1));
            if (isAlive(universe, 0, col + 1)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("SE4 - [%d][%d] = %b\n", row + 1, col + 1, isAlive(universe, row + 1, col + 1));
            if (isAlive(universe, row + 1, col + 1)) {
                count++;
            }
        }

        //Check SW
        if (row == size - 1 && col == 0) { //Bottom left corner, check top right corner
            //System.out.printf("SW1 - [%d][%d] = %b\n", 0, size - 1, isAlive(universe, 0, size - 1));
            if (isAlive(universe, 0, size - 1)) {
                count++;
            }
        } else if (col == 0) { //Left col, check right col
            //System.out.printf("SW2 - [%d][%d] = %b\n", row + 1, size - 1, isAlive(universe, row + 1, size - 1));
            if (isAlive(universe, row + 1, size - 1)) {
                count++;
            }
        } else if (row == size - 1) { //Bottom row, check top row
            //System.out.printf("SW3 - [%d][%d] = %b\n", 0, col - 1, isAlive(universe, 0, col - 1));
            if (isAlive(universe, 0, col - 1)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("SW4 - [%d][%d] = %b\n", row + 1, col - 1, isAlive(universe, row + 1, col - 1));
            if (isAlive(universe, row + 1, col - 1)) {
                count++;
            }
        }

        //Check NW
        if (row == 0 && col == 0) { //Top left corner, check bottom right corner
            //System.out.printf("NW1 - [%d][%d] = %b\n", size - 1, size - 1, isAlive(universe, size - 1, size - 1));
            if (isAlive(universe, size - 1, size - 1)) {
                count++;
            }
        } else if (col == 0) { //Left col, check right col
            //System.out.printf("NW2 - [%d][%d] = %b\n", row - 1, size - 1, isAlive(universe, row - 1, size - 1));
            if (isAlive(universe, row - 1, size - 1)) {
                count++;
            }
        } else if (row == 0) { //Top row, check bottom row
            //System.out.printf("NW3 - [%d][%d] = %b\n", size - 1, col - 1, isAlive(universe, size - 1, col - 1));
            if (isAlive(universe, size - 1, col - 1)) {
                count++;
            }
        } else { //Any other point
            //System.out.printf("SE4 - [%d][%d] = %b\n", row - 1, col - 1, isAlive(universe, row - 1, col - 1));
            if (isAlive(universe, row - 1, col - 1)) {
                count++;
            }
        }

        return count;
    }


    private static boolean isAlive(char[][] universe, int row, int col) {
        return Objects.equals(universe[row][col], 'O');
    }
}

