/**
 * Charles Omaoeng
 *  CS251L
 *  GameManager.java
 *  Initializes Snake game, contains all game logic
 *  Needs a Level file (text file) containing board & wall specifications to work
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameManager {

    private int maxRow; //height, board[maxRow][maxCol], y-axis
    private int maxCol; //width, board[maxRow][maxCol], x-axis
    private final GamePieces[][] board;
    private final ArrayList<Wall> walls = new ArrayList<>();
    private final Random random;

    private final Deque<Snake> snake = new LinkedList<>();
    private int maxLen = 5;
    private int score = 0;
    private int speedIncr = 0;

    private boolean collision = false;


    /**
     * Creates a new GameManager,
     * takes in command line argument args, if level file is specified it is initialized,
     * else default level is initialized
     * @param args command line args
     */
    GameManager(String[] args, int mapChoice) throws IOException {
        this.random = new Random();
        File level;

        if (args.length != 0) {
            level = new File(args[0]);
            readLevelFile(level);
        } else {
            getChosenLevel(mapChoice);
        }

        this.board = new GamePieces[maxRow][maxCol];
        initBoard();
    }


    /**
     * reads level file,
     * sets maxRow, maxCol, and initializes wall information
     * @param level text file that contains level configs
     */
    private void readLevelFile(File level) throws IOException {
        try (BufferedReader input = new BufferedReader(new FileReader(level))) {
            ArrayList<String> levelLines = new ArrayList<>();
            String line;
            while ((line = input.readLine()) != null) {
                if (!line.isEmpty()) {
                    levelLines.add(line);
                }

            }
            String[] size = levelLines.get(0).split(" ");
            maxCol = Integer.parseInt(size[0]);
            maxRow = Integer.parseInt(size[1]);
            levelLines.remove(0);

            addWallsList(levelLines);
        }
    }

    /**
     * Uses a switch to choose what map level is to be utilized for snake game
     * @param mapChoice int, value of int determines what is chosen
     */
    private void getChosenLevel(int mapChoice) {
        switch (mapChoice) {
            case 1 -> zigzagLevel();
            case 2 -> crossLevel();
            default -> defaultLevel();
        }
    }

    /**
     * set maxRow, maxCol, and initializes wall information based on default settings
     * this map is a basic box
     */
    private void defaultLevel() {
        ArrayList<String> levelLines = new ArrayList<>();
        levelLines.add("40 40");
        levelLines.add("0 39 0 0");
        levelLines.add("0 39 39 39");
        levelLines.add("0 0 0 39");
        levelLines.add("39 39 0 39");
        String[] size = levelLines.get(0).split(" ");
        maxCol = Integer.parseInt(size[0]);
        maxRow = Integer.parseInt(size[1]);
        levelLines.remove(0);

        addWallsList(levelLines);
    }

    /**
     * set maxRow, maxCol, and initializes wall information based on default settings
     * this map is a rectangular box with zigzag wall obstacles
     */
    private void zigzagLevel() {
        ArrayList<String> levelLines = new ArrayList<>();
        levelLines.add("71 45");
        levelLines.add("0 70 0 0");
        levelLines.add("0 70 44 44");
        levelLines.add("0 0 0 44");
        levelLines.add("70 70 0 44");
        levelLines.add("10 10 1 35");
        levelLines.add("20 20 9 43");
        levelLines.add("30 30 1 35");
        levelLines.add("40 40 9 43");
        levelLines.add("50 50 1 35");
        levelLines.add("60 60 9 43");
        String[] size = levelLines.get(0).split(" ");
        maxCol = Integer.parseInt(size[0]);
        maxRow = Integer.parseInt(size[1]);
        levelLines.remove(0);

        addWallsList(levelLines);
    }

    /**
     * set maxRow, maxCol, and initializes wall information based on default settings
     * this map is a square box with a large cross-shaped obstacle in the middle
     */
    private void crossLevel() {
        ArrayList<String> levelLines = new ArrayList<>();
        levelLines.add("60 60");
        levelLines.add("0 59 0 0");
        levelLines.add("0 59 59 59");
        levelLines.add("0 0 0 59");
        levelLines.add("59 59 0 59");
        levelLines.add("27 30 9 48");
        levelLines.add("9 24 27 30");
        levelLines.add("33 48 27 30");

        String[] size = levelLines.get(0).split(" ");
        maxCol = Integer.parseInt(size[0]);
        maxRow = Integer.parseInt(size[1]);
        levelLines.remove(0);

        addWallsList(levelLines);
    }

    /**
     * adds lines of strings from level file into ArrayList
     * @param levelLines ArrayList containing all Wall objects
     */
    private void addWallsList(ArrayList<String> levelLines) {
        for (String lines : levelLines) {
            Wall tempWall = new Wall(lines);
            walls.add(tempWall);
        }
    }

    /**
     * Initializes board,
     * sets all pieces to empty,
     * then adds walls,
     * and finally spawns Apple and Snake at a random location
     */
    private void initBoard() {
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                board[row][col] = GamePieces.EMPTY;
            }
        }
        placeWalls(walls);
        spawnApple();
        spawnSnake();
    }

    /**
     * Adds all walls in allWalls to the board
     * @param allWalls ArrayList containing walls to-be-added
     */
    private void placeWalls(ArrayList<Wall> allWalls) {
        for (Wall wall : allWalls) {
            placeWall(wall);
        }
    }

    /**
     * Adds a wall to board using information from a Wall object
     * @param wall Wall object that has leftBound, rightBound, upperBound, and lowerBound information
     */
    private void placeWall(Wall wall) {
        int startRow = wall.getWallUpBound();
        int startCol = wall.getWallLeftBound();
        int endRow = wall.getWallLowBound() + 1;
        int endCol = wall.getWallRightBound() + 1;

        for (int wallRow = startRow; wallRow < endRow; wallRow++) {
            for (int wallCol = startCol; wallCol < endCol; wallCol++) {
                board[wallRow][wallCol] = GamePieces.WALL;
            }
        }
    }

    /**
     * replaces a random EMPTY gamePiece on the board with an APPLE,
     * APPLE may either be a normal APPLE, a GOLDAPPLE (+5 more points), or a SPEEDAPPLE (increments snake speed)
     */
    private void spawnApple() {
        int appleRow = random.nextInt(maxRow);
        int appleCol = random.nextInt(maxCol);
        int specialAppleNum = random.nextInt(200);

        if (board[appleRow][appleCol] != GamePieces.EMPTY) {
            spawnApple();
        } else {
            if (specialAppleNum < 10 && score >= 20) {
                board[appleRow][appleCol] = GamePieces.PUMPKINAPPLE;
            } else if(specialAppleNum > 10 && specialAppleNum < 20 && score >= 20 && speedIncr <= 70) {
                board[appleRow][appleCol] = GamePieces.SPEEDAPPLE;
            } else if(specialAppleNum > 20 && specialAppleNum < 40 && score >= 10) {
                board[appleRow][appleCol] = GamePieces.GOLDAPPLE;
            } else {
                board[appleRow][appleCol] = GamePieces.APPLE;
            }
        }
    }

    /**
     * replaces a random EMPTY gamePiece on the board with a SNAKE
     */
    private void spawnSnake() {
        int snakeCol = random.nextInt(maxCol);
        int snakeRow = random.nextInt(maxRow);

        if (board[snakeRow][snakeCol] != GamePieces.EMPTY) {
            spawnSnake();
        } else {
            snake.add(new Snake(snakeRow, snakeCol));
            board[snakeRow][snakeCol] = GamePieces.SNAKE;
        }
    }

    /**
     * Checks if a place on the board is valid for the SNAKE to move onto
     * @param row row int to-be-checked
     * @param col col int to-be-checked
     * @return True if board[row][col] is not valid, else false
     */
    private boolean collision(int row, int col) {
        switch (board[row][col]) {
            case APPLE, EMPTY, GOLDAPPLE, SPEEDAPPLE, PUMPKINAPPLE -> {return false;}
            default -> {
                collision = true;
                return true;
            }
        }
    }

    /**
     * Checks if a place on the board is a type of APPLE.
     * APPLE - increases score and maxLen by 1
     * GOLDAPPLE increases score and maxLen by 5
     * SPEEDAPPLE increases score and maxLen by 1, increases speed by 10
     * PUMPKINAPPLE increases score and maxLen by 10, decreases speed by 10
     * @param row row int to-be-checked
     * @param col col int to-be-checked
     */
    private void appleCheck(int row, int col) {
        if (board[row][col] == GamePieces.APPLE) {
            spawnApple();
            score++;
            maxLen++;
        } else if (board[row][col] == GamePieces.GOLDAPPLE) {
            spawnApple();
            score += 5;
            maxLen += 5;
        } else if (board[row][col] == GamePieces.SPEEDAPPLE) {
            spawnApple();
            score++;
            maxLen++;
            speedIncr += 10;
        } else if (board[row][col] == GamePieces.PUMPKINAPPLE) {
            spawnApple();
            score += 10;
            maxLen += 10;
            speedIncr -= 10;
        }
    }

    /**
     * Moves the snake by a given int value, while also checking collision and apple position
     * However, value must be valid.
     * Valid values: 0, 1, -1
     * @param changeRow row int that represents how much the snake head moves
     * @param changeCol col int that represents how much the snake head moves
     */
    public void move(int changeRow, int changeCol) {
        Snake head = snake.getFirst();
        if (!collision(head.snakeRow() + changeRow, head.snakeCol() + changeCol)) {
            if (snake.size() < maxLen) {
                Snake nextPart = new Snake(head.snakeRow() + changeRow, head.snakeCol() + changeCol);

                appleCheck(head.snakeRow() + changeRow, head.snakeCol() + changeCol);

                snake.push(nextPart);

                board[nextPart.snakeRow()][nextPart.snakeCol()] = GamePieces.SNAKE;
            }
            else {
                Snake nextPart = new Snake(head.snakeRow() + changeRow, head.snakeCol() + changeCol);

                appleCheck(head.snakeRow() + changeRow, head.snakeCol() + changeCol);

                snake.push(nextPart);
                board[snake.getFirst().snakeRow()][snake.getFirst().snakeCol()] = GamePieces.SNAKE;
                board[snake.getLast().snakeRow()][snake.getLast().snakeCol()] = GamePieces.EMPTY;
                snake.removeLast();
            }
        }
    }

    /**
     * gets the maxRow
     * @return int value of the maxRow
     */
    public int getMaxRow() {
        return maxRow;
    }

    /**
     * gets the maxCol
     * @return int value of the maxCol
     */
    public int getMaxCol() {
        return maxCol;
    }

    /**
     * gets the gameBoard
     * @return 2D GamePiece array
     */
    public GamePieces[][] getBoard() {
        return board;
    }

    /**
     * gets current score
     * @return int value of score
     */
    public int getScore() {
        return score;
    }

    /**
     * Returns a String representation of the game board
     * @return String representing the board
     */
    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder();

        for (GamePieces[] pieceRow : board) {
            for (GamePieces piece : pieceRow) {
                switch (piece) {
                    case WALL -> boardStr.append("X");
                    case APPLE -> boardStr.append("f");
                    case SNAKE -> boardStr.append("S");
                    default -> boardStr.append(".");
                }
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    public boolean isCollision() {
        return collision;
    }

    /**
     * resets to default settings when called
     * snake is emptied,
     * a new board is initiated,
     * collision, score, maxLen, and speedIncr are reset to default
     */
    public void newGame() {
        snake.clear();
        initBoard();
        collision = false;
        score = 0;
        maxLen = 5;
        speedIncr = 0;
    }

    /**
     * gets the speedIncr
     * @return int value of speedIncr
     */
    public int getSpeedIncr() {
        return speedIncr;
    }
}


