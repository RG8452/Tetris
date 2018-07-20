
/*
 *L block
 *Key piece will be in the center of the L
 *orange color
 */

import java.awt.Color;

public class L extends Tetromino {
	public static final Color lColor = new Color(245, 139, 16);

	public L() // default constructor
	{
		super(lColor);

		blocks[0] = new Block(4, 1);
		blocks[1] = new Block(5, 0);
		blocks[2] = new Block(3, 1);
		blocks[3] = new Block(5, 1);
	}

	/**
	 * Rotation Notation: Block 0 will always be in the center of the long leg The
	 * rest are pretty irrelevant, they just need to fill positions rotation 0 is
	 * once ccw from L
	 */
	public void rotate(int dir) {
		undraw(); // erase current block

		Block[] temp = new Block[4]; // array used for checking next spot
		boolean valid = true; // if next rotation is valid
		int tempRotation = rotation; // stores current rotation

		if (dir == 1) // if rotated with 'e'
		{
			switch (rotation) {
			case 0:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY + 1);

				rotation = 1;
				break;
			case 1:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY + 1);

				rotation = 2;
				break;
			case 2:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY - 1);

				rotation = 3;
				break;
			case 3:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY - 1);

				rotation = 0;
				break;
			}
		}

		else if (dir == -1) // if rotated w/ 'w'
		{
			switch (rotation) {
			case 0:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY - 1);

				rotation = 3;
				break;
			case 1:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY - 1);

				rotation = 0;
				break;
			case 2:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY + 1);

				rotation = 1;
				break;
			case 3:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY + 1);

				rotation = 2;
				break;
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