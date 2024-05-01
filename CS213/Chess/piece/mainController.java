package src.piece;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Xiaoyu Chen
 * @author Dong Shu
 */
public class mainController {
    public static boolean ifEnd = false;
    public static grid[][] board = new grid[8][8];
    private static boolean warn = false;
    private static int currentTurn = 1;
    private static int nextTurn = 2;
    private static int input = 0;

    public static void initializeALL() {
        // initialize
        board = grid.initializeGrid();
        for (int i = 2; i < 6; i++) { // y
            for (int j = 0; j < 8; j++) { // x
                board[i][j].type.player = 0;
            }
        }

        // initialize rook
        board[0][0].turnRook(2);
        board[0][0].type.ifMoved = false;
        board[0][7].turnRook(2);
        board[0][7].type.ifMoved = false;
        board[7][0].turnRook(1);
        board[7][0].type.ifMoved = false;
        board[7][7].turnRook(1);
        board[7][7].type.ifMoved = false;
        // initialize knight
        board[0][1].turnKnight(2);
        board[0][6].turnKnight(2);
        board[7][1].turnKnight(1);
        board[7][6].turnKnight(1);
        // initialize bishop
        board[0][2].turnBishop(2);
        board[0][5].turnBishop(2);
        board[7][2].turnBishop(1);
        board[7][5].turnBishop(1);
        // initialize king
        board[0][4].turnKing(2);
        board[0][4].type.ifMoved = false;
        board[7][4].turnKing(1);
        board[7][4].type.ifMoved = false;
        // initialize queen
        board[0][3].turnQueen(2);
        board[7][3].turnQueen(1);
        adjustAll();
        // initialize pawn
        for (int i = 0; i < 8; i++) { // initialize white pawn
            board[6][i].turnPawn(1);
            board[6][i].type.ifMoved = false;
            board[6][i].type.attackRange.add(board[6][i].pos + 16);
        }
        for (int i = 0; i < 8; i++) { // initialize black pawn
            board[1][i].turnPawn(2);
            board[1][i].type.ifMoved = false;
            board[1][i].type.attackRange.add(board[1][i].pos - 16);

        }
    }

