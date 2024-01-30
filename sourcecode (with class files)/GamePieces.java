/**
 * Charles Omaoeng
 * CS251L
 * GamePieces.java
 * Enum that contains all game piece types
 */

/**
 * EMPTY - an EMPTY placeholder on the board, no special features
 * SNAKE - the main moving piece on the board, a single SNAKE piece on the board corresponds to a single body part
 * APPLE - if a SNAKE piece collides with an APPLE piece, score and maxLen goes up by 1
 * WALL - if a SNAKE piece collides with a WALL piece, the game ends; WALL serves as the boundary of the gameBoard
 * GOLDAPPLE - if a SNAKE piece collides with GOLDAPPLE, score and maxLen goes up by 5
 * SPEEDAPPLE - if a SNAKE piece collides with SPEEDAPPLE, score and maxLen goes up by 1, speed increases by 10
 * PUMPKINAPPLE - if a SNAKE piece collides with PUMPKINAPPLE, score and maxLen goes up by 10, speed decreases by 10
 * if speedIncr is more than 10
 */
public enum GamePieces {
    APPLE,
    EMPTY,
    SNAKE,
    WALL,
    GOLDAPPLE,
    SPEEDAPPLE,
    PUMPKINAPPLE,
}
