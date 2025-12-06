package chess;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    int row;
    int col;

    HashMap<Integer, Integer> rowMapper;
    HashMap<Integer, Character> columnMapper;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;

        columnMapper = new HashMap<>();
        columnMapper.put(1, 'a');
        columnMapper.put(2, 'b');
        columnMapper.put(3, 'c');
        columnMapper.put(4, 'd');
        columnMapper.put(5, 'e');
        columnMapper.put(6, 'f');
        columnMapper.put(7, 'g');
        columnMapper.put(8, 'h');

        rowMapper = new HashMap<>();
        rowMapper.put(1, 8);
        rowMapper.put(2, 7);
        rowMapper.put(3, 6);
        rowMapper.put(4, 5);
        rowMapper.put(5, 4);
        rowMapper.put(6, 3);
        rowMapper.put(7, 2);
        rowMapper.put(8, 1);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }

    @Override
    public String toString() {
        return "ChessPosition{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    public String toChessTablePosition() {
        String c = String.valueOf(columnMapper.get(col));
        String r = String.valueOf(rowMapper.get(row));
        return c + r;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