    public static void main(String args[]) {

        initializeALL();
        printBoard();

        Scanner sc = new Scanner(System.in);

        while (!ifEnd) {
            String inputStr, a = "", b = "", c = "";
            inputStr = sc.nextLine();
            String temp[] = inputStr.split(" ");
            a = temp[0];
            if (temp.length > 1)
                b = temp[1];
            if (temp.length > 2)
                c = temp[2];

            if (a.equals("resign")) { // 4: resign
                input = 4;
            } else if (c.equals("draw?")) { // 5: draw
                input = 5;
            } else if (getPos(a).type.player != currentTurn) {
                System.out.println("Illegal move, try again");
                continue;
            } else if (checkMate(currentTurn, nextTurn) == true) {
                input = 6;
            } else if (getPos(b).type.player == 0) { // 1: the destination is empty
                input = 1;
            } else if (getPos(b).type.player == currentTurn) { // 2: the destination is occupied by ally
                input = 2;
            } else if (getPos(b).type.player == nextTurn) { // 3: the destination is occupied by enemy
                input = 3;
            } else {
                System.out.println("unrecognized input");
                continue;
            }

            switch (input) {
                case 1:
                    // King side Castle
                    if (getPos(a).getX() == 'e' && getPos(a).getY() == 1 && getPos(b).getX() == 'g'
                            && getPos(b).getY() == 1 ||
                            getPos(a).getX() == 'e' && getPos(a).getY() == 8 && getPos(b).getX() == 'g'
                                    && getPos(b).getY() == 8) {
                        if (KingSideCastle(a, b) == true && currentTurn == 1 && check(2) == false) {
                            board[7][6].turnKing(1);
                            board[7][5].turnRook(1);
                            board[7][4].clearGrid();
                            board[7][7].clearGrid();
                            System.out.println("Successful KingSide Castle");
                            adjustAll();
                            currentTurn = 2;
                            nextTurn = 1;
                            System.out.println();
                            printBoard();
                            // 1

                            break;

                        } else if (KingSideCastle(a, b) == true && currentTurn == 2 && check(1) == false) {
                            board[0][6].turnKing(2);
                            board[0][5].turnRook(2);
                            board[0][4].clearGrid();
                            board[0][7].clearGrid();
                            System.out.println("Successful KingSide Castle");
                            adjustAll();
                            currentTurn = 1;
                            nextTurn = 2;
                            System.out.println();
                            printBoard();
                            // 1

                            break;

                        } else {
                            System.out.println("Illegal move, try again");
                            continue;
                        }
                    }

                    // Queen side Castle
                    if (getPos(a).getX() == 'e' && getPos(a).getY() == 1 && getPos(b).getX() == 'c'
                            && getPos(b).getY() == 1 ||
                            getPos(a).getX() == 'e' && getPos(a).getY() == 8 && getPos(b).getX() == 'c'
                                    && getPos(b).getY() == 8) {
                        if (QueenSideCastle(a, b) == true && currentTurn == 1 && check(2) == false) {
                            board[7][2].turnKing(1);
                            board[7][3].turnRook(1);
                            board[7][0].clearGrid();
                            board[7][4].clearGrid();
                            System.out.println("Successful QueenSide Castle");
                            adjustAll();
                            currentTurn = 2;
                            nextTurn = 1;
                            System.out.println();
                            printBoard();
                            // 1

                            break;

                        } else if (QueenSideCastle(a, b) == true && currentTurn == 2 && check(1) == false) {
                            board[0][2].turnKing(2);
                            board[0][3].turnRook(2);
                            board[0][0].clearGrid();
                            board[0][4].clearGrid();
                            System.out.println("Successful QueenSide Castle");
                            // 1
                            adjustAll();
                            currentTurn = 1;
                            nextTurn = 2;
                            System.out.println();
                            printBoard();

                            break;

                        } else {
                            System.out.println("Illegal move, try again");
                            continue;
                        }
                    } else {

                        if (checkPromotion(currentTurn, a, b) == true) {
                            grid end = getPos(b);
                            int x = 7 - end.pos / 8;
                            int y = end.pos % 8;
                            grid start = getPos(a);
                            int one = 7 - start.pos / 8;
                            int two = start.pos % 8;
                            if (temp.length == 2) {
                                board[x][y].turnQueen(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals(null)) {
                                board[x][y].turnQueen(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("Q")) {
                                board[x][y].turnQueen(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("R")) {
                                board[x][y].turnRook(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("N")) {
                                board[x][y].turnKnight(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("B")) {
                                board[x][y].turnBishop(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("P")) {
                                System.out.println("Illegal move, try again");
                                continue;
                            } else {
                                System.out.println("Illegal move, try again");
                                continue;
                            }
                            if (currentTurn == 1) {
                                currentTurn = 2;
                                nextTurn = 1;
                            } else if (currentTurn == 2) {
                                currentTurn = 1;
                                nextTurn = 2;
                            }
                            // 1
                            adjustAll();
                            System.out.println();
                            printBoard();

                            break;
                        }

                        move(a, b, currentTurn);

                        if (warn == true) {
                            System.out.println("Illegal move, try again");
                            warn = false;
                            continue;
                        }
                        if (currentTurn == 1) {
                            currentTurn = 2;
                            nextTurn = 1;
                        } else if (currentTurn == 2) {
                            currentTurn = 1;
                            nextTurn = 2;
                        }
                        // 1
                        System.out.println();
                        printBoard();

                    }
                    break;

                case 2:
                    System.out.println("Illegal move, try again");
                    continue;
                case 3:

                    // enpasson
                    if (checkEnPassant(currentTurn, a, b) == true) {

                        if (checkPromotion(currentTurn, a, b) == true) {
                            grid end = getPos(b);
                            int x = 7 - end.pos / 8;
                            int y = end.pos % 8;
                            grid start = getPos(a);
                            int one = 7 - start.pos / 8;
                            int two = start.pos % 8;
                            if (temp.length == 2) {
                                board[x][y].turnQueen(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals(null)) {
                                board[x][y].turnQueen(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("Q")) {
                                board[x][y].turnQueen(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("R")) {
                                board[x][y].turnRook(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("N")) {
                                board[x][y].turnKnight(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("B")) {
                                board[x][y].turnBishop(currentTurn);
                                board[one][two].clearGrid();
                            } else if (c.equals("P")) {
                                System.out.println("Illegal move, try again");
                                continue;
                            } else {
                                System.out.println("Illegal move, try again");
                                continue;
                            }

                            if (currentTurn == 1) {
                                currentTurn = 2;
                                nextTurn = 1;
                            } else if (currentTurn == 2) {
                                currentTurn = 1;
                                nextTurn = 2;
                            }
                            // 1
                            adjustAll();
                            System.out.println();
                            printBoard();

                            break;

                        }
                        enPassant(currentTurn, a, b);

                        if (currentTurn == 1) {
                            currentTurn = 2;
                            nextTurn = 1;
                        } else if (currentTurn == 2) {
                            currentTurn = 1;
                            nextTurn = 2;
                        }
                        // 1
                        adjustAll();
                        System.out.println();
                        printBoard();

                        break;
                    }
                    if (getPos(a).type.type == 6) {
                        if (getPos(a).type.attackRangeP.contains(getPos(b).pos)) {
                            getPos(a).clearGrid();
                            getPos(b).turnPawn(currentTurn);
                            getPos(b).type.ifMoved = true;
                            adjustAll();
                            if (currentTurn == 1) {
                                currentTurn = 2;
                                nextTurn = 1;
                            } else if (currentTurn == 2) {
                                currentTurn = 1;
                                nextTurn = 2;
                            }
                            System.out.println();
                            printBoard();
                            // 1

                        } else {
                            System.out.println("Illegal move, try again");
                        }
                        break;
                    }

                    move(a, b, currentTurn);
                    if (warn == true) {
                        System.out.println("Illegal move, try again");
                        warn = false;
                        continue;
                    }
                    if (currentTurn == 1) {
                        currentTurn = 2;
                        nextTurn = 1;
                    } else if (currentTurn == 2) {
                        currentTurn = 1;
                        nextTurn = 2;
                    }
                    // 1
                    System.out.println();
                    printBoard();

                    break;
                case 4:
                    if (currentTurn == 1) {
                        System.out.println("Black wins");
                        System.exit(1);
                    } else if (currentTurn == 2) {
                        System.out.println("White wins");
                        System.exit(1);
                    } else {
                        continue;
                    }
                    break;
                case 5:
                    System.out.println("draw");
                    System.exit(1);
                    break;
                case 6:
                    System.out.println();
                    print();
                    System.out.println("\nCheckmate\n");
                    if (currentTurn == 1) {
                        System.out.println("Black wins");
                        System.exit(1);
                    } else if (currentTurn == 2) {
                        System.out.println("White wins");
                        System.exit(1);
                    }
                    System.exit(1);

            }
        }
        sc.close();

    }

    // when nomal move && enpasson both reach to the last row, promote
    /**
     * check whether a pawn is eligible for a promotion
     * 
     * @author Dong Shu
     * @param player      current player
     * @param startPawn   pawn's starting position
     * @param distination pawn's ending position
     * @return true or false
     */
    public static boolean checkPromotion(int player, String startPawn, String distination) {
        grid pawn = getPos(startPawn);
        grid end = getPos(distination);
        if (pawn.type.type == 6) {
            if (player == 1 && pawn.type.player == 1) {
                if (end.pos >= 56 && end.pos <= 63) {
                    return true;
                }
            } else if (player == 2 && pawn.type.player == 2) {
                if (end.pos >= 0 && end.pos <= 7) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * check whether the pawn is eligible for enPassant
     * 
     * @author Dong Shu
     * @param player      current player
     * @param startPawn   pawn's starting position
     * @param distination pawn's ending position
     * @return true or false
     */
    public static boolean checkEnPassant(int player, String startPawn, String distination) {
        if (player == 1) {
            grid pawn = getPos(startPawn);
            grid end = getPos(distination);
            if (pawn.type.type == 6) {
                if (grid.inFirstColumn[pawn.pos]) {
                    if (pawn.type.player == 1 && end.type.player == 2 && end.pos == pawn.pos + 9) {
                        return true;
                    }

                } else if (grid.inEighthColumn[pawn.pos]) {
                    if (pawn.type.player == 1 && end.type.player == 2 && end.pos == pawn.pos + 7) {
                        return true;
                    }

                } else {
                    if (pawn.type.player == 1 && end.type.player == 2) {
                        if (end.pos == pawn.pos + 7 || end.pos == pawn.pos + 9) {
                            return true;
                        }
                    }
                }
            }
        } else if (player == 2) {
            grid pawn = getPos(startPawn);
            grid end = getPos(distination);
            if (pawn.type.type == 6) {
                if (grid.inFirstColumn[pawn.pos]) {
                    if (pawn.type.player == 2 && end.type.player == 1 && end.pos == pawn.pos - 9) {
                        return true;
                    }

                } else if (grid.inEighthColumn[pawn.pos]) {
                    if (pawn.type.player == 2 && end.type.player == 1 && end.pos == pawn.pos - 7) {
                        return true;
                    }

                } else {
                    if (pawn.type.player == 2 && end.type.player == 1) {
                        if (end.pos == pawn.pos - 7 || end.pos == pawn.pos - 9) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * make the pawn attack the front left or front right piece
     * 
     * @author Dong Shu
     * @param player      current player
     * @param startPawn   pawn's starting position
     * @param distination pawn's ending position
     */
    public static void enPassant(int player, String startPawn, String distination) {
        if (player == 1) {
            grid pawn = getPos(startPawn);
            grid end = getPos(distination);
            if (grid.inFirstColumn[pawn.pos]) {
                if (pawn.type.player == 1 && end.type.player == 2 && end.pos == pawn.pos + 9) {
                    int one = 7 - end.pos / 8;
                    int two = end.pos % 8;
                    board[one][two].turnPawn(1);
                    int x = 7 - pawn.pos / 8;
                    int y = pawn.pos % 8;
                    board[x][y].clearGrid();
                }
            } else if (grid.inEighthColumn[pawn.pos]) {
                if (pawn.type.player == 1 && end.type.player == 2 && end.pos == pawn.pos + 7) {
                    int one = 7 - end.pos / 8;
                    int two = end.pos % 8;
                    board[one][two].turnPawn(1);
                    int x = 7 - pawn.pos / 8;
                    int y = pawn.pos % 8;
                    board[x][y].clearGrid();
                }
            } else {
                if (pawn.type.player == 1 && end.type.player == 2) {
                    if (end.pos == pawn.pos + 7 || end.pos == pawn.pos + 9) {
                        int one = 7 - end.pos / 8;
                        int two = end.pos % 8;
                        board[one][two].turnPawn(1);
                        int x = 7 - pawn.pos / 8;
                        int y = pawn.pos % 8;
                        board[x][y].clearGrid();
                    }
                }
            }
        } else if (player == 2) {
            grid pawn = getPos(startPawn);
            grid end = getPos(distination);
            if (grid.inFirstColumn[pawn.pos]) {
                if (pawn.type.player == 2 && end.type.player == 1 && end.pos == pawn.pos - 9) {
                    int one = 7 - end.pos / 8;
                    int two = end.pos % 8;
                    board[one][two].turnPawn(2);
                    int x = 7 - pawn.pos / 8;
                    int y = pawn.pos % 8;
                    board[x][y].clearGrid();
                }
            } else if (grid.inEighthColumn[pawn.pos]) {
                if (pawn.type.player == 2 && end.type.player == 1 && end.pos == pawn.pos - 7) {
                    int one = 7 - end.pos / 8;
                    int two = end.pos % 8;
                    board[one][two].turnPawn(2);
                    int x = 7 - pawn.pos / 8;
                    int y = pawn.pos % 8;
                    board[x][y].clearGrid();
                }
            } else {
                if (pawn.type.player == 2 && end.type.player == 1) {
                    if (end.pos == pawn.pos - 7 || end.pos == pawn.pos - 9) {
                        int one = 7 - end.pos / 8;
                        int two = end.pos % 8;
                        board[one][two].turnPawn(2);
                        int x = 7 - pawn.pos / 8;
                        int y = pawn.pos % 8;
                        board[x][y].clearGrid();
                    }
                }
            }
        }
    }

    /**
     * convert a piece position to grid[][]
     * eg. "a8" --> board[0][0]
     * 
     * @author Dong Shu
     * @param a piece position
     * @return return a grid piece
     */
    public static grid getPos(String a) {
        int x = (int) a.charAt(0) - 97;
        int y = 8 - Integer.valueOf(String.valueOf(a.charAt(1)));
        return board[y][x];
    }

    /**
     * convert a piece position to grid[][]
     * eg. "56" --> board[0][0]
     * 
     * @author Dong Shu
     * @param a piece position
     * @return return a grid piece
     */
    public static grid intGetPos(int a) {
        int x = 7 - a / 8;
        int y = a % 8;
        return board[x][y];
    }

    /**
     * pass in a piece position and convert into a piece
     * check the place that this piece can go(including the first enemy position)
     * 
     * @author Dong Shu
     * @param a piece position
     * @return return a set of destination that are empty
     */
    public static ArrayList<Integer> enemyOccupiedGrid(String a) {
        ArrayList<Integer> enemyOccupied = new ArrayList<>();
        grid piece = getPos(a);
        for (int move : piece.type.attackRange) {
            int Grid = piece.pos + move;
            if (Grid >= 0 && Grid < 64) {
                int x = 7 - Grid / 8;
                int y = Grid % 8;
                if (board[x][y].type.player == 0) {
                    enemyOccupied.add(move);
                } else if (board[x][y].type.player != piece.type.player) {
                    enemyOccupied.add(move);
                    break;
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return enemyOccupied;
    }

    /**
     * pass in a piece position and convert into a piece
     * check the place that this piece can go
     * 
     * @author Dong Shu
     * @param a piece position
     * @return return a set of destination that are empty
     */
    public static ArrayList<Integer> nonOccupiedGrid(String a) {
        ArrayList<Integer> nonOccupied = new ArrayList<>();
        grid piece = getPos(a);
        for (int move : piece.type.attackRange) {
            int Grid = piece.pos + move;
            if (Grid >= 0 && Grid < 64) {
                int x = 7 - Grid / 8;
                int y = Grid % 8;
                if (board[x][y].type.player == 0) {
                    nonOccupied.add(move);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
        return nonOccupied;
    }

    /**
     * check if the king is eligible for king side castle
     * 
     * @author Dong Shu
     * @param a king's position
     * @param b end position
     * @return return true or false
     */
    public static boolean KingSideCastle(String a, String b) {
        grid kingPiece = getPos(a);
        grid emptyPiece = getPos(b);
        grid kingTemp1 = intGetPos(kingPiece.pos + 1);
        // grid kingTemp2 = intGetPos(kingPiece.pos + 2);
        if (kingPiece.type.player == 1 && emptyPiece.type.player == 0) { // if the player is white go through this
                                                                         // condition
            if (kingPiece.type.ifMoved == false) {
                if (checkB(kingPiece.pos) == false && checkB(kingPiece.pos + 1) == false
                        && checkB(emptyPiece.pos) == false) {
                    if (kingTemp1.type.player == 0) {
                        return true;
                    }
                }
            }
        } else if (kingPiece.type.player == 2 && emptyPiece.type.player == 0) { // if the player is black go through
                                                                                // this condition
            if (kingPiece.type.ifMoved == false) {
                if (checkW(kingPiece.pos) == false && checkW(kingPiece.pos + 1) == false
                        && checkW(emptyPiece.pos) == false) {
                    if (kingTemp1.type.player == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * check if the king is eligible for queen side castle
     * 
     * @author Dong Shu
     * @param a king's position
     * @param b end position
     * @return return true or false
     */
    public static boolean QueenSideCastle(String a, String b) {

        grid kingPiece = getPos(a);
        grid emptyPiece = getPos(b); // same position as kingTemp2
        grid kingTemp1 = intGetPos(kingPiece.pos - 1);
        grid kingTemp3 = intGetPos(emptyPiece.pos - 1);
        if (kingPiece.type.player == 1 && emptyPiece.type.player == 0) { // if the player is white go through this
                                                                         // condition
            if (kingPiece.type.ifMoved == false) {
                if (checkB(kingPiece.pos) == false && checkB(kingPiece.pos - 1) == false
                        && checkB(emptyPiece.pos) == false) {
                    if (kingTemp1.type.player == 0 && kingTemp3.type.player == 0) {
                        return true;
                    }
                }
            }
        } else if (kingPiece.type.player == 2 && emptyPiece.type.player == 0) { // if the player is black go through
                                                                                // this condition
            if (kingPiece.type.ifMoved == false) {
                if (checkW(kingPiece.pos) == false && checkW(kingPiece.pos - 1) == false
                        && checkW(emptyPiece.pos) == false) {
                    if (kingTemp1.type.player == 0 && kingTemp3.type.player == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * print the whole 8*8 grid
     * 
     * @author Xiaoyu Chen
     */
    public static void print() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.player == 0) {
                    if (grid.getColor(board[i][j])) {
                        System.out.print("   ");
                    } else {
                        System.out.print("## ");
                    }

                } else if (board[i][j].type.player == 1) {
                    System.out.print("w" + board[i][j].type.letter + " ");
                } else if (board[i][j].type.player == 2) {
                    System.out.print("b" + board[i][j].type.letter + " ");
                }
            }
            System.out.println(8 - i);
        }
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + String.valueOf((char) (97 + i)) + " ");
        }
        System.out.println("\n");
    }

    /**
     * print the whole 8*8 grid, and then print who moves next, detect if is in
     * black check of white check
     * 
     * @author Xiaoyu Chen
     */
    public static void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.player == 0) {
                    if (grid.getColor(board[i][j])) {
                        System.out.print("   ");
                    } else {
                        System.out.print("## ");
                    }

                } else if (board[i][j].type.player == 1) {
                    System.out.print("w" + board[i][j].type.letter + " ");
                } else if (board[i][j].type.player == 2) {
                    System.out.print("b" + board[i][j].type.letter + " ");
                }
            }
            System.out.println(8 - i);
        }
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + String.valueOf((char) (97 + i)) + " ");
        }
        System.out.println("\n");
        if (currentTurn == 2) {
            if (checkBking()) {
                System.out.println("Check\n");
            }
            System.out.print("Black's move: ");
        } else if (currentTurn == 1) {
            if (checkWking()) {
                System.out.println("Check\n");
            }
            System.out.print("White's move: ");
        }
    }

    /**
     * @author Xiaoyu Chen
     * @param a      the start postion
     * @param b      the destination postion
     * @param player white or black player
     */
    public static void move(String a, String b, int player) {
        int x1 = (int) a.charAt(0) - 97;
        int y1 = 8 - Integer.valueOf(String.valueOf(a.charAt(1)));

        int x2 = (int) b.charAt(0) - 97;
        int y2 = 8 - Integer.valueOf(String.valueOf(b.charAt(1)));

        int type = board[y1][x1].type.type;

        boolean ifInRange = board[y1][x1].type.attackRange.contains(board[y2][x2].pos);
        if (ifInRange) {
            board[y1][x1].clearGrid();
            if (type == 1) {
                board[y2][x2].turnKing(player);
            } else if (type == 2) {
                board[y2][x2].turnRook(player);
            } else if (type == 3) {
                board[y2][x2].turnBishop(player);
            } else if (type == 4) {
                board[y2][x2].turnQueen(player);
            } else if (type == 5) {
                board[y2][x2].turnKnight(player);
            } else if (type == 6) {
                board[y2][x2].turnPawn(player);
            }
            board[y2][x2].type.ifMoved = true;
        } else {
            warn = true;
            // System.out.println("illegal move");
        }
        adjustAll();
        // if (checkB() == true) {
        // input = 6;
        // }
        // if (checkW() == true) {
        // input = 7;
        // }

    }

    /**
     * probaboly won't use this method
     * 
     * @author Dong Shu
     * @param a      position of the starting piece
     * @param b      position of the ending piece
     * @param player the current player
     */
    public static void legalmove(String a, String b, int player) {
        grid piece1 = getPos(a);
        grid piece2 = getPos(b);

        int type = piece1.type.type;

        boolean ifInRange = enemyOccupiedGrid(a).contains(piece2.pos);
        if (ifInRange) {
            piece1.clearGrid();
            if (type == 1) {
                piece2.turnKing(player);
            } else if (type == 2) {
                piece2.turnRook(player);
            } else if (type == 3) {
                piece2.turnBishop(player);
            } else if (type == 4) {
                piece2.turnQueen(player);
            } else if (type == 5) {
                piece2.turnKnight(player);
            } else if (type == 6) {
                piece2.turnPawn(player);
            }
            piece2.type.ifMoved = true;
        } else {
            warn = true;
        }
    }

    /**
     * get the grid position
     * check its player on the grid
     * 
     * @author Dong Shu
     * @param a a number from 0 - 63
     * @return return the player(1 or 2 or 0)
     */
    public static int getPlayer(int a) {
        int y = a / 8 + 1;
        int x = a % 8 + 97;
        return board[x][y].type.player;
    }

    /**
     * get the position of a piece
     * convert to a piece and check its legal move
     * 
     * @author Dong Shu
     * @param a a number from 0 - 63
     * @return a set of moves
     */
    public static ArrayList<Integer> legalMoves(int a) {
        ArrayList<Integer> moves = new ArrayList<Integer>();
        int y = a / 8 + 1;
        int x = a % 8 + 97;
        for (int move : board[x][y].type.attackRange) {
            moves.add(move);
        }
        return moves;
    }

    /**
     * check if the white piece can attack the postion that passed in
     * 
     * @author Dong Shu
     * @param pos a number from 0 - 63
     * @return return true or false
     */
    public static boolean checkW(int pos) {
        ArrayList<Integer> whiteRange = new ArrayList<Integer>(); // a set of attack ranges of all white chesses
        // int wKing;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.player == 1) {
                    String piece = grid.convertBack(board[i][j].pos);
                    whiteRange.addAll(enemyOccupiedGrid(piece));
                }
            }
        }
        // wKing = getPosOfKing(1);
        if (whiteRange.contains(pos)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * check if the white piece can check black king
     * 
     * @author Dong Shu
     * @return return true or false
     */
    public static boolean checkBking() {
        ArrayList<Integer> whiteRange = new ArrayList<Integer>(); // a set of attack ranges of all white chesses
        int bKing;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.player == 1) {
                    whiteRange.addAll(board[i][j].type.attackRange);
                }
            }
        }
        bKing = getPosOfKing(2);
        if (whiteRange.contains(bKing)) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * check if the black piece can attack the postion that passed in
     * 
     * @author Dong Shu
     * @param pos a number from 0 - 63
     * @return return true or false
     */
    public static boolean checkB(int pos) {
        ArrayList<Integer> blackRange = new ArrayList<Integer>(); // a set of attack ranges of all black chesses
        // int bKing;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.player == 2) {
                    String piece = grid.convertBack(board[i][j].pos);
                    blackRange.addAll(enemyOccupiedGrid(piece));
                }
            }
        }
        // bKing = getPosOfKing(2);
        if (blackRange.contains(pos)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * check if the black piece can check white king
     * 
     * @author Dong Shu
     * @return return true or false
     */
    public static boolean checkWking() {
        ArrayList<Integer> blackRange = new ArrayList<Integer>(); // a set of attack ranges of all black chesses
        int wKing;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.player == 2) {
                    blackRange.addAll(board[i][j].type.attackRange);
                }
            }
        }
        wKing = getPosOfKing(1);
        if (blackRange.contains(wKing)) {
            return true;
        } else {
            return false;
        }
    }

    // TODO
    /**
     * check if the current player can make next player in checkmate
     * 
     * @author Dong Shu
     * @param currentPlayer whiteplayer or blackplayer
     * @param nextPlayer    whiteplayer or blackplayer
     * @return return true or false
     */
    public static boolean checkMate(int currentPlayer, int nextPlayer) {
        if (check(currentPlayer) == true && escapeMove(nextPlayer) == false) {
            // && blockMove(nextPlayer) == false) {
            return true;
        }
        return false;
    }

    /**
     * check if the current player can make next player in check
     * 
     * @author Dong Shu
     * @param player whiteplayer or blackplayer
     * @return return true or false
     */
    public static boolean check(int player) {
        if (player == 1) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].type.player == 1) {
                        if (board[i][i].type.attackRange.contains(getPosOfKing(2))) { // check if white piece can attack
                                                                                      // black king
                            return true;
                        }
                    }
                }
            }
        } else if (player == 2) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].type.player == 2) {
                        if (board[i][i].type.attackRange.contains(getPosOfKing(1))) { // check if black piece can attack
                                                                                      // white king
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    // TODO
    /**
     * if current player's move can make next player in check,
     * then we want to know which piece is currently checking enemy's king
     * 
     * @author Dong Shu
     * @param player whiteplayer or blackplayer
     * @return return a set of pieces that can check enemy's king
     */
    public static ArrayList<grid> CheckPiece(int player) {
        ArrayList<grid> pieces = new ArrayList<>();
        if (player == 1) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].type.player == 1) {
                        if (board[i][i].type.attackRange.contains(getPosOfKing(2))) { // check if white piece can attack
                                                                                      // black king
                            pieces.add(board[i][j]);
                        }
                    }
                }
            }
        } else if (player == 2) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].type.player == 2) {
                        if (board[i][i].type.attackRange.contains(getPosOfKing(1))) { // check if black piece can attack
                                                                                      // white king
                            pieces.add(board[i][j]);
                        }
                    }
                }
            }
        }
        return pieces;
    }

    // TODO check if the king can escape from check
    /**
     * check if whether the next player's king have the escape move from current
     * player's check
     * 
     * @author Dong Shu
     * @param nextPlayer whiteplayer or blackplayer
     * @return return true or false
     */
    public static boolean escapeMove(int nextPlayer) {
        ArrayList<Integer> moves = new ArrayList<>();
        ArrayList<Integer> check = new ArrayList<>();
        if (nextPlayer == 1) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].type.type == 1 && board[i][j].type.player == 1) {
                        moves.addAll(board[i][j].type.attackRange);
                    }
                }
            }
            for (int move : moves) {
                if (checkB(move) == true) { // if white king don't have escape move, add 1
                    check.add(1);
                } else if (checkB(move) == false) { // if white king still have escape move, add 0
                    check.add(0);
                }
            }
            if (check.contains(0)) {
                return true;
            }
        } else if (nextPlayer == 2) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].type.type == 1 && board[i][j].type.player == 2) {
                        moves.addAll(board[i][j].type.attackRange);
                    }
                }
            }
            for (int move : moves) {
                if (checkW(move) == true) {
                    check.add(1);
                } else if (checkW(move) == false) {
                    check.add(0);
                }
            }
            if (check.contains(0)) {
                return true;
            }
        }
        return false;
    }

    // TODO piece can block the check enemy
    /**
     * check if whether the next player can block the current player's check piece
     * 
     * @author Dong Shu
     * @param nextPlayer whiteplayer or blackplayer
     * @return return true or false
     */
    public static boolean blockMove(int nextPlayer) {
        ArrayList<grid> checkPiece = new ArrayList<>();
        if (nextPlayer == 2) {
            checkPiece.addAll(CheckPiece(1)); // white pieces that can check black king
            for (grid piece : checkPiece) {
                for (int move : piece.type.attackRange) {
                    if (checkB(move) == true || checkB(piece.pos) == true) { // check if black piece can block or attack
                                                                             // the white check piece
                        return true;
                    }
                }
            }
        } else if (nextPlayer == 1) {
            checkPiece.addAll(CheckPiece(2)); // black pieces that can check white king
            for (grid piece : checkPiece) {
                for (int move : piece.type.attackRange) {
                    if (checkW(move) == true || checkW(piece.pos) == true) { // check if white piece can block or attack
                                                                             // the black check piece
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 
     * @author Xiaoyu Chen
     * @param player whiteplayer or blackplayer
     * @return return the position of king of the slected player
     */
    public static int getPosOfKing(int player) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.type == 1 && board[i][j].type.player == player) {
                    return grid.convert(board[i][j]);
                }
            }
        }
        return -1;
    }

    /**
     * probably won't use this method
     * 
     * @author Xiaoyu Chen
     */
    public void checkSet() { // a set of operations that need to be checked after every move
        if (currentTurn == 1) {
            checkB(getPosOfKing(1));
        } else if (currentTurn == 2) {
            checkW(getPosOfKing(2));
        }
    }

    /**
     * adjust all grids fomr 0 to 63
     * 
     * @author Xiaoyu Chen
     */
    private static void adjustAll() {
        for (int i = 0; i < 64; i++) {
            rangeAdjust(i);
        }
    }

    /**
     * @author Xiaoyu Chen
     * @param ctr a single chess which has range that needs to be adjusted
     */

    private static void rangeAdjust(int ctr) {
        grid center = getPos(ctr);
        ArrayList<Integer> temp = center.type.attackRange;
        if (center.type.type == 0) {
            return;
        } else if (center.type.type == 1) { // king
            // boolean bool = true;
            // if (center.type.ifMoved == false)
            // bool = false;
            center.turnKing(center.type.player);
            // center.type.ifMoved = bool;
            for (int i = 0; i < temp.size(); i++) {
                checkLegalMoveK(ctr, temp.get(i));
            }

        } else if (center.type.type == 2) { // rook
            // boolean bool = true;
            // if (center.type.ifMoved == false)
            // bool = false;
            center.turnRook(center.type.player);
            // center.type.ifMoved = bool;
            for (int i = 0; i < temp.size(); i++) {
                checkLegalMoveR(ctr, temp.get(i));
            }

        } else if (center.type.type == 3) { // bishop
            center.turnBishop(center.type.player);
            for (int i = 0; i < temp.size(); i++) {
                checkLegalMoveB(ctr, temp.get(i));
            }
        } else if (center.type.type == 4) { // queen
            center.turnQueen(center.type.player);
            for (int i = 0; i < temp.size(); i++) {
                checkLegalMoveQ(ctr, temp.get(i));
            }
        } else if (center.type.type == 5) { // knight
            center.turnKnight(center.type.player);
            for (int i = 0; i < temp.size(); i++) {
                checkLegalMoveN(ctr, temp.get(i));
            }
        } else if (center.type.type == 6) { // pawn
            ArrayList<Integer> temp2 = center.type.attackRangeP;
            boolean bool = true;
            if (center.type.ifMoved == false) {
                bool = false;
            }
            if (center.type.ifMoved == true) {
                center.turnPawn(center.type.player);
                center.type.ifMoved = true;
            }

            center.type.ifMoved = bool;
            for (int i = 0; i < temp.size(); i++) {
                checkLegalMoveP1(ctr, temp.get(i));
            }
            for (int i = 0; i < temp2.size(); i++) {
                checkLegalMoveP2(ctr, temp2.get(i));
            }
        }
    }

    /**
     * check if the input pos is a legal attackrange
     * legal if
     * 1 there is no chess between selected chess and pos
     * 2 pos is not an ally chess
     * if pos is a enemy chess, it shouldn't be deleted
     * 
     * @author Xiaoyu Chen
     * @param ctr selected chess
     * @param pos one element in the attackrange, passed to be checked
     * @return not important, just in case
     */
    private static void checkLegalMoveR(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);

        int c1, c2, t1, t2;
        c1 = center.x - 97;
        c2 = 8 - center.y;
        t1 = target.x - 97;
        t2 = 8 - target.y;

        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));
        } else {
            if (c1 == t1) {
                if (c2 > t2) {
                    for (int i = 1; i < c2 - t2; i++) {
                        if (board[c2 - i][c1].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                        }
                    }
                } else if (c2 < t2) {
                    for (int i = 1; i < t2 - c2; i++) {
                        if (board[c2 + i][c1].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                        }
                    }
                }
            } else if (c2 == t2) {
                if (c1 > t1) {
                    for (int i = 1; i < c1 - t1; i++) {
                        if (board[c2][c1 - i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                        }
                    }
                } else if (c1 < t1) {
                    for (int i = 1; i < t1 - c1; i++) {
                        if (board[c2][c1 + i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                        }
                    }
                }
            }
        }
    }

    /**
     * @author Xiaoyu Chen
     * @param ctr selected chess
     * @param pos one element in the attackrange, passed to be checked
     * @return not important, just in case
     */
    private static int checkLegalMoveK(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);

        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));
            return 0;
        }

        return 1;
    }

    /**
     * @author Xiaoyu Chen
     * @param ctr selected chess
     * @param pos one element in the attackrange, passed to be checked
     * @return not important, just in case
     */
    private static int checkLegalMoveB(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);

        int c1, c2, t1, t2;
        c1 = center.x - 97;
        c2 = 8 - center.y;
        t1 = target.x - 97;
        t2 = 8 - target.y;

        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));

            return 0;

        } else {
            if (c1 > t1) { // target is left
                if (c2 > t2) { // target is upleft
                    for (int i = 1; i < c2 - t2; i++) {
                        if (board[c2 - i][c1 - i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                } else if (c2 < t2) { // target is downleft
                    for (int i = 1; i < t2 - c2; i++) {
                        if (board[c2 + i][c1 - i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                }
            } else if (c1 < t1) { // target is right
                if (c2 > t2) { // target is upright
                    for (int i = 1; i < c2 - t2; i++) {
                        if (board[c2 - i][c1 + i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                } else if (c2 < t2) { // target is downright
                    for (int i = 1; i < t2 - c2; i++) {
                        if (board[c2 + i][c1 + i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                }
            }
        }
        return 1;
    }

    /**
     * @author Xiaoyu Chen
     * @param ctr selected chess
     * @param pos one element in the attackrange, passed to be checked
     * @return not important, just in case
     */
    private static int checkLegalMoveQ(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);

        int c1, c2, t1, t2;
        c1 = center.x - 97;
        c2 = 8 - center.y;
        t1 = target.x - 97;
        t2 = 8 - target.y;

        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));

            return 0;

        } else {
            if (c1 > t1) { // target is left
                if (c2 > t2) { // target is upleft
                    for (int i = 1; i < c2 - t2; i++) {
                        if (board[c2 - i][c1 - i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                } else if (c2 < t2) { // target is downleft
                    for (int i = 1; i < t2 - c2; i++) {
                        if (board[c2 + i][c1 - i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                }
            } else if (c1 < t1) { // target is right
                if (c2 > t2) { // target is upright
                    for (int i = 1; i < c2 - t2; i++) {
                        if (board[c2 - i][c1 + i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                } else if (c2 < t2) { // target is downright
                    for (int i = 1; i < t2 - c2; i++) {
                        if (board[c2 + i][c1 + i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                }

            } else if (c1 == t1) {
                if (c2 > t2) {
                    for (int i = 1; i < c2 - t2; i++) {
                        if (board[c2 - i][c1].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                } else if (c2 < t2) {
                    for (int i = 1; i < t2 - c2; i++) {
                        if (board[c2 + i][c1].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                }
            } else if (c2 == t2) {
                if (c1 > t1) {
                    for (int i = 1; i < c1 - t1; i++) {
                        if (board[c2][c1 - i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                } else if (c1 < t1) {
                    for (int i = 1; i < t1 - c1; i++) {
                        if (board[c2][c1 + i].type.player != 0) {
                            center.type.attackRange.remove(Integer.valueOf(pos));
                            return 0;
                        }
                    }
                }
            }
        }
        return 1;
    }

    /**
     * @author Xiaoyu Chen
     * @param ctr selected chess
     * @param pos one element in the attackrange, passed to be checked
     * @return not important, just in case
     */
    private static int checkLegalMoveN(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);
        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));

            return 0;
        }
        return 1;
    }

    /**
     * @author Xiaoyu Chen
     * @param ctr selected chess
     * @param pos one element in the attackrange, passed to be checked
     * @return not important, just in case
     */
    private static int checkLegalMoveP1(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);

        if (target.type.player == center.type.player) {

            center.type.attackRange.remove(Integer.valueOf(pos));

            return 0;

        }

        return 1;
    }

    /**
     * @author Xiaoyu Chen
     * @param ctr selected chess
     * @param pos one element in the attackrange, passed to be checked
     * @return not important, just in case
     */
    private static int checkLegalMoveP2(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);

        if (target.type.player == center.type.player) {

            center.type.attackRangeP.remove(Integer.valueOf(pos));

            return 0;

        }

        return 1;
    }

    /**
     * @author Xiaoyu Chen
     * @param pos int, 0 - 63
     * @return a corresponding grid in the 8*8 matrix
     */
    private static grid getPos(int pos) {
        int x = 7 - pos / 8;
        int y = pos % 8;
        return board[x][y];
    }

}