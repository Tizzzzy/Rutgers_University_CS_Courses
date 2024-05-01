package src.piece;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Xiaoyu Chen
 * @author Dong Shu
 */
public class grid {
    public char x; // xpos of the grid
    public int y; // ypos of the grid
    public int pos;
    private boolean isWhite; // True if the grid is a white grid
    public chessType type; //

    public grid(char x, int y, chessType type) {
        this.x = x;
        this.y = y;
        this.pos = 8 * (this.y - 1) + ((int) this.x - 97);
        this.type = type;
        if (((int) x + y) % 2 != 0) {
            this.isWhite = true;
        } else {
            this.isWhite = false;
        }
    }

    /**
     * use this in main
     * display:
     * # # # # # # # # 1
     * # # # # # # # # 2
     * # # # # # # # # 3
     * # # # # # # # # 4
     * # # # # # # # # 5
     * # # # # # # # # 6
     * # # # # # # # # 7
     * # # # # # # # # 8
     * a b c d e f g h
     * 
     * @author Xiaoyu Chen
     * @return a 8*8 matrix of grid, each with a corresponding pos, x, y and
     *         chessType 0. first two rows assigned to black player and last two
     *         rows assigned to white player
     */

    public static grid[][] initializeGrid() {
        grid[][] board = new grid[8][8];

        for (int i = 0; i < 8; i++) { // y
            for (int j = 0; j < 8; j++) { // x
                chessType newChess = new chessType();
                newChess.player = 0;

                if (i == 0 || i == 1) {
                    newChess.player = 2;
                }
                if (i == 6 || i == 7) {
                    newChess.player = 1;
                }
                board[i][j] = new grid((char) (97 + j), 8 - i, newChess);

                board[i][j].type.type = 0;
            }
        }

        return board;
    }

    public void setType(chessType type) {
        this.type = type;
    }

    public char getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public static boolean getColor(grid grid) {
        return grid.isWhite;
    }

    public static chessType getType(grid grid) {
        return grid.type;
    }

    /**
     * clear the selected grid
     * 
     * @author Xiaoyu Chen
     * 
     */
    public void clearGrid() {
        this.type = new chessType();
        type.player = 0;
        type.ifMoved = false;

    }

    /**
     * @author Xiaoyu Chen
     * @param x char, from 'a' to 'h'
     * @param y int, from '1 to 8'
     * @return int pos, from 0 - 63
     */
    public static int convert(char x, int y) {
        return 8 * (y - 1) + ((int) x - 97);
    }

    public int convert() {
        return 8 * (this.y - 1) + ((int) this.x - 97);
    }

    /**
     * @author Xiaoyu Chen
     * @param grid
     * @return the pos of the grid
     */
    public static int convert(grid grid) {
        return 8 * (grid.y - 1) + ((int) grid.x - 97);
    }

    /**
     * method used for test
     * 
     * @author Xiaoyu Chen
     * @param a int from 0 to 63
     * @return a printable string, eg. "a3"
     */
    public static String convertBack(int a) {
        int y = a / 8 + 1;
        char x = (char) (a % 8 + 97);
        return String.valueOf(x) + String.valueOf(y);
    }

    /**
     * turn the selected chess into king, initialize attackrange
     * 
     * @author Xiaoyu Chen
     * @param player 1 - white; 2 - black
     */
    public void turnKing(int player) {
        chessType type = new chessType(1);
        int pos = convert(x, y);
        ArrayList<Integer> range = new ArrayList<Integer>();
        range.addAll(Arrays.asList(pos + 7, pos + 8, pos + 9, pos - 1, pos + 1, pos - 9, pos - 8, pos - 7));
        // edgeDetect
        if (this.y == 8) {
            range.remove(Integer.valueOf(pos + 7));
            range.remove(Integer.valueOf(pos + 8));
            range.remove(Integer.valueOf(pos + 9));
        }
        if (this.y == 1) {
            range.remove(Integer.valueOf(pos - 9));
            range.remove(Integer.valueOf(pos - 8));
            range.remove(Integer.valueOf(pos - 7));
        }
        if (this.x == 'a') {
            range.remove(Integer.valueOf(pos + 7));
            range.remove(Integer.valueOf(pos - 1));
            range.remove(Integer.valueOf(pos - 9));
        }
        if (this.x == 'h') {
            range.remove(Integer.valueOf(pos + 9));
            range.remove(Integer.valueOf(pos + 1));
            range.remove(Integer.valueOf(pos - 7));
        }
        type.setRange(range);
        type.player = player;
        this.type = type;

    }

    /**
     * turn the selected chess into rook, initialize attackrange
     * 
     * @author Xiaoyu Chen
     * @param player 1 - white; 2 - black
     */
    public void turnRook(int player) {
        chessType type = new chessType(2);
        ArrayList<Integer> range = new ArrayList<Integer>();
        int pos = convert(x, y); // range: with in 0 - 63
        int i = pos + 8;
        while (i < 63) {
            range.add(i);
            i = i + 8;
        }

        i = pos - 8;
        while (i >= 0) {
            range.add(i);
            i = i - 8;
        }

        i = 1;
        while (i < pos % 8 + 1) {
            range.add(pos - i);
            i++;
        }

        i = 1;
        while (i < 8 - pos % 8) {
            range.add(pos + i);
            i++;
        }

        type.setRange(range);
        type.player = player;
        this.type = type;
    }

    /**
     * turn the selected chess into bishop, initialize attackrange
     * 
     * @author Xiaoyu Chen
     * @param player 1 - white; 2 - black
     */
    public void turnBishop(int player) {
        chessType type = new chessType(3);
        ArrayList<Integer> range = new ArrayList<Integer>();
        int pos = convert(x, y);
        int i = pos;
        while (i <= 63) {
            if (i % 8 != 7) {
                if (i != pos)
                    range.add(i);
                i = i + 9;
            } else {
                if (i != pos)
                    range.add(i);

                break;
            }
        }
        i = pos;
        while (i <= 63) {
            if (i % 8 != 0) {
                if (i != pos)
                    range.add(i);
                i = i + 7;
            } else {
                if (i != pos)
                    range.add(i);
                break;
            }
        }
        i = pos;
        while (i >= 0) {
            if (i % 8 != 0) {
                if (i != pos)

                    range.add(i);
                i = i - 9;
            } else {
                if (i != pos)

                    range.add(i);

                break;
            }
        }
        i = pos;
        while (i >= 0) {
            if (i % 8 != 7) {
                if (i != pos)

                    range.add(i);
                i = i - 7;
            } else {
                if (i != pos)

                    range.add(i);

                break;
            }
        }
        type.setRange(range);
        type.player = player;
        this.type = type;

    }

    /**
     * turn the selected chess into queen, initialize attackrange, rook and bishop
     * combined
     * 
     * @author Xiaoyu Chen
     * @param player 1 - white; 2 - black
     */
    public void turnQueen(int player) {
        chessType type = new chessType(4);
        ArrayList<Integer> range = new ArrayList<Integer>();
        int pos = convert(x, y); // range: with in 0 - 63
        int i = pos + 8;
        while (i < 63) {
            range.add(i);
            i = i + 8;
        }

        i = pos - 8;
        while (i >= 0) {
            range.add(i);
            i = i - 8;
        }

        i = 1;
        while (i < pos % 8 + 1) {
            range.add(pos - i);
            i++;
        }

        i = 1;
        while (i < 8 - pos % 8) {
            range.add(pos + i);
            i++;
        }
        i = pos;
        while (i <= 63) {
            if (i % 8 != 7) {
                if (i != pos)
                    range.add(i);
                i = i + 9;
            } else {
                if (i != pos)
                    range.add(i);

                break;
            }
        }
        i = pos;
        while (i <= 63) {
            if (i % 8 != 0) {
                if (i != pos)

                    range.add(i);
                i = i + 7;
            } else {
                if (i != pos)
                    range.add(i);
                break;
            }
        }
        i = pos;
        while (i >= 0) {
            if (i % 8 != 0) {
                if (i != pos)

                    range.add(i);
                i = i - 9;
            } else {
                if (i != pos)

                    range.add(i);

                break;
            }
        }
        i = pos;
        while (i >= 0) {
            if (i % 8 != 7) {
                if (i != pos)

                    range.add(i);
                i = i - 7;
            } else {
                if (i != pos)

                    range.add(i);

                break;
            }
        }
        type.setRange(range);
        type.player = player;
        this.type = type;

    }

