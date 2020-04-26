package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().replace("\"", "");
        char[] board = input.toCharArray();

        System.out.println("---------");
        System.out.println("|" + " " + board[0] + " " + board[1] + " " + board[2] + " " + "|");
        System.out.println("|" + " " + board[3] + " " + board[4] + " " + board[5] + " " + "|");
        System.out.println("|" + " " + board[6] + " " + board[7] + " " + board[8] + " " + "|");
        System.out.println("---------");

        int state = -1;

        //Calculate number of moves for X and O
        int xMoves = 0;
        int oMoves = 0;
        for (char cell : board) {
            if (cell == 'X') { xMoves++; }
            if (cell == 'O') { oMoves++; }
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
                    line = "" + board[0] + board[1] + board[2];
                    //System.out.println("TopRow: " + line);
                    break;
                case 1:
                    line = "" + board[3] + board[4] + board[5];
                    //System.out.println("MidRow: " + line);
                    break;
                case 2:
                    line = "" + board[6] + board[7] + board[8];
                    //System.out.println("BtmRow: " + line);
                    break;
                case 3:
                    line = "" + board[0] + board[3] + board[6];
                    //System.out.println("LftCol: " + line);
                    break;
                case 4:
                    line = "" + board[1] + board[4] + board[7];
                    //System.out.println("MidCol: " + line);
                    break;
                case 5:
                    line = "" + board[2] + board[5] + board[8];
                    //System.out.println("RgtCol: " + line);
                    break;
                case 6:
                    line = "" + board[0] + board[4] + board[8];
                    //System.out.println("TLtoBR: " + line);
                    break;
                case 7:
                    line = "" + board[2] + board[4] + board[6];
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
            state = 0;
        }

        if (!xWins && !oWins && isFullBoard){
            state = 1;
        }

        if (xWins && !oWins){
            state = 2;
        }

        if (!xWins && oWins){
            state = 3;
        }

        /*Impossible states:
        1: both X and O has 3 in a row
        2: difference between X vs O moves is 2+
        */
        if ((xWins && oWins) || (xMoves - oMoves >= 2 || oMoves - xMoves >= 2)) {
            state = 4;
        }

        //System.out.println("xMoves:      " + xMoves);
        //System.out.println("oMoves:      " + oMoves);
        //System.out.println("isFullBoard: " + isFullBoard);
        //System.out.println("xWins:       " + xWins);
        //System.out.println("oWins:       " + oWins);

        switch (state){
            case 0:
                System.out.println("Game not finished");
                break;
            case 1:
                System.out.println("Draw");
                break;
            case 2:
                System.out.println("X wins");
                break;
            case 3:
                System.out.println("O wins");
                break;
            case 4:
                System.out.println("Impossible");
        }


    }
}