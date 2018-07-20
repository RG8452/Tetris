
/*
 *Ryan Gahagan
 *This is the handler class which does all logic for the Tetris game
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

class Tetris implements KeyListener, MouseListener {
	public static int block = GUI.block; // retrieves block size from frameTest class
	public static int pixel = block / 8; // used for specific block sizes

	public static Tetromino curTetromino = randomTetromino(); // current Tetromino
	public static Tetromino heldTetromino = null; // held Tetromino
	public static ArrayList<Tetromino> nextTetrominos = new ArrayList<Tetromino>(); // next list

	public static Color[][] grid = new Color[22][10]; // grid for which all blocks are handled

	public static boolean running = false; // if true, game is running
	public static boolean firstHold = true; // variable to check if a player is trying to hold a piece, happens only
											// once

	public static long start = (System.nanoTime()) / 1000000; // Gets game start time; used for timing piece drops
	public static long timerStart = start;
	public static long placeTimer = start;
	public static int delay = 1600; // Delay time between piece movements down, in miliseconds. Default is 1.5
									// seconds (1500 miliseconds)
	public static int frame = 33;

	public static long quickMove = (System.nanoTime()) / 1000000; // Timer for automatic movement
	public static int curXMove = 0; // Direction of x movement
	public static int moveDelay = 66; // Timing of automatic x movement
	public static boolean rightPressed = false; // Booleans for determening if a key is pressed
	public static boolean leftPressed = false;

	public static int level = 1; // ABRITRARY GAMEPLAY ELEMENTS
	public static int nextLines = 10;
	public static int lines = 0;
	public static int score = 0;
	public static long seconds = 0;

	public static int LineCount = 0; // List of the stats of how many of each block have been placed
	public static int SquareCount = 0;
	public static int LCount = 0;
	public static int JCount = 0;
	public static int SCount = 0;
	public static int ZCount = 0;
	public static int TCount = 0;
	public static Tetromino[] allBlocks = { new Square(), new T(), new S(), new Z(), new L(), new J(), new Line() };

	// keyListener methods
	public void keyPressed(KeyEvent e) {
		if (running) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT: // right arrow key
				curXMove = 1; // set x dir to right
				rightPressed = true; // set right to pressed
				quickMove = System.nanoTime() / 1000000; // begin automatic movement timer
				break;
			case KeyEvent.VK_LEFT: // left arrow key
				curXMove = -1; // set x dir to left
				leftPressed = true; // set left to pressed
				quickMove = System.nanoTime() / 1000000; // begin automatic movement timer
				break;
			case KeyEvent.VK_UP: // up arrow key
				curTetromino.drop(); // drop
				break;
			case KeyEvent.VK_DOWN: // down arrow key
				curTetromino.moveDown(); // soft drop
				if (!MainMenu.hardMode) {
					score += level;
				} else {
					score += 2 * level;
				}
				break;
			}
		}

		GUI.getMyPanel().repaint(); // Redraws screen after movements
	}

	public void keyReleased(KeyEvent e) {
		if (running) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT: // right arrow key
				if (!leftPressed) {
					curXMove = 0;
					quickMove = 0;
				}
				rightPressed = false;
				break;
			case KeyEvent.VK_LEFT: // left arrow key
				if (!rightPressed) {
					curXMove = 0;
					quickMove = 0;
				}
				leftPressed = false;
				break;
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		char c = e.getKeyChar();
		// System.out.println("You typed " + c);

		if (running) {
			switch (c) {
			case 'q': // If q is pressed
				curTetromino.rotate(-1); // Rotate current tetromino to the left
				break;
			case 'e': // If e is pressed
				curTetromino.rotate(1); // Rotate current tetromino to the right
				break;
			case 'w': // If w is pressed
				swapHeld(); // swap your held and current pieces
				firstHold = false;
				break;
			}
		}

		GUI.getMyPanel().repaint(); // Redraws screen after rotations
	}

	// MouseListener methods
	public void mouseClicked(MouseEvent e) {
		if (!running) {
			if (e.getY() > block * 15 && e.getY() < block * 20 && e.getX() > block * 6 && e.getX() < block * 14) {
				resetAll();
			}
		}

		GUI.getMyPanel().repaint();
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	// logic methods
	public static void play() // play method
	{
		if ((System.nanoTime() / 1000000) - start >= delay) // If the current time is [delay] seconds after the last
															// recorded time
		{
			curTetromino.moveDown(); // Move the current tetromino down one level
			start = (System.nanoTime()) / 1000000; // Reset the last recorded time to the current time
		}

		if ((System.nanoTime() / 1000000) - timerStart >= 1000) {
			timerStart = (System.nanoTime()) / 1000000;
			seconds++;
		}

		if ((System.nanoTime() / 1000000) - quickMove >= moveDelay) {
			curTetromino.moveX(curXMove);
			quickMove = System.nanoTime() / 1000000;
		}

		GUI.getMyPanel().repaint(); // Redraw the screen after movements
	}

	public static void place() // place curBlock, make a new one
	{
		if (System.nanoTime() / 1000000 - placeTimer > 20) {
			for (Block d : curTetromino.blocks) {
				if (d.blockY == 0 && !checkClear(d.blockY)) {
					running = false;
				}
			}

			int clearCount = 0;

			for (int currentRow = 21; currentRow >= 0; currentRow--) {
				if (checkClear(currentRow)) {
					running = true;
					clearRow(currentRow);
					clearCount++;
					currentRow++;
				}
			}

			if (clearCount > 0 && MainMenu.musicSelect != 'M') {
				java.awt.Toolkit.getDefaultToolkit().beep(); // play a beep
			}

			if (!MainMenu.hardMode) {
				switch (clearCount) {
				case 1: // single
					score += 40 * level;
					break;
				case 2: // double
					score += 100 * level;
					break;
				case 3: // triple
					score += 300 * level;
					break;
				case 4: // tetris
					score += 1200 * level;
					break;
				}
			} else {
				switch (clearCount) {
				case 1: // single
					score += 80 * level;
					break;
				case 2: // double
					score += 200 * level;
					break;
				case 3: // triple
					score += 600 * level;
					break;
				case 4: // tetris
					score += 2400 * level;
					break;
				}
			}

			lines += clearCount;
			nextLines -= clearCount;

			if (nextLines <= 0) {
				levelUp();
			}

			if (curTetromino instanceof L) {
				LCount++;
			} else if (curTetromino instanceof Line) {
				LineCount++;
			} else if (curTetromino instanceof Square) {
				SquareCount++;
			} else if (curTetromino instanceof T) {
				TCount++;
			} else if (curTetromino instanceof S) {
				SCount++;
			} else if (curTetromino instanceof Z) {
				ZCount++;
			} else if (curTetromino instanceof J) {
				JCount++;
			}

			if (running) {
				swapNext();
				start = System.nanoTime() / 1000000;
				firstHold = true;

				for (int checkOne = 0; checkOne < 22; checkOne++) {
					for (int checkTwo = 0; checkTwo < 10; checkTwo++) {
						if (grid[checkOne][checkTwo] == null) {
							grid[checkOne][checkTwo] = Color.black;
						}
					}
				}

				placeTimer = System.nanoTime() / 1000000;
			}
		}

		else // a piece is placed stupidly fast ie a glitch
		{
			curTetromino.undraw();

			for (Block c : curTetromino.blocks) {
				System.out.println(c.blockY);
			}

			for (int kk = 0; kk < 10; kk++) {
				grid[0][kk] = Color.black;
				grid[1][kk] = Color.black;
			}
			swapNext();
		}

		GUI.getMyPanel().repaint(); // Redraws screen
	}

	public static boolean checkClear(int row) // checks if a row can be cleared
	{
		for (int z = 0; z < 10; z++) {
			if (grid[row][z].equals(Color.BLACK)) // if there are any open spaces
			{
				return false;
			}
		}

		return true;
	}

	public static void clearRow(int row) // deletes a row and lowers everything above it
	{
		Color tempC;

		for (int r = row; r > 0; r--) {
			for (int k = 0; k < 10; k++) {
				tempC = grid[r - 1][k];
				grid[r][k] = tempC;
			}
		}

		for (int bl = 0; bl < 10; bl++) {
			grid[0][bl] = Color.black;
		}

		/*
		 * for(Block bbb: curTetromino.blocks) { if(bbb.blockY <= row) { bbb.blockY++;
		 * if(bbb.blockY > 21) {bbb.blockY = 21;} } }
		 */
	}

	public static void swapHeld() // swaps held piece and current piece
	{
		Tetromino tempTet = null;

		if (firstHold) // if you havent swapped on this piece
		{
			curTetromino.undraw();

			if (heldTetromino != null) // if your held piece isn't null
			{
				tempTet = heldTetromino; // store held

				// check each type of block and swap a new one into held to ensure a zero start
				if (curTetromino instanceof L) {
					heldTetromino = new L();
				} else if (curTetromino instanceof Line) {
					heldTetromino = new Line();
				} else if (curTetromino instanceof Square) {
					heldTetromino = new Square();
				} else if (curTetromino instanceof T) {
					heldTetromino = new T();
				} else if (curTetromino instanceof S) {
					heldTetromino = new S();
				} else if (curTetromino instanceof Z) {
					heldTetromino = new Z();
				} else if (curTetromino instanceof J) {
					heldTetromino = new J();
				}

				curTetromino = tempTet; // current becomes held
			}

			else // if you are on your first swap ever
			{
				// swap current piece into held piece
				if (curTetromino instanceof L) {
					heldTetromino = new L();
				} else if (curTetromino instanceof Line) {
					heldTetromino = new Line();
				} else if (curTetromino instanceof Square) {
					heldTetromino = new Square();
				} else if (curTetromino instanceof T) {
					heldTetromino = new T();
				} else if (curTetromino instanceof S) {
					heldTetromino = new S();
				} else if (curTetromino instanceof Z) {
					heldTetromino = new Z();
				} else if (curTetromino instanceof J) {
					heldTetromino = new J();
				}

				swapNext(); // swap into your next piece for current to avoid null
			}

			curTetromino.draw();
		}

		// if you have already hit swap, do nothing
	}

	public static void swapNext() // swaps in your next piece and moves the menu
	{
		// sets curTetromino to next tetromino
		curTetromino = nextTetrominos.remove(0);
		nextTetrominos.add(randomTetromino());
		curTetromino.draw();
	}

	public static Tetromino randomTetromino() // get a random Tetromino
	{
		if (!MainMenu.hardMode) // even randomization on easy mode
		{
			int rnd = (int) (Math.random() * 7); // Randomizes the next tetromino

			switch (rnd) {
			case 0:
				return new Square(); // If 0 make square
			case 1:
				return new Line(); // If 1 make line
			case 2:
				return new T(); // If 2 make T
			case 3:
				return new S(); // If 3 make S
			case 4:
				return new Z(); // If 4 make Z
			case 5:
				return new L(); // If 5 make L
			case 6:
				return new J(); // If 6 make J
			default: // Default case; if random returns an illegal number, print error message
				System.out.println("Too many random!");
				return null; // Defaults to line block if error occurs
			}
		}

		else // hard mode is more likely to create annoying blocks i.e. square, S, and Z
		{
			int rnd = (int) (Math.random() * 100); // Randomizes the next tetromino

			// Hard mode makes good pieces less common and bad pieces more common
			// Lines and Ts are each 12%
			// Ls and Js are each 14%
			// S, Z, and Square are each 16%
			if (rnd < 12) {
				return new Line();
			} else if (rnd < 24) {
				return new T();
			} else if (rnd < 38) {
				return new L();
			} else if (rnd < 52) {
				return new J();
			} else if (rnd < 68) {
				return new S();
			} else if (rnd < 84) {
				return new Z();
			} else {
				return new Square();
			}

		}

	}

	public static void levelUp() // used for all leveling logic
	{
		if (level <= 8) {
			nextLines = (5 * level + 5) + nextLines;
			delay -= 3 * frame;
		}

		else {
			nextLines = 60;
			delay -= 4 * frame;
		}

		if (MainMenu.hardMode) {
			if (level < 11) {
				delay -= frame * 3;
			}

			else {
				delay--;
			}
		}

		if (delay < (frame * 3) && !MainMenu.hardMode) {
			delay = frame * 3;
		} else if (MainMenu.hardMode && delay < (frame * 2)) {
			delay = frame * 2;
		}

		level++;
	}

	public static void clearGrid() // resets grid back to empty
	{
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j] = Color.black;
			}
		}
	}

	public static void resetAll() // resets game when "Replay?" is clicked
	{
		curTetromino = randomTetromino(); // current Tetromino
		heldTetromino = null; // held Tetromino

		clearGrid();

		nextTetrominos.clear();
		for (int l = 0; l < 3; l++) {
			nextTetrominos.add(randomTetromino());
		}

		running = true; // if true, game is running
		firstHold = true; // variable to check if a player is trying to hold a piece, happens only once

		start = (System.nanoTime()) / 1000000; // Gets game start time; used for timing piece drops
		timerStart = start;
		placeTimer = start;
		delay = 1600; // Delay time between piece movements down, in miliseconds. Default is 1.5
						// seconds (1500 miliseconds)

		quickMove = (System.nanoTime()) / 1000000; // Timer for automatic movement
		curXMove = 0; // Direction of x movement
		rightPressed = false; // Booleans for determening if a key is pressed
		leftPressed = false;

		lines = 0;
		score = 0;
		seconds = 0;

		LineCount = 0; // List of the stats of how many of each block have been placed
		SquareCount = 0;
		LCount = 0;
		JCount = 0;
		SCount = 0;
		ZCount = 0;
		TCount = 0;

		if (MainMenu.hardMode) // if the player is in hard mode
		{
			level = 9;
			delay = 24 * frame;
			nextLines = 50;
		} else {
			level = 1;
			delay = 1600;
			nextLines = 10;
		}

		GUI.getMyPanel().repaint();
	}

	// draw methods
	public static void drawBackground(Graphics g) // draws all background components
	{
		drawGrid(g);
		drawLines(g);
		drawMenus(g);
		drawHeldPiece(g);
		drawNextPieces(g);
		drawStatsPieces(g);
		drawTitle(g);

		if (!running) {
			drawReset(g);
		}
	}

	public static void fillGrid() // fills the grid with black
	{
		for (int l = 0; l < 3; l++) {
			nextTetrominos.add(randomTetromino());
		}

		for (Color[] c : grid) {
			// Arrays.fill(x, y) fills array x with y
			Arrays.fill(c, Color.black);
		}
	}

	public static void drawGrid(Graphics g) // fills the color of the grid
	{
		boolean bottom = false;
		for (int r = 2; r < 22; r++) // start at the 2nd index
		{
			for (int c = 0; c < 10; c++) {
				// Draw current tetromino
				g.setColor(grid[r][c]); // fills block
				g.fillRect(block * (c + 5), block * (r + 3), block, block);

				g.setColor(grid[r][c].brighter()); // puts a lighter edge in the top left
				g.fillRect(block * (c + 5), block * (r + 3), pixel * 7, pixel);
				g.fillRect(block * (c + 5), block * (r + 3), pixel, pixel * 7);

				g.setColor(grid[r][c].brighter().brighter()); // puts a double light pixel in the TL corner
				g.fillRect(block * (c + 5), block * (r + 3), pixel, pixel);

				g.setColor(grid[r][c].darker()); // puts a darker edge in the bottom right
				g.fillRect(block * (c + 5) + pixel, block * (r + 4) - pixel, pixel * 7, pixel);
				g.fillRect(block * (c + 6) - pixel, block * (r + 3) + pixel, pixel, pixel * 7);

				g.setColor(grid[r][c].darker().darker()); // puts a double dark pixel in the BR corner
				g.fillRect(block * (c + 6) - pixel, block * (r + 4) - pixel, pixel, pixel);
			}
		}
	}

	public static void drawNextPieces(Graphics g) // fills in the NEXT menu
	{
		int sBlock = block * 4 / 5; // draws small blocks
		int sPixel = pixel * 4 / 5;
		double guideX;
		int guideY;
		Color curColor;

		for (int z = 0; z < 3; z++) // loop through each piece
		{
			for (Block b : nextTetrominos.get(z).blocks) {
				if (!MainMenu.hardMode) {
					curColor = nextTetrominos.get(z).getColor();
				} else {
					curColor = Tetromino.ghost;
				}

				guideX = b.blockX + 17.25;
				guideY = b.blockY + 8;

				g.setColor(curColor);
				g.fillRect((int) (sBlock * guideX), sBlock * (guideY + 3 * z), sBlock, sBlock);

				g.setColor(curColor.brighter());
				g.fillRect((int) (sBlock * guideX), sBlock * (guideY + 3 * z), sPixel * 7 + 2, sPixel);
				g.fillRect((int) (sBlock * guideX), sBlock * (guideY + 3 * z), sPixel, sPixel * 7 + 2);

				g.setColor(curColor.brighter().brighter());
				g.fillRect((int) (sBlock * guideX), sBlock * (guideY + 3 * z), sPixel, sPixel);

				g.setColor(curColor.darker());
				g.fillRect((int) (sBlock * guideX) + sPixel, sBlock * (guideY + 1 + 3 * z) - sPixel, sPixel * 7,
						sPixel);
				g.fillRect((int) (sBlock * (guideX + 1)) - sPixel, sBlock * (guideY + 3 * z) + sPixel, sPixel,
						sPixel * 7);

				g.setColor(curColor.darker().darker());
				g.fillRect((int) (sBlock * (guideX + 1)) - sPixel, sBlock * (guideY + 1 + 3 * z) - sPixel, sPixel,
						sPixel);
			}
		}
	}

	public static void drawHeldPiece(Graphics g) // fills in the NEXT menu
	{
		int sBlock = block * 3 / 4; // draws small blocks
		int sPixel = pixel * 3 / 4;
		double guideX;
		double guideY;
		Color curColor;

		if (heldTetromino != null) {
			for (Block b : heldTetromino.blocks) {
				if (!MainMenu.hardMode) {
					curColor = heldTetromino.getColor();
				} else {
					curColor = Tetromino.ghost;
				}

				guideX = b.blockX - 1.66;
				guideY = b.blockY + 8.5;

				g.setColor(curColor);
				g.fillRect((int) (sBlock * guideX), (int) (sBlock * guideY), sBlock, sBlock);

				g.setColor(curColor.brighter());
				g.fillRect((int) (sBlock * guideX), (int) (sBlock * guideY), sPixel * 7 + 2, sPixel);
				g.fillRect((int) (sBlock * guideX), (int) (sBlock * guideY), sPixel, sPixel * 7 + 2);

				g.setColor(curColor.brighter().brighter());
				g.fillRect((int) (sBlock * guideX), (int) (sBlock * guideY), sPixel, sPixel);

				g.setColor(curColor.darker());
				g.fillRect((int) (sBlock * guideX) + sPixel, (int) (sBlock * (guideY + 1)) - sPixel, sPixel * 7,
						sPixel);
				g.fillRect((int) (sBlock * (guideX + 1)) - sPixel, (int) (sBlock * guideY) + sPixel, sPixel,
						sPixel * 7);

				g.setColor(curColor.darker().darker());
				g.fillRect((int) (sBlock * (guideX + 1)) - sPixel, (int) (sBlock * (guideY + 1)) - sPixel, sPixel,
						sPixel);
			}
		}
	}

	public static void drawStatsPieces(Graphics g) // draws in tiny blocks in the stats menu for reference
	{
		double guideX;
		double guideY;
		double yShift = 0;
		Color curColor;

		if (block % 3 == 0) {
			int tBlock = block * 1 / 3; // draws tiny blocks
			int tPixel = pixel * 1 / 3;

			for (Tetromino tet : allBlocks) {
				for (Block b : tet.blocks) {
					guideX = b.blockX - .33;
					guideY = b.blockY + 33 + yShift;

					if (!MainMenu.hardMode) {
						curColor = tet.getColor();
					} else {
						curColor = Tetromino.ghost;
					}

					g.setColor(curColor);
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tBlock, tBlock);

					g.setColor(curColor.brighter());
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel * 7 + 2, tPixel);
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel, tPixel * 7 + 2);

					g.setColor(curColor.brighter().brighter());
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel, tPixel);

					g.setColor(curColor.darker());
					g.fillRect((int) (tBlock * guideX) + tPixel, (int) (tBlock * (guideY + 1)) - tPixel, tPixel * 7,
							tPixel);
					g.fillRect((int) (tBlock * (guideX + 1)) - tPixel, (int) (tBlock * guideY) + tPixel, tPixel,
							tPixel * 7);

					g.setColor(curColor.darker().darker());
					g.fillRect((int) (tBlock * (guideX + 1)) - tPixel, (int) (tBlock * (guideY + 1)) - tPixel, tPixel,
							tPixel);
				}
				yShift += .25 * tBlock;
			}
		}

		else if (block == 32) {
			int tBlock = block * 3 / 8; // draws tiny blocks
			int tPixel = pixel * 3 / 8;

			for (Tetromino tet : allBlocks) {
				for (Block b : tet.blocks) {
					guideX = b.blockX - .75;
					guideY = b.blockY + 29.5 + yShift;

					if (!MainMenu.hardMode) {
						curColor = tet.getColor();
					} else {
						curColor = Tetromino.ghost;
					}

					g.setColor(curColor);
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tBlock, tBlock);

					g.setColor(curColor.brighter());
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel * 7 + 2, tPixel);
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel, tPixel * 7 + 2);

					g.setColor(curColor.brighter().brighter());
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel, tPixel);

					g.setColor(curColor.darker());
					g.fillRect((int) (tBlock * guideX) + tPixel, (int) (tBlock * (guideY + 1)) - tPixel, tPixel * 7 + 2,
							tPixel);
					g.fillRect((int) (tBlock * (guideX + 1)) - tPixel, (int) (tBlock * guideY) + tPixel, tPixel,
							tPixel * 7 + 2);

					g.setColor(curColor.darker().darker());
					g.fillRect((int) (tBlock * (guideX + 1)) - tPixel, (int) (tBlock * (guideY + 1)) - tPixel, tPixel,
							tPixel);
				}
				yShift += .3 * tBlock;
			}
		}

		else {
			int tBlock = block * 1 / 2; // draws tiny blocks
			int tPixel = pixel * 1 / 2;

			for (Tetromino tet : allBlocks) {
				for (Block b : tet.blocks) {
					guideX = b.blockX - 1.75;
					guideY = b.blockY + 21.5 + yShift;

					if (!MainMenu.hardMode) {
						curColor = tet.getColor();
					} else {
						curColor = Tetromino.ghost;
					}

					g.setColor(curColor);
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tBlock, tBlock);

					g.setColor(curColor.brighter());
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel * 7 + 2, tPixel);
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel, tPixel * 7 + 2);

					g.setColor(curColor.brighter().brighter());
					g.fillRect((int) (tBlock * guideX), (int) (tBlock * guideY), tPixel, tPixel);

					g.setColor(curColor.darker());
					g.fillRect((int) (tBlock * guideX) + tPixel, (int) (tBlock * (guideY + 1)) - tPixel, tPixel * 7,
							tPixel);
					g.fillRect((int) (tBlock * (guideX + 1)) - tPixel, (int) (tBlock * guideY) + tPixel, tPixel,
							tPixel * 7);

					g.setColor(curColor.darker().darker());
					g.fillRect((int) (tBlock * (guideX + 1)) - tPixel, (int) (tBlock * (guideY + 1)) - tPixel, tPixel,
							tPixel);
				}
				yShift += .14 * tBlock;
			}
		}
	}

	public static void drawLines(Graphics g) // draws white lines of the grid
	{
		g.setColor(Color.white);
		g.drawRect(block * 5, block * 5, block * 10, block * 20); // draw the border

		for (int q = 5; q < 15; q++) // draws the 8x8 grid
		{
			g.drawLine(block * q, block * 5, block * q, block * 25); // draw 10 columns

			g.drawLine(block * 5, block * (q + 1), block * 15, block * (q + 1)); // draws 10 rows
			g.drawLine(block * 5, block * (q + 11), block * 15, block * (q + 11)); // draws 10 more rows
		}
	}

	public static void drawMenus(Graphics g) // draws each menu
	{
		g.setFont(new Font("TimesRoman", Font.PLAIN, block / 2));
		Color fontColor = Color.white;

		g.setColor(Color.RED); // NEXT Menu
		g.drawRect((int) (block * 15.5), block * 5, block * 4, block * 10);
		g.setColor(fontColor);
		g.drawString("NEXT", block * 17 - block / 4, block * 5 - 2);

		g.setColor(Color.GREEN); // HOLD Menu
		g.drawOval((int) (block * .5), block * 5, block * 4, block * 4);
		g.setColor(fontColor);
		g.drawString("HOLD", block * 2 - block / 4, block * 5 - 2);

		g.setColor(Color.cyan); // LEVEL Menu
		g.drawRect((int) (block * .5), block * 24, block * 4, block);
		g.setColor(fontColor);
		g.drawString("LEVEL: " + level, (int) (block * .5) + 2, block * 25 - block / 3);

		g.setColor(Color.magenta); // LINES Menu
		g.drawRect((int) (block * .5), (int) (block * 20.5), block * 4, block * 2);
		g.drawRect((int) (block * .5), (int) (block * 21.5), block * 4, block * 2);
		g.setColor(fontColor);
		g.drawString("LINES:", (int) (block * 1.5), (int) (block * 21.5 - block / 3));
		g.drawString("TOTAL: " + lines, (int) (block * .5) + 2, (int) (block * 22.5 - block / 3));
		g.drawString("NEXT:  " + nextLines, (int) (block * .5) + 2, (int) (block * 23.5 - block / 3));

		g.setColor(new Color(37, 123, 249)); // SCORE Menu
		g.drawRect((int) (block * 15.5), block * 22, block * 4, block * 2);
		g.drawLine((int) (block * 15.5), block * 23, (int) (block * 19.5), block * 23);
		g.setColor(fontColor);
		g.drawString("SCORE:", block * 17 - (int) (block / 2.5), block * 23 - block / 3);
		g.drawString(String.valueOf(score), block * 17 - (int) (block / 2.5), block * 24 - block / 3);

		DecimalFormat timeOut = new DecimalFormat("00");
		g.setColor(new Color(186, 226, 38)); // TIME Menu
		g.drawRect((int) (block * 15.5), (int) (block * 19.5), block * 4, block * 2);
		g.drawLine((int) (block * 15.5), (int) (block * 20.5), (int) (block * 19.5), (int) (block * 20.5));
		g.setColor(fontColor);
		g.drawString("TIME:", (int) (block * 17), (int) (block * 20.5 - block / 3));
		g.drawString(timeOut.format(seconds / 60) + ":" + timeOut.format(seconds % 60), (int) (block * 17),
				(int) (block * 21.5 - block / 3));

		DecimalFormat statsOut = new DecimalFormat("000");
		g.setColor(new Color(204, 83, 255)); // STATS Menu
		g.drawRect((int) (block * .5), (int) (block * 9.5), block * 4, (int) (block * 10.5));
		g.drawLine((int) (block * .5), (int) (block * 10.5), (int) (block * 4.5), (int) (block * 10.5));
		g.setColor(fontColor);
		g.drawString("STATS", (int) (block * 2 - block / 3), (int) (block * 10.5 - block / 3));
		g.setFont(new Font("TimesRoman", Font.BOLD, (int) (block * .75)));

		double jumpDist = 1.33;
		double start = 11.5;
		g.drawString("- " + statsOut.format(SquareCount), (int) (block * 2.66), (int) (block * start));
		start += jumpDist;
		g.drawString("- " + statsOut.format(TCount), (int) (block * 2.66), (int) (block * start));
		start += jumpDist;
		g.drawString("- " + statsOut.format(SCount), (int) (block * 2.66), (int) (block * start));
		start += jumpDist;
		g.drawString("- " + statsOut.format(ZCount), (int) (block * 2.66), (int) (block * start));
		start += jumpDist;
		g.drawString("- " + statsOut.format(LCount), (int) (block * 2.66), (int) (block * start));
		start += jumpDist;
		g.drawString("- " + statsOut.format(JCount), (int) (block * 2.66), (int) (block * start));
		start += jumpDist;
		g.drawString("- " + statsOut.format(LineCount), (int) (block * 2.66), (int) (block * start));
	}

	public static void drawTitle(Graphics g) // draws in the "TETRIS" title in rainbow
	{
		g.setFont(new Font("TimesRoman", Font.BOLD, block * 4)); // set new font

		// this loops through the rainbow and approximates letter locations and sizes
		g.setColor(Color.red);
		g.drawString("T", block * 4 - block / 2, block * 5 - 2);
		g.setColor(Color.orange);
		g.drawString("E", block * 6, block * 5 - 2);
		g.setColor(Color.yellow);
		g.drawString("T", block * 8 + block / 6, block * 5 - 2);
		g.setColor(Color.green);
		g.drawString("R", block * 11 - block / 2, block * 5 - 2);
		g.setColor(Color.blue);
		g.drawString("I", block * 13 + block / 6, block * 5 - 2);
		g.setColor(new Color(255, 0, 255, 255));
		g.drawString("S", block * 14 + block / 3, block * 5 - 2);
	}

	public static void drawReset(Graphics g) // draws the replay button
	{
		g.setColor(Color.black);
		g.fillRect(block * 6 + 1, block * 15 + 1, block * 8 - 1, block * 5 - 1);

		g.setColor(Color.red);
		g.fillRect(block * 6 + 1, block * 15 + 1, block * 8 - 1, block);
		g.fillRect(block * 6 + 1, block * 19 + 1, block * 8 - 1, block - 1);
		g.fillRect(block * 6 + 1, block * 15 + 1, block, block * 5 - 1);
		g.fillRect(block * 13 + 1, block * 15 + 1, block - 1, block * 5 - 1);

		g.setColor(Color.white);
		g.setFont(new Font("Serif", Font.BOLD, (int) (block * .9)));
		g.drawString("GAME OVER", (int) (block * 7.25), block * 17);
		g.drawString("Replay?", (int) (block * 8.5), (int) (block * 18.5));
	}
}