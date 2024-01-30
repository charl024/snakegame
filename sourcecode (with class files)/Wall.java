/** 
 *  Wall.java
 *  Contains information pertaining to WALL gamePiece
 */

public class Wall {
    private final int wallLeftBound;
    private final int wallRightBound;
    private final int wallUpBound;
    private final int wallLowBound;

    /**
     * A constructor that takes a string with given wall information, and parses that string to set bounds
     * @param wallStr
     */
    Wall(String wallStr) {
        String[] wallLine = wallStr.split(" ");
        this.wallLeftBound = Integer.parseInt(wallLine[0]);
        this.wallRightBound = Integer.parseInt(wallLine[1]);
        this.wallUpBound = Integer.parseInt(wallLine[2]);
        this.wallLowBound = Integer.parseInt(wallLine[3]);
    }

    /**
     * @return int that represents the leftBound of a wall
     */
    public int getWallLeftBound() {
        return wallLeftBound;
    }

    /**
     * @return int that represents the rightBound of a wall
     */
    public int getWallRightBound() {
        return wallRightBound;
    }

    /**
     * @return int that represents the upBound of a wall
     */
    public int getWallUpBound() {
        return wallUpBound;
    }

    /**
     * @return int that represents the lowBound of a wall
     */
    public int getWallLowBound() {
        return wallLowBound;
    }

}
