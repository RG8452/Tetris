
/*
 *T block
 *Key piece is in center
 *Shaped like a t (duh) 
 *Purple color
 */

import java.awt.Color;

public class T extends Tetromino {
	public static final Color tColor = new Color(190, 70, 177);

	public T() {
		super(tColor);

		blocks[0] = new Block(4, 1);
		blocks[1] = new Block(4, 0);
		blocks[2] = new Block(5, 1);
		blocks[3] = new Block(3, 1);
	}

	/*
	 * rotation notation: block 0 is always center blocks 123 will be the edges
	 * starting at the top and going clockwise in all scenarios rotation == 0 means
	 * the T is upside down, and 2 means it's right side up
	 */
	public void rotate(int dir) {
		undraw(); // erase current block

		Block[] temp = new Block[4]; // array used for checking next spot
		boolean valid = true; // if next rotation is valid
		int tempRotation = rotation; // stores current rotation

		if (dir == 1) // if rotating with 'e'
		{
			switch (rotation) {
			case 0: // up to right
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX, blocks[0].blockY + 1);

				rotation = 1;
				break;
			case 1: // right to down
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY);

				rotation = 2;
				break;
			case 2: // down to left
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY);

				rotation = 3;
				break;
			case 3: // left to up
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY);

				rotation = 0;
				break;
			default:
				System.out.println("OH NO");
			}
		}

		else if (dir == -1) // if rotating with 'q'
		{
			switch (rotation) {
			case 0: // up to left
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY);

				rotation = 3;
				break;
			case 1: // right to up
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY);

				rotation = 0;
				break;
			case 2: // down to right
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX, blocks[0].blockY + 1);

				rotation = 1;
				break;
			case 3: // left to down
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY);

				rotation = 2;
				break;
			default:
				System.out.println("OH NO");
			}
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