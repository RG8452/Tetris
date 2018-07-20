
/*
 *Abstract class that each block will extend
 */

import java.awt.Color;

public abstract class Tetromino {
	protected int rotation; // orientation of block

	protected Block[] blocks = new Block[4]; // contains the 4 blocks which make up the tetromino

	protected Color blockColor; // tetromino default color

	public static final Color ghost = new Color(229, 229, 229);

	public Tetromino(Color b) // constructs a tetromino of rotation 0 and a new Color
	{
		rotation = 0; // Sets initial rotation
		blockColor = b; // Color of block
	}

	public void moveX(int dir) // moves the block left and right, pos dir is right and negative dir is left
	{
		undraw(); // fills current block with black
		boolean out = false; // whether or not the spot is valied

		for (Block b : blocks) // check each block
		{
			if (b.blockX > 0 && b.blockX < 9) // if in bounds
			{
				if (Tetris.grid[b.blockY][b.blockX + dir].equals(Color.BLACK)) // if spot is black
				{
					// nothing (Block will treat space as empty)
				}

				else // if spot is not black
				{
					out = true; // tetromino is impacting an illegal location; piece is 'out'
				}
			}

			b.blockX += dir; // move the block

			if (b.blockX > 9 || b.blockX < 0) // if out of bounds
			{
				out = true; // out is true
			}
		}

		if (out) // if out of bounds
		{
			for (Block b : blocks) // move each block back
			{
				b.blockX -= dir;
			}
		}

		draw();
	}

	public void moveDown() // upon down arrow key, move the block down
	{
		undraw();
		boolean out = false;

		for (Block b : blocks) // check each block
		{
			if (b.blockY < 21) // if in bounds
			{
				if (!MainMenu.hardMode) {
					if (Tetris.grid[b.blockY + 1][b.blockX].equals(Color.BLACK)
							|| Tetris.grid[b.blockY + 1][b.blockX].equals(ghost)) // if spot is black
					{
						// do nothing
					}

					else // if spot is not black
					{
						out = true; // out
					}
				}

				else {
					if (Tetris.grid[b.blockY + 1][b.blockX].equals(Color.BLACK)) // if spot is black
					{
						// do nothing
					}

					else // if spot is not black
					{
						out = true; // out
					}
				}
			}

			b.blockY++; // move the block

			if (b.blockY > 21) // if out of bounds
			{
				out = true; // out is true
			}
		}

		if (out) // if out of bounds
		{
			for (Block b : blocks) // move each block back
			{
				b.blockY--;

				if (b.blockY == 0 && !Tetris.checkClear(b.blockY)) {
					Tetris.running = false;
				}
			}

			draw();

			Tetris.place();
		}

		// this logic places the block immediately upon reaching the bottom
		// #nowiggle
		/*
		 * for(Block b: blocks) { if(b.blockY == 21) { Tetris.place(); } }
		 */

		if (!out) {
			draw();
		}
	}

	public void drop() // upon up arrow key, drop block instantly
	{
		undraw();

		boolean out = false;
		int fallCount = 0;
		while (!out) {
			for (Block b : blocks) // check each block
			{
				if (b.blockY < 21) // if in bounds
				{
					if (!MainMenu.hardMode) {
						if (Tetris.grid[b.blockY + 1][b.blockX].equals(Color.BLACK)
								|| Tetris.grid[b.blockY + 1][b.blockX].equals(ghost)) // if spot is black
						{
							// do nothing
						}

						else // if spot is not black
						{
							out = true; // out
						}
					}

					else {
						if (Tetris.grid[b.blockY + 1][b.blockX].equals(Color.BLACK)) // if spot is black
						{
							// do nothing
						}

						else // if spot is not black
						{
							out = true; // out
						}
					}
				}

				fallCount++;
				b.blockY++; // move the block

				if (b.blockY > 21) // if out of bounds
				{
					out = true; // out is true
				}
			}
		}

		for (Block b : blocks) {
			b.blockY--;

			if (b.blockY == 0 && !Tetris.checkClear(b.blockY)) {
				Tetris.running = false;
			}
		}

		fallCount /= 4; // set fall count for a full piece, not blocks
		fallCount--;

		if (!MainMenu.hardMode) {
			Tetris.score += Tetris.level * fallCount * 2; // add 2 score per block per level
		} else {
			Tetris.score += Tetris.level * fallCount * 4; // add 4 score per block per level in hard mode
		}

		draw(); // redraw

		Tetris.place();
	}

	public void undraw() // erases the current block
	{
		if (!MainMenu.hardMode) // if you're in easy mode, undraw the ghose
		{
			int minX = 9;
			int maxX = 0;
			int top = 22;

			for (Block b : blocks) // loop each block
			{
				if (b.blockX > maxX) {
					maxX = b.blockX;
				} // determine bounds of block
				if (b.blockX < minX) {
					minX = b.blockX;
				}
				if (b.blockY < top) {
					top = b.blockY;
				}
			}

			for (int r = 21; r >= top; r--) // loop from the bottom up to the block
			{
				for (int c = minX; c <= maxX; c++) // loop from the left of the block to the right
				{
					if (Tetris.grid[r][c].equals(ghost)) // if your spot is white
					{
						Tetris.grid[r][c] = Color.black; // clear it
					}
				}
			}
		}

		for (Block b : blocks) // clear the block
		{
			Tetris.grid[b.blockY][b.blockX] = Color.black;
		}
	}

	public void draw() // fills in the current blocks
	{
		if (!MainMenu.hardMode) // if you're in easy mode, draw a ghost piece
		{
			boolean out = false;
			int fallCount = 0;
			int tempFallCount = 0;

			while (!out) // loop to find distance for ghost piece
			{
				for (int z = 0; z < 4; z++) // loop each block
				{
					tempFallCount = fallCount;
					Block b = blocks[z];

					if (b.blockY + tempFallCount < 22) {
						if (Tetris.grid[b.blockY + tempFallCount][b.blockX].equals(Color.BLACK)) // if spot is black
						{
							// do nothing
						} else {
							out = true;
						}
					}

					tempFallCount++; // increase temp

					if (b.blockY + tempFallCount > 21) // if too low
					{
						out = true;
					}

					if (!out && !Tetris.grid[b.blockY + tempFallCount][b.blockX].equals(Color.BLACK)) // if spot is not
																										// black
					{
						out = true; // must be out
					}
				}

				if (!out) {
					fallCount++;
				}
			}

			for (Block b : blocks) {
				Tetris.grid[b.blockY + fallCount][b.blockX] = ghost; // draw in the ghost blocks FIRST
			}
		}

		for (Block b : blocks) {
			if (!MainMenu.hardMode) {
				Tetris.grid[b.blockY][b.blockX] = blockColor; // draw in the actual blocks SECOND
			} else {
				Tetris.grid[b.blockY][b.blockX] = ghost;
			}
		}
	}

	public Color getColor() {
		return blockColor;
	}

	public abstract void rotate(int dir); // abstract rotation method
}
