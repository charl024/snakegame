Snake.jar

SECTION I: HOW TO RUN
- After downloading Snake.jar, run 'java -jar Snake.jar' in current directory on Command Line to run Snake.
- Once running this command, a window will appear.
- This window will display a menu screen where you can choose what level to play.

	SECTION IA: RUNNING CUSTOM LEVEL FILES
	- Additionally, you can run your own custom level files (.txt files).
	- This is done by specifying your level file in the Command Line argument to run Snake.jar.
	  ex. running the command 'java -jar Snake.jar maze-simple.txt' 
	- Make sure your custom level file is in the same directory/folder as Snake.jar.
	- If you do run a custom level file, level selection menu will not be displayed.
	- Instead, your level file will be displayed immediately.
	
	SECTION IB: CUSTOM LEVEL FILE SPECIFICATIONS
	- Level files (.txt files) need to be formatted a certain way to be valid.
	- The first line specifies the WIDTH and HEIGHT of the play area of snake. NOTE: WIDTH and HEIGHT cannot exceed 80 (only numbers below 80 and over 0 are VALID)
	  ex. "80 80"
	- The next lines specify WALL placement.
	- These lines consist of 4 different numbers (integers only, cannot be decimals).
	  The 1st number is the x-value coordinate of the TOP LEFT corner of the WALL (where it starts).
	  The 2nd number is the x-value coordinate of the BOTTOM RIGHT corner of the WALL (where it ends).
	  The 3rd number is the y-value coordinate of the TOP LEFT corner of the WALL (where it starts).
	  The 4th number is the y-value coordinate of the BOTTOM RIGHT corner of the WALL (where it ends).
	  ex. "0 79 0 0"
	  - This specifies a wall that starts from the top left corner of the play area to the top right corner of the play area.
	  ex. "0 0 0 79"
	  - This specifies a wall that starts from the top left corner of the play area to the bottom left corner of the play area.
	- You can have as many lines of wall specifications as you want.
	EXAMPLE LAYOUT:
		80 80
		0 79 0 0
		0 79 79 79
		0 0 0 79
		79 79 0 79
		
SECTION II: HOW TO PLAY
- Arrow keys are used to move the snake.
- UP ARROW KEY moves the snake up, DOWN ARROW KEY moves the snake down, LEFT ARROW KEY moves the snake left, RIGHT ARROW KEY moves the snake right
- Once the level is displayed on the window, either press the spacebar or the start button to begin the game.
- A snake and an apple will spawn somewhere in the play area.
- Your goal is to have your snake consume as many apples as possible by moving it using the arrow keys.
- Every time your snake consumes an apple, it grows longer and you get a point to your overall score. There are special apples that may spawn in certain conditions.
- Everytime an apple is eaten, your score goes up by 1. Special apples may increase or decrease score.
- You lose if the snake either hits a WALL or runs into itself.

	SECTION IIA: SPECIAL APPLES
	- There are 3 special applies that may spawn.
	- GOLDEN APPLES: If eaten, gives 5 points and increases length by 5. Only spawns if score is equal to or more than 10.
	- SPEED APPLES: If eaten, gives 1 point and increases length by 1. Increases speed of snake. Only spawns if score is equal to or more than 20 and speed is below a certain point (75).
	- PUMPKIN APPLES: If eaten, gives 10 points and increases length by 10. Decreases speed of snake. Only spawns if score is equal to or more than 20.

SECTION III: GAME DATA

	SECTION IIIA: CLASSES
	- RunSnake.java : Runs Snake, it does this by creating an instance of MainGame.java. This is the java file that is called with the Command Line command. Takes in Command Line input.
	- MainGame.java : Contains and initializes the main GUI JFrame for snake as well as the JPanel that contains the graphics for Snake.
	- GameManager.java : Contains all game logic. Takes in Command Line input from RunSnake.java.
	- Snake.java : Contains information pertaining to a SNAKE game piece.
	- Wall.java : Contains information pertaining to a WALL on the play area. 
	- GamePieces.java : An enum class that has all the game pieces used in Snake.

	SECTIONIIIB: SNAKE ALGORITHM
	- The "snake" shown on the GUI and game panel in this Snake game is actually a Deque of "snake" (Snake.java) objects. 
	- Each "snake" object contains two integers (row and column) representing a certain point on the game board.
	- As the snake "moves" through the game board, the snake object in the first element of the Deque has its corresponding row and column information updated.
	- In other words, row and col are either subtracted by 1, or have 1 added to them depending on the direction moved.
	- This change in coordinate is updated on the board. The rest of the snake objects in the Deque that make up the visible "snake" on the game board are also updated during this process, thus making it seem like the snake is
	"slithering" across the board.
	- A snake has a maximum length, it can only be as long as that maximum length. Once the game starts, the snake will extend up to that maximum length.
	- In this Snake game, the code will check whether or not the snake has reached its maximum length. If it hasn't reached its maximum length, then it will keep "growing" until it has reached that maximum length.
	- If the snake eats an apple, the maximum length will increase.

	SECTIONIIIC: COLLISION DETECTION & GAME OVER
	- The game detects collision through a method utilizing a switch.
	- This switch will check if a tile surrounding the "head" of the snake is a certain game piece.
	- If the game piece is not a WALL or a SNAKE, this method will return a boolean value of "false".
	- If it does detect a WALL or a SNAKE, this method will return a boolean value of "true".
	- The graphics displayed on the window will be updated as long as collision is false.
	- As soon as it is set to true, the graphics will stop being updated and the game instance ends.
	- A dialog will appear that will prompt the user to either continue or quit the game.
	
	SECTIONIIID: KNOWN BUGS
	- Pressing two arrow keys in quick succession may cause the snake to collide with itself and end the game.
		- The cause of this is likely the way I am preventing the snake from going backwards on itself and ending the game prematurely because it hit itself.
		- Improving the checking method will solve this bug.
