package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter cells: ");
        String initialInput = scanner.nextLine().replace("\"", "");

        char[][] board = createBoard(initialInput.replace('_', ' '));
        printBoard(board);

        boolean canMakeMove = false;
        do {
            System.out.print("Enter the coordinates: ");
            String move = scanner.nextLine();
            canMakeMove = makeMove(move, board, 'X');

        } while (!canMakeMove);

        printBoard(board);

        //System.out.println(getState(board));
    }

    private static char[][] createBoard(String input) {
        char[] in = input.toCharArray();
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

        switch (coordinates){
            case 11:
                return fillCellIfEmpty(board, 2, 0);
            case 12:
                return fillCellIfEmpty(board, 1, 0);
            case 13:
                return fillCellIfEmpty(board, 0, 0);
            case 21:
                return fillCellIfEmpty(board, 2, 1);
            case 22:
                return fillCellIfEmpty(board, 1,1);
            case 23:
                return fillCellIfEmpty(board, 0, 1);
            case 31:
                return fillCellIfEmpty(board, 2,2);
            case 32:
                return fillCellIfEmpty(board, 1,2);
            case 33:
                return fillCellIfEmpty(board, 0, 2);
            default:
                return false;
        }
    }

    private static boolean fillCellIfEmpty(char[][] board, int i1, int i2){
        if (board[i1][i2] == 'X' || board[i1][i2] == 'O'){
            System.out.println("This cell is occupied! Choose another one!");
            return false;
        }

        board[i1][i2] = 'X';
        return true;
    }

    private static String getState(char[][] board) {
        int statusCode = -1;
        String state = "";

        //Calculate number of moves for X and O
        int xMoves = 0;
        int oMoves = 0;
        for (int i = 0; i < 3; i++){
                for (int j = 0; j < 3; j++){
                    if (board[i][j] == 'X') { xMoves++; }
                    if (board[i][j] == 'O') { oMoves++; }
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

        if (!xWins && !oWins && !isFullBoard){
            statusCode = 0;
        }

        if (!xWins && !oWins && isFullBoard){
            statusCode = 1;
        }

        if (xWins && !oWins){
            statusCode = 2;
        }

        if (!xWins && oWins){
            statusCode = 3;
        }

        /*Impossible states:
        1: both X and O has 3 in a row
        2: difference between X vs O moves is 2+
        */
        if ((xWins && oWins) || (xMoves - oMoves >= 2 || oMoves - xMoves >= 2)) {
            statusCode = 4;
        }

        switch (statusCode) {
            case 0:
                state = "Game not finished";
                break;
            case 1:
                state = "Draw";
                break;
            case 2:
                state = "X wins";
                break;
            case 3:
                state = "O wins";
                break;
            case 4:
                state = "Impossible";
            default:
                state = "ERROR";
        }
        return state;
    }
}