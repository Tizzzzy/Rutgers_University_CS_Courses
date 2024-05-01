package src.piece;

import java.util.ArrayList;

public class tesetmain {
    public static grid[][] board = new grid[8][8];

    public static void main(String[] args) {
        // initialize
        board = grid.initializeGrid();

        // initialize pawn
        for (int i = 0; i < 8; i++) { // initialize white pawn
            board[6][i].turnPawn(1);
            board[6][i].type.attackRange.add(board[6][i].pos + 16);
        }
        for (int i = 0; i < 8; i++) { // initialize black pawn
            board[1][i].turnPawn(2);
            board[1][i].type.attackRange.add(board[1][i].pos - 16);
        }
        // initialize rook
        board[0][0].turnRook(2);
        board[0][7].turnRook(2);
        board[7][0].turnRook(1);
        board[7][7].turnRook(1);
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
        board[7][4].turnKing(1);
        // initialize queen
        board[0][3].turnQueen(2);
        board[7][3].turnQueen(1);
        printBoard();

        adjustAll();
        for (int i = 0; i < intGetPos(3).type.attackRange.size(); i++) {
            System.out.println(intGetPos(3).type.attackRange.get(i));
        }
        System.out.println();
    }

    public static grid intGetPos(int a) {
        int x = 7 - a / 8;
        int y = a % 8;
        return board[x][y];
    }

    public static void printBoard() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].type.player == 0) {
                    if (grid.getColor(board[i][j])) {
                        System.out.print("   ");
                    } else {
                        System.out.print("## ");
                    }
                    // System.out.print(board[i][j].pos + " ");

                } else if (board[i][j].type.player == 1) {
                    System.out.print("w" + board[i][j].type.letter + " ");
                    // System.out.print(board[i][j].pos + " ");
                } else if (board[i][j].type.player == 2) {
                    System.out.print("b" + board[i][j].type.letter + " ");
                    // System.out.print(board[i][j].pos + " ");
                }
            }
            System.out.println(8 - i);
        }
        for (int i = 0; i < 8; i++) {
            System.out.print(String.valueOf((char) (97 + i)) + "  ");
        }
        System.out.println();
    }

    private static void adjustAll() {
        for (int i = 0; i < 64; i++) {
            rangeAdjust(i);
        }
    }

    private static void rangeAdjust(int ctr) {
        grid center = getPos(ctr);
        ArrayList<Integer> temp = center.type.attackRange;
        if (center.type.type == 1) { // king
            for (int i = 0; i < temp.size(); i++) {
                if (checkLegalMoveK(ctr, temp.get(i)) == 0)
                    i--;
            }
        } else if (center.type.type == 2) { // rook
            for (int i = 0; i < temp.size(); i++) {
                if (checkLegalMoveR(ctr, temp.get(i)) == 0)
                    i--;
            }
        } else if (center.type.type == 3) { // bishop
            for (int i = 0; i < temp.size(); i++) {
                if (checkLegalMoveB(ctr, temp.get(i)) == 0)
                    i--;
            }
        } else if (center.type.type == 4) { // queen
            for (int i = 0; i < temp.size(); i++) {
                if (checkLegalMoveQ(ctr, temp.get(i)) == 0)
                    i--;
            }
        } else if (center.type.type == 5) { // knight
            for (int i = 0; i < temp.size(); i++) {
                if (checkLegalMoveN(ctr, temp.get(i)) == 0)
                    i--;
            }
        } else if (center.type.type == 6) { // pawn
            for (int i = 0; i < temp.size(); i++) {
                if (checkLegalMoveP(ctr, temp.get(i)) == 0)
                    i--;
            }
        }

    }

    /*
     * check if the input pos is a legal attackrange
     * legal if
     * 1 there is no chess between selected chess and pos
     * 2 pos is not an ally chess
     * if pos is a enemy chess, it shouldn't be deleted
     */
    private static int checkLegalMoveR(int ctr, int pos) {
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
            if (c1 == t1) {
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

    private static int checkLegalMoveK(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);
        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));
            return 0;
        }

        return 1;
    }

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

    private static int checkLegalMoveN(int ctr, int pos) {
        grid center = getPos(ctr);
        grid target = getPos(pos);

        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));

            return 0;
        }
        return 1;
    }

    private static int checkLegalMoveP(int ctr, int pos) { // todo
        grid center = getPos(ctr);
        grid target = getPos(pos);

        if (target.type.player == center.type.player) {
            center.type.attackRange.remove(Integer.valueOf(pos));

            return 0;
        }
        return 1;
    }

    private static grid getPos(int pos) {
        int x = 7 - pos / 8;
        int y = pos % 8;
        return board[x][y];
    }
}
