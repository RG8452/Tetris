
/*
 *Line block
 *4x1 line
 *Key Piece is on the bottom
 *The color is light blue
 */

import java.awt.Color;

public class Line extends Tetromino {
	public static final Color lineColor = new Color(40, 160, 217);

	public Line() // default constructor
	{
		super(lineColor); // create a Tetromino with that blue color

		blocks[0] = new Block(3, 0); // create a flat line above the grid
		blocks[1] = new Block(4, 0);
		blocks[2] = new Block(5, 0);
		blocks[3] = new Block(6, 0);
	}

	/*
	 * Rotation notation: 0 is 0123 3 is 2 is 3 1 is 0 3210 2 1 1 2 0 3 The line
	 * rotates around a square in the middle and the 0 moves clockwise
	 */
	public void rotate(int dir) {
		undraw(); // erase current block

		Block[] temp = new Block[4]; // array used for checking next spot
		boolean valid = true; // if next rotation is valid
		int tempRotation = rotation; // stores current rotation

		if (dir == 1) // if rotating with 'e'
		{
			switch (rotation) {
			case 0: // starts flat at top of middle square, moves to right
				temp[0] = new Block(blocks[2].blockX, blocks[2].blockY - 1);
				temp[1] = new Block(blocks[2].blockX, blocks[2].blockY);
				temp[2] = new Block(blocks[2].blockX, blocks[2].blockY + 1);
				temp[3] = new Block(blocks[2].blockX, blocks[2].blockY + 2);

				rotation = 1;
				break;
			case 1: // start on right, move to bottom
				temp[0] = new Block(blocks[2].blockX + 1, blocks[2].blockY);
				temp[1] = new Block(blocks[2].blockX, blocks[2].blockY);
				temp[2] = new Block(blocks[2].blockX - 1, blocks[2].blockY);
				temp[3] = new Block(blocks[2].blockX - 2, blocks[2].blockY);

				rotation = 2;
				break;
			case 2: // start on bottom, move to left
				temp[0] = new Block(blocks[2].blockX, blocks[2].blockY + 1);
				temp[1] = new Block(blocks[2].blockX, blocks[2].blockY);
				temp[2] = new Block(blocks[2].blockX, blocks[2].blockY - 1);
				temp[3] = new Block(blocks[2].blockX, blocks[2].blockY - 2);

				rotation = 3;
				break;
			case 3: // start on left, move to top
				temp[0] = new Block(blocks[2].blockX - 1, blocks[2].blockY);
				temp[1] = new Block(blocks[2].blockX, blocks[2].blockY);
				temp[2] = new Block(blocks[2].blockX + 1, blocks[2].blockY);
				temp[3] = new Block(blocks[2].blockX + 2, blocks[2].blockY);

				rotation = 0;
				break;
			default:
				System.out.println("OH NO");
			}
		}

		else if (dir == -1) // if rotating with 'q'
		{
			switch (rotation) {
			case 0: // starts flat at top of middle square, moves to left
				temp[0] = new Block(blocks[1].blockX, blocks[1].blockY + 2);
				temp[1] = new Block(blocks[1].blockX, blocks[1].blockY + 1);
				temp[2] = new Block(blocks[1].blockX, blocks[1].blockY);
				temp[3] = new Block(blocks[1].blockX, blocks[1].blockY - 1);

				rotation = 3;
				break;
			case 1: // start on right, move to top
				temp[0] = new Block(blocks[1].blockX - 2, blocks[1].blockY);
				temp[1] = new Block(blocks[1].blockX - 1, blocks[1].blockY);
				temp[2] = new Block(blocks[1].blockX, blocks[1].blockY);
				temp[3] = new Block(blocks[1].blockX + 1, blocks[1].blockY);

				rotation = 0;
				break;
			case 2: // start on bottom, move to right
				temp[0] = new Block(blocks[1].blockX, blocks[1].blockY - 2);
				temp[1] = new Block(blocks[1].blockX, blocks[1].blockY - 1);
				temp[2] = new Block(blocks[1].blockX, blocks[1].blockY);
				temp[3] = new Block(blocks[1].blockX, blocks[1].blockY + 1);

				rotation = 1;
				break;
			case 3: // start on left, move to bottom
				temp[0] = new Block(blocks[1].blockX + 2, blocks[1].blockY);
				temp[1] = new Block(blocks[1].blockX + 1, blocks[1].blockY);
				temp[2] = new Block(blocks[1].blockX, blocks[1].blockY);
				temp[3] = new Block(blocks[1].blockX - 1, blocks[1].blockY);

				rotation = 2;
				break;
			default:
				System.out.println("OH NO");
			}
		}

		int shift = 0; // used for forcing the block inbounds
		for (Block t : temp) {
			if (t.blockX > 9 && (t.blockX - 9) >= shift) // if the block is out right
			{
				shift = t.blockX - 9; // shift is the furthest distance out of the right
			}

			else if (t.blockX < 0 && t.blockX < shift) // if the block is out left
			{
				shift = t.blockX; // shift is the furthest distance out of the left
			}
		}

		for (Block t : temp) {
			t.blockX -= shift; // force the block in bounds
		}

		for (Block t : temp) {
			if (!t.checkBounds()) // if any of the spots are not in bounds
			{
				valid = false; // not a valid move
			}

			if (valid && !(Tetris.grid[t.blockY][t.blockX].equals(Color.BLACK))) {
				valid = false; // if any spots are taken, not a valid move
			}
		}

		if (valid) // if the move is still valid
		{
			for (int k = 0; k < 4; k++) {
				blocks[k] = temp[k]; // reset the array of blocks to its new position
			}
		}

		else // if the move is no longer valid
		{
			rotation = tempRotation; // reset rotation
		}

		draw(); // draw the block
	}
}

// old rotation code, does the wiggle worm across screen
/*
 * Block[] temp = new Block[4]; boolean valid = true;
 * 
 * if(rotation == 0) //if block starts at flat { if(dir == 1) //if rotating
 * right, place the block at 3rd spot vert { temp[0] = new
 * Block(blocks[2].blockX, blocks[2].blockY - 3); temp[1] = new
 * Block(blocks[2].blockX, blocks[2].blockY - 2); temp[2] = new
 * Block(blocks[2].blockX, blocks[2].blockY - 1); temp[3] = new
 * Block(blocks[2].blockX, blocks[2].blockY); }
 * 
 * else if(dir == -1) //if ccw, place bottom at 2nd spot up { temp[0] = new
 * Block(blocks[1].blockX, blocks[2].blockY - 3); temp[1] = new
 * Block(blocks[1].blockX, blocks[2].blockY - 2); temp[2] = new
 * Block(blocks[1].blockX, blocks[2].blockY - 1); temp[3] = new
 * Block(blocks[1].blockX, blocks[2].blockY); }
 * 
 * for(Block t: temp) { if(!t.checkBounds()) { valid = false; }
 * 
 * if(valid && !(Tetris.grid[t.blockY][t.blockX].equals(Color.BLACK) ||
 * Tetris.grid[t.blockY][t.blockX].equals(lineColor))) { valid = false; } }
 * 
 * if(valid) { rotation = 1; for(int k = 0; k < 4; k++) { blocks[k] = temp[k]; }
 * } }
 * 
 * else if(rotation == 1) //if the block starts vertical { if(dir == 1) //if
 * rotating right, place bottom at 2nd { temp[0] = new Block(blocks[3].blockX-1,
 * blocks[3].blockY); temp[1] = new Block(blocks[3].blockX, blocks[3].blockY);
 * temp[2] = new Block(blocks[3].blockX+1, blocks[3].blockY); temp[3] = new
 * Block(blocks[3].blockX+2, blocks[3].blockY); }
 * 
 * else if(dir == -1) //if rotating left, place bottom at 3rd { temp[0] = new
 * Block(blocks[3].blockX-2, blocks[3].blockY); temp[1] = new
 * Block(blocks[3].blockX-1, blocks[3].blockY); temp[2] = new
 * Block(blocks[3].blockX, blocks[3].blockY); temp[3] = new
 * Block(blocks[3].blockX+1, blocks[3].blockY); }
 * 
 * for(Block t: temp) { if(!t.checkBounds()) { valid = false; }
 * 
 * if(valid && !(Tetris.grid[t.blockY][t.blockX].equals(Color.BLACK) ||
 * Tetris.grid[t.blockY][t.blockX].equals(lineColor))) { valid = false; } }
 * 
 * if(valid) { rotation = 0; for(int k = 0; k < 4; k++) { blocks[k] = temp[k]; }
 * } }
 */