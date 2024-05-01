package src.piece;

import java.util.ArrayList;

/**
 * @author Xiaoyu Chen
 */
public class chessType {
    public int type = 0;
    public String letter; // a single printable letter which stands for the chess type
    public int player; // 0-undefined; 1-white; 2-black
    public boolean ifMoved;
    public ArrayList<Integer> attackRange;
    public ArrayList<Integer> attackRangeP; // additional range for pawn

    public chessType() {
        this.type = 0;
        this.player = 0;
        ArrayList<Integer> temp = new ArrayList<Integer>();
        this.attackRange = temp;
    }

    /**
     * @param i represents for the type of chess
     *          0: null
     *          1: king
     *          2: rook
     *          3: bishop
     *          4: queen
     *          5: knight
     *          6: pawn
     */
    public chessType(int i) {
        this.type = i;
        ArrayList<Integer> temp = new ArrayList<Integer>();
        this.attackRange = temp;

        if (i == 1) {
            this.letter = "K";
        } else if (i == 2) {
            this.letter = "R";
        } else if (i == 3) {
            this.letter = "B";
        } else if (i == 4) {
            this.letter = "Q";
        } else if (i == 5) {
            this.letter = "N";
        } else if (i == 6) {
            this.letter = "P";
        }
    }

    /**
     * 
     * @param i     represents for type of chess
     * @param range range of the chess
     */

    public chessType(int i, ArrayList<Integer> range) {
        this.type = i;
        this.attackRange = range;
    }

    /**
     * assign an range
     * 
     * @param range a ArrayList of integers
     */
    public void setRange(ArrayList<Integer> range) {
        this.attackRange = range;
    }

    /**
     * special case for pawn; includes the range that this pawn can attack on, not
     * move to
     * size of thie range should be 0 - 2;
     * 
     * @param range a Arraylist of integers
     */
    public void setRangeP(ArrayList<Integer> range) {
        this.attackRangeP = range;
    }

    /**
     * I made attackrange public, this might be useless
     * 
     * @return the attackrange of this chess
     */
    public ArrayList<Integer> getRange() {
        return this.attackRange;
    }

    /**
     * 
     * @param a an integer(0-63) that you want to add to the attackrange
     */
    public void add(int a) {
        this.attackRange.add(a);
    }
}