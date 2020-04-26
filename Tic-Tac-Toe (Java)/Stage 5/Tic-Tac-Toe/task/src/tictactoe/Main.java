package tictactoe;

import java.util.Arrays;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        char[][] board = createBoard();
        printBoard(board);

        Player player = new Player('U');
        boolean canMakeMove = false;
        boolean exit = false;
        String gameState = getState(board);

        do {
            player.changePlayer();
            do {
                char cPlayer = player.getSymbol();
                System.out.println(cPlayer);
                System.out.print("Enter the coordinates: ");
                String move = scanner.nextLine();

                canMakeMove = makeMove(move, board, cPlayer);

            } while (!canMakeMove);
            printBoard(board);
            gameState = getState(board);

            if(!gameState.equals("Game not finished")){
                exit = true;
            }
        } while (!exit);
        System.out.println(gameState);
    }

    private static char[][] createBoard() {
        char[] in = new char[9];
        Arrays.fill(in, ' ');
        return new char[][]{new char[]{in[0], in[1], in[2]}
                , new char[]{in[3], in[4], in[5]}
                , new char[]{in[6], in[7], in[8]}};
    }

    private static void printBoard(char[][] board) {
        System.out.println("---------");
        for (char[] pos : board) {
            for (int i = 0; i < pos.length; i++) {
                if (i == 0) {
                    System.out.print("| ");
                    System.out.print(pos[i] + " ");
                } else if (i == pos.length - 1) {
                    System.out.print(pos[i]);
                    System.out.print(" |");
                } else {
                    System.out.print(pos[i] + " ");
                }
            }
            System.out.println();
        }
        System.out.println("---------");
    }

    private static boolean makeMove(String move, char[][] board, char symbol) {
        if (!move.matches("\\d\\s\\d")) {
            System.out.println("You should only use numbers!");
            return false; //move doesn't match required input
        }

        move = move.replaceAll("\\s", "");

        for (char c : move.toCharArray()) {
            int n = Character.getNumericValue(c);
            if (n > 3 || n < 1) {
                System.out.println("Coordinates should be from 1 to 3");
                return false; // one int is less than 1 or more than 3
            }
        }

        int coordinates = Integer.parseInt(move);

        switch (coordinates) {
            case 11:
                return fillCellIfEmpty(board, 2, 0, symbol);
            case 12:
                return fillCellIfEmpty(board, 1, 0 , symbol);
            case 13:
                return fillCellIfEmpty(board, 0, 0, symbol);
            case 21:
                return fillCellIfEmpty(board, 2, 1, symbol);
            case 22:
                return fillCellIfEmpty(board, 1, 1, symbol);
            case 23:
                return fillCellIfEmpty(board, 0, 1, symbol);
            case 31:
                return fillCellIfEmpty(board, 2, 2, symbol);
            case 32:
                return fillCellIfEmpty(board, 1, 2, symbol);
            case 33:
                return fillCellIfEmpty(board, 0, 2, symbol);
            default:
                return false;
        }
    }

    private static boolean fillCellIfEmpty(char[][] board, int i1, int i2, char symbol) {
        if (board[i1][i2] == 'X' || board[i1][i2] == 'O') {
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }

        board[i1][i2] = symbol;
        return true;
    }

    private static String getState(char[][] board) {
        int statusCode = 0;
        String state = "";

        //Calculate number of moves for X and O
        int xMoves = 0;
        int oMoves = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 'X') {
                    xMoves++;
                }
                if (board[i][j] == 'O') {
                    oMoves++;
                }
            }
        }

        boolean isFullBoard = (xMoves + oMoves) == 9;

        //Calculate if X or O wins
        boolean xWins = false;
        boolean oWins = false;

        //Loop through all lines
        for (int a = 0; a < 8; a++) {
            String line = null;
            switch (a) {
                case 0:
                    line = "" + board[0][0] + board[0][1] + board[0][2];
                    //System.out.println("TopRow: " + line);
                    break;
                case 1:
                    line = "" + board[1][0] + board[1][1] + board[1][2];
                    //System.out.println("MidRow: " + line);
                    break;
                case 2:
                    line = "" + board[2][0] + board[2][1] + board[2][2];
                    //System.out.println("BtmRow: " + line);
                    break;
                case 3:
                    line = "" + board[0][0] + board[1][0] + board[2][0];
                    //System.out.println("LftCol: " + line);
                    break;
                case 4:
                    line = "" + board[0][1] + board[1][1] + board[2][1];
                    //System.out.println("MidCol: " + line);
                    break;
                case 5:
                    line = "" + board[0][2] + board[1][2] + board[2][2];
                    //System.out.println("RgtCol: " + line);
                    break;
                case 6:
                    line = "" + board[0][0] + board[1][1] + board[2][2];
                    //System.out.println("TLtoBR: " + line);
                    break;
                case 7:
                    line = "" + board[0][2] + board[1][1] + board[2][0];
                    //System.out.println("TRtoBL: " + line);
                    break;
            }
            if (line.equals("XXX")) {
                xWins = true;
            }
            if (line.equals("OOO")) {
                oWins = true;
            }
        }


        if (!xWins && !oWins && !isFullBoard) {
            return "Game not finished";
        }



        if (!xWins && !oWins && isFullBoard) {
            return "Draw";
        }

        if (xWins && !oWins) {
            return "X wins";
        }

        if (!xWins && oWins) {
            return "O wins";
        }

        /*Impossible states:
        1: both X and O has 3 in a row
        2: difference between X vs O moves is 2+
        */
        if ((xWins && oWins) || (xMoves - oMoves >= 2 || oMoves - xMoves >= 2)) {
            return "Impossible";
        }

        return state;
    }


}

class Player {

    char symbol;

    public Player(char symbol) {
        this.symbol = symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public void changePlayer(){
        switch (this.symbol){
            case 'U':
            case 'O':
                this.setSymbol('X');
                break;
            case 'X':
                this.setSymbol('O');
                break;
        }
    }

    public char getSymbol() {
        return symbol;
    }
}