
/*
 *Square block
 *2x2 square, yellow color
 *Key piece is always in the top left
 *Rotation is negligible
 */

import java.awt.Color;

public class Square extends Tetromino {
	public static final Color squareColor = new Color(240, 240, 5);

	public Square() // default constructor
	{
		super(squareColor); // creates a tetromino with this yellow color

		blocks[0] = new Block(4, 0); // creates the square outside the grid
		blocks[1] = new Block(5, 0);
		blocks[2] = new Block(4, 1);
		blocks[3] = new Block(5, 1);
	}

	public void rotate(int dir) {
		// do nothing, as squares don't rotate
	}
}