    /**
     * turn the selected chess into rook, initialize attackrange
     * 
     * @author Xiaoyu Chen
     * @param player 1 - white; 2 - black
     */
    public void turnKnight(int player) { // ma
        chessType type = new chessType(5);
        ArrayList<Integer> range = new ArrayList<Integer>();
        int pos = convert(x, y); // range: with in 0 - 63
        range.addAll(Arrays.asList(pos + 6, pos + 10, pos + 15, pos + 17, pos - 10, pos - 6, pos - 17, pos - 15));
        if (this.y > 6) {
            range.remove(Integer.valueOf(pos + 15));
            range.remove(Integer.valueOf(pos + 17));
            if (this.y > 7) {
                range.remove(Integer.valueOf(pos + 6));
                range.remove(Integer.valueOf(pos + 10));
            }
        }

        if (this.y < 3) {
            range.remove(Integer.valueOf(pos - 17));
            range.remove(Integer.valueOf(pos - 15));
            if (this.y < 2) {
                range.remove(Integer.valueOf(pos - 10));
                range.remove(Integer.valueOf(pos - 6));
            }
        }
        if (this.x < 'c') {
            range.remove(Integer.valueOf(pos + 6));
            range.remove(Integer.valueOf(pos - 10));
            if (this.x < 'b') {
                range.remove(Integer.valueOf(pos + 15));
                range.remove(Integer.valueOf(pos - 17));
            }
        }
        if (this.x > 'f') {
            range.remove(Integer.valueOf(pos + 10));
            range.remove(Integer.valueOf(pos - 6));
            if (this.x > 'g') {
                range.remove(Integer.valueOf(pos + 17));
                range.remove(Integer.valueOf(pos - 16));
            }
        }
        type.setRange(range);
        type.player = player;
        this.type = type;

    }

    /**
     * turn the selected chess into pawn, initialize attackrange and moverange
     * 
     * @author Xiaoyu Chen
     * @param player 1 - white; 2 - black
     */
    public void turnPawn(int player) {
        chessType type = new chessType(6);
        ArrayList<Integer> range = new ArrayList<Integer>();
        int pos = convert(x, y); // range: with in 0 - 63
        if (player == 1) {
            range.add(pos + 8);
        } else if (player == 2) {
            range.add(pos - 8);
        }
        ArrayList<Integer> rangeP = new ArrayList<Integer>();
        if (player == 1) {
            if (this.x == 'a') {
                rangeP.add(pos + 9);
            } else if (this.x == 'h') {
                rangeP.add(pos + 7);
            } else {
                rangeP.add(pos + 7);
                rangeP.add(pos + 9);
            }
        } else if (player == 2) {
            if (this.x == 'a') {
                rangeP.add(pos - 7);
            } else if (this.x == 'h') {
                rangeP.add(pos - 9);
            } else {
                rangeP.add(pos - 7);
                rangeP.add(pos - 9);
            }
        }
        type.setRange(range);
        type.setRangeP(rangeP);
        type.player = player;
        this.type = type;
    }

    /**
     * a set of boolean that check the position that passed in
     * and check whether is in first, second, seventh or eighth column
     * 
     * also check whether is in second, seventh row
     * 
     * @author Dong Shu
     * 
     */
    public static final boolean[] inFirstColumn = intiColumn(0);
    public static final boolean[] inSecondColumn = intiColumn(1);
    public static final boolean[] inSeventhColumn = intiColumn(6);
    public static final boolean[] inEighthColumn = intiColumn(7);

    public static final boolean[] inSecondRow = initRow(8);
    public static final boolean[] inSeventhRow = initRow(48);

    public static boolean[] intiColumn(int columnNumber) {
        final boolean[] column = new boolean[64];
        do {
            column[columnNumber] = true;
            columnNumber += 8;
        } while (columnNumber < 64);
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[64];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % 8 != 0);
        return row;
    }

    public boolean isMoveLegal(int move) {
        return this.type.getRange().contains(move);
    }
}
