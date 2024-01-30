/**
 * MainGame.java
 * The main GUI display for snake game
 * Extends JFrame, and includes a nested class (MainGamePanel)
 * MainGame includes the MainGamePanel, infoPanel (displays score, and startButton)
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

public class MainGame extends JFrame {
    private GameManager game;
    private GamePieces[][] board;
    private final String[] args;

    private final int cellSize = 10;
    private int width;
    private int height;
    private final String messageStart = "Press SPACE to Start";

    private final JButton button = new JButton(messageStart);
    private JLabel scoreLabel;
    private JLabel speedLabel;
    private Timer gameTimer;
    private boolean paused = true;
    private int delay = 100;


    private int mapChoice = 0;
    private final JPanel mainPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private MainGamePanel mainGamePanel;

    private int moveInt;
    int currMove = KeyEvent.VK_LEFT;
    int preMove = KeyEvent.VK_LEFT;

    /**
     * Constructs a MainGame object, displays the main GUI for the Snake Game
     * @param args commandLine argument with the name of the Level file (.txt file)
     */
    public MainGame(String[] args) throws IOException {
        super("Snake ");
        this.args = args;

        this.setLayout(new GridBagLayout());

        setMainPanel();

        this.getContentPane().add(mainPanel);

        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Sets up width and height to be used for the MainGamePanel
     * Additionally, sets the GameManager along with the gameBoard from that instance of GameManager
     * @param args commandLine argument with the name of the Level file (.txt file)
     * @param mapChoice integer that decides what level to display
     */
    private void setup(String[] args, int mapChoice) throws IOException {
        this.game = new GameManager(args, mapChoice);

        this.width = game.getMaxCol() * cellSize;
        this.height = game.getMaxRow() * cellSize;
        this.board = game.getBoard();
    }

    /**
     * Sets up the main panel to be displayed
     * If there are no given commandLine arguments, displays a menu screen that allows for different levels to be chosen
     * If there is a commandLine argument, displays that map specified by given level file
     */
    private void setMainPanel() throws IOException {
        mainPanel.setLayout(cardLayout);
        mainPanel.setBackground(Color.black);
        mainPanel.setPreferredSize(new Dimension(800,850));

        if (args.length == 0) {
            mainPanel.add(mapMenuPanel());
            cardLayout.first(mainPanel);
        } else {
            setup(args, mapChoice);
            mainPanel.add(playPanel());
        }

    }

    /**
     * JPanel that contains the infoPanel (has scoreLabel and startButton) and
     * backgroundPanel (has MainGamePanel)
     * @return JPanel with crucial game components (score, startButton, MainGamePanel)
     */
    private JPanel playPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(backgroundPanel());
        mainPanel.add(infoPanel());

        return mainPanel;
    }

    /**
     * JPanel that displays the current score and has the startButton
     * @return JPanel with score display and startButton
     */
    private JPanel infoPanel() {
        JPanel infoPanel = new JPanel();

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        infoPanel.setPreferredSize(new Dimension(800,50));
        infoPanel.setBackground(Color.white);

        scoreLabel = new JLabel("Current Score: " + 0);
        speedLabel = new JLabel("Current Speed: " + 0);

        infoPanel.add(Box.createHorizontalGlue());
        infoPanel.add(speedLabel);
        infoPanel.add(Box.createHorizontalGlue());
        infoPanel.add(scoreLabel);
        infoPanel.add(Box.createHorizontalGlue());
        infoPanel.add(startButton());
        infoPanel.add(Box.createHorizontalGlue());

        return infoPanel;
    }

    /**
     * JPanel that holds the main snake game panel (MainGamePanel)
     * @return JPanel that holds MainGamePanel
     */
    private JPanel backgroundPanel() {
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(Color.black);
        backgroundPanel.setPreferredSize(new Dimension(800, 800));

        mainGamePanel = new MainGamePanel();
        backgroundPanel.add(mainGamePanel);

        return backgroundPanel;
    }

    /**
     * JPanel that displays the levels to be selected
     * @return JPanel that allows for choice of level
     */
    private JPanel mapMenuPanel() {
        JPanel backgroundPanel = new JPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setBackground(Color.lightGray);
        backgroundPanel.setPreferredSize(new Dimension(800, 800));

        backgroundPanel.add(mapButtonPanel());

        return backgroundPanel;
    }

    /**
     * JPanel that holds the buttons that allow for the user to choose levels
     * The actionListener for each button calls changeMap method
     * @return JPanel with map choice buttons
     */
    private JPanel mapButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        ButtonGroup choiceGroup = new ButtonGroup();
        JRadioButton defaultChoice = new JRadioButton("Default Box");
        JRadioButton zigzagChoice = new JRadioButton("Zig-Zag");
        JRadioButton crossChoice = new JRadioButton("Cross");

        defaultChoice.addActionListener(e -> {
            mapChoice = 0;
            changeMap(args, mapChoice);

        });
        zigzagChoice.addActionListener(e -> {
            mapChoice = 1;
            changeMap(args, mapChoice);
        });
        crossChoice.addActionListener(e -> {
            mapChoice = 2;
            changeMap(args, mapChoice);
        });

        choiceGroup.add(defaultChoice);
        choiceGroup.add(zigzagChoice);
        choiceGroup.add(crossChoice);

        buttonPanel.add(defaultChoice);
        buttonPanel.add(zigzagChoice);
        buttonPanel.add(crossChoice);

        return buttonPanel;
    }

    /**
     * changes the level to be displayed, and switches what is being displayed on mainPanel to playPanel (which displays
     * the chosen level)
     * @param args commandLine argument with the name of the Level file (.txt file)
     * @param mapChoice integer that decides what level to display
     */
    private void changeMap(String[] args, int mapChoice) {
        try {
            setup(args, mapChoice);
            mainPanel.add(playPanel());
            cardLayout.last(mainPanel);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        mainGamePanel.requestFocusInWindow();
    }

    /**
     * JButton that starts the game or stops the game
     * @return JButton that toggles pause
     */
    private JButton startButton() {
        button.addActionListener(e -> togglePause());
        return button;
    }

    /**
     * A method that changes the paused state of gameTimer.
     * if paused is true, start gameTimer and set pause to false
     * if paused is false, stop gameTimer and set pause to true
     * in both cases, the button text is changed to suit the state
     */
    private void togglePause() {
        if (paused) {
            paused = false;
            gameTimer.start();
            String messageStop = "Press SPACE to Stop";
            button.setText(messageStop);
        } else {
            paused = true;
            gameTimer.stop();
            button.setText(messageStart);
        }
    }

    /**
     * This method is called when the SNAKE collides with itself or a WALL.
     * Ends the current game, stops the timer.
     * Displays a dialog that asks user to start a new game.
     * If YES is selected, the board, score, speed, direction, and button label is reset.
     * If NO is selected, the JFrame (MainGame) is forcibly closed.
     */
    private void gameOver() {
        gameTimer.stop();
        int choice = JOptionPane.showConfirmDialog(this, "Start new game?", "Game Over!", JOptionPane.YES_NO_OPTION);
        if (choice == 0) {
            game.newGame();
            scoreLabel.setText("Current Score: " + game.getScore());
            speedLabel.setText("Current Speed: " + game.getSpeedIncr());
            button.setText(messageStart);
            currMove = KeyEvent.VK_LEFT;
            preMove = KeyEvent.VK_LEFT;
            moveInt = 0;
            paused = true;
            cardLayout.first(mainPanel);
            delay = 100;

        } else {
            this.dispose();
        }
        repaint();
    }

    /**
     * Loops over the gameBoard and draws on the MainGamePanel depending on the gamePiece
     * WALL - light brown
     * SNAKE - light yellow
     * APPLE - red
     * GOLDAPPLE - yellow
     * SPEEDAPPLE - blue
     * PUMPKINAPPLE - orange
     * EMPTY - light grass green
     * @param graphics graphics to draw
     */
    private void fillBoard(Graphics graphics) {
        for(int i = 0; i < board.length; i++) {
            for(int j = 0; j < board[0].length; j++) {
                switch (board[i][j]) {
                    case WALL -> {
                        graphics.setColor(new Color(130,100,70));
                        graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case SNAKE -> {
                        graphics.setColor(new Color(255,255,150));
                        graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case APPLE -> {
                        graphics.setColor(Color.red);
                        graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case GOLDAPPLE -> {
                        graphics.setColor(Color.YELLOW);
                        graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case SPEEDAPPLE -> {
                        graphics.setColor(Color.BLUE);
                        graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    case PUMPKINAPPLE -> {
                        graphics.setColor(Color.ORANGE);
                        graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }
                    default -> {
                        graphics.setColor(new Color(154,247,100));
                        graphics.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                    }

                }
            }
        }
    }

    /**
     * A class nested inside MainGame, extends JPanel and implements KeyListener and ActionLister
     * This is where the graphics of Snake Game is drawn and set-up
     */
    private class MainGamePanel extends JPanel implements KeyListener, ActionListener {
        /**
         * Constructs a MainGamePanel object
         * Sets the size of this panel
         * Sets the game timer, the default state is paused
         */
        public MainGamePanel() {
            this.setPreferredSize(new Dimension(width, height));

            gameTimer = new Timer(delay, this);

            gameTimer.stop();

            this.addKeyListener(this);
            this.setFocusable(true);
            this.requestFocusInWindow();
        }

        /**
         * Calls GameManager's move method depending on the value of moveInt
         */
        private void moveSnake() {
            switch (moveInt) {
                case 0 -> game.move(0,-1);
                case 1 -> game.move(0,1);
                case 2 -> game.move(1,0);
                case 3 -> game.move(-1,0);
            }
        }

        /**
         * paints the panel
         * @param graphics the <code>Graphics</code> object to protect
         */
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            fillBoard(graphics);
        }

        @Override
        public void keyTyped(KeyEvent e) {

        }

        /**
         * Changes the value of moveInt depending on the key pressed
         * @param e the event to be processed
         */
        @Override
        public void keyPressed(KeyEvent e) {
            if (!paused) {
                currMove = e.getKeyCode();
                if (currMove == KeyEvent.VK_LEFT && preMove != KeyEvent.VK_RIGHT) {
                    moveInt = 0;
                    preMove = KeyEvent.VK_LEFT;
                } else if (currMove == KeyEvent.VK_RIGHT && preMove != KeyEvent.VK_LEFT) {
                    moveInt = 1;
                    preMove = KeyEvent.VK_RIGHT;
                } else if (currMove == KeyEvent.VK_DOWN && preMove != KeyEvent.VK_UP) {
                    moveInt = 2;
                    preMove = KeyEvent.VK_DOWN;
                } else if (currMove == KeyEvent.VK_UP && preMove != KeyEvent.VK_DOWN) {
                    moveInt = 3;
                    preMove = KeyEvent.VK_UP;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                togglePause();
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        /**
         * Drives the gameplay for snake game
         * if the snake is not colliding on itself or a wall:
         * Calls moveSnake to move the snake
         * Updates the scoreLabel
         * repaints the board to match the changes in gameBoard
         * Else, gameOver is called.
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!game.isCollision()) {
                moveSnake();
                scoreLabel.setText("Current Score: " + game.getScore());
                speedLabel.setText("Current Speed: " + game.getSpeedIncr());
                repaint();
                this.requestFocusInWindow();
            } else {
                gameOver();
            }

            gameTimer.setDelay(delay - game.getSpeedIncr());
        }
    }

}
