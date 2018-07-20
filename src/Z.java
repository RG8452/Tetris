
/*
 *Z block
 *Red color
 *Key piece is identical to s piece
 */

import java.awt.Color;

public class Z extends Tetromino {
	public static final Color zColor = new Color(244, 51, 21);

	public Z() {
		super(zColor);

		blocks[0] = new Block(4, 0);
		blocks[1] = new Block(3, 0);
		blocks[2] = new Block(4, 1);
		blocks[3] = new Block(5, 1);
	}

	/**
	 * Rotation Notation: center block is always 0 other blocks read left to right,
	 * top to bottom
	 *
	 * 0: XXX 1: X1X 10X 20X X23 3XX
	 *
	 * 2: 12X 3: XX1 X03 X02 XXX X3X
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
				temp[2] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY + 1);

				rotation = 1;
				break;
			case 1:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX - 1, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY);

				rotation = 2;
				break;
			case 2:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX + 1, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX, blocks[0].blockY + 1);

				rotation = 3;
				break;
			case 3:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY + 1);

				rotation = 0;
				break;
			}
		}

		else if (dir == -1) // if rotated with 'w'
		{
			switch (rotation) {
			case 0:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX + 1, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX + 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX, blocks[0].blockY + 1);

				rotation = 3;
				break;
			case 1:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY + 1);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY + 1);

				rotation = 0;
				break;
			case 2:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX - 1, blocks[0].blockY);
				temp[3] = new Block(blocks[0].blockX - 1, blocks[0].blockY + 1);

				rotation = 1;
				break;
			case 3:
				temp[0] = new Block(blocks[0].blockX, blocks[0].blockY);
				temp[1] = new Block(blocks[0].blockX - 1, blocks[0].blockY - 1);
				temp[2] = new Block(blocks[0].blockX, blocks[0].blockY - 1);
				temp[3] = new Block(blocks[0].blockX + 1, blocks[0].blockY);

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