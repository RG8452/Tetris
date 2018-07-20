
/*
 *Each block on the grid
 *Used as components in each tetromino, 4 apiece
 */

import java.awt.Color;
import java.awt.Graphics;

public class Block {
	public int blockX; // XY coords, according to Tetris.grid
	public int blockY;

	public Block(int x, int y) // constructor
	{
		blockX = x;
		blockY = y;
	}

	public boolean check() // returns true if the spot is totally valid
	{
		if (checkBounds()) {
			if (checkEmpty()) {
				return true;
			}
		}

		return false;
	}

	public boolean checkBounds() // returns true if the spot is in bounds
	{
		if (blockX >= 0 && blockX <= 9) {
			if (blockY >= 0 && blockY <= 21) {
				return true;
			}
		}

		return false;
	}

	public boolean checkEmpty() // returns true if the spot is empty
	{
		if (Tetris.grid[blockX][blockY].equals(Color.BLACK)) {
			return true;
		}

		return false;
	}
}
