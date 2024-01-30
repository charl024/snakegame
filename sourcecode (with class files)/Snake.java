/**
 * Charles Omaoeng
 * CS251L
 * Snake.java
 * Contains information pertaining to SNAKE gamePiece
 */
public record Snake(int snakeRow, int snakeCol) {
    /**
     * @return row int of current snake object
     */
    @Override
    public int snakeRow() {
        return snakeRow;
    }

    /**
     * @return col int of current snake object
     */
    @Override
    public int snakeCol() {
        return snakeCol;
    }

}
