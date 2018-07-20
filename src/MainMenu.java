
/**
 * This class handles all menu interactions
 * It will also implement a mouse listener for interaction
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

class MainMenu implements MouseListener, MouseMotionListener {
	public static int block = GUI.block;
	public static boolean running = true;
	public static boolean hardMode = false; // hard mode makes all pieces white, removes ghosts, increases speed and
											// acceleration, and awards double points
	public static char musicSelect = 'A';

	// mouseListener methods
	public void mouseClicked(MouseEvent e) {
		if (running) {
			if (e.getY() >= block * 17 && e.getY() <= block * 21) {
				if (e.getX() >= block * 4 && e.getX() <= block * 16) // if the player clicks in the "PLAY" box
				{
					running = false; // stop running the menu
					Tetris.running = true; // run the game
				}
			}

			else if (e.getY() >= 9.5 * block && e.getY() <= 12 * block) {
				if (e.getX() >= 3.5 * block && e.getX() <= 7 * block) // if the player clicks in the "A" blox
				{
					if (musicSelect == 'A') {
						musicSelect = 'M'; // mute if unselect
					} else {
						musicSelect = 'A'; // set music
					}
				} else if (e.getX() >= 8.5 * block && e.getX() <= 12 * block) // elif the player clicks in the "B" blox
				{
					if (musicSelect == 'B') {
						musicSelect = 'M'; // mute if unselect
					} else {
						musicSelect = 'B'; // set music
					}
				} else if (e.getX() >= 13.5 * block && e.getX() <= 17 * block) // elif the player clicks in the "C" blox
				{
					if (musicSelect == 'C') {
						musicSelect = 'M'; // mute if unselect
					} else {
						musicSelect = 'C'; // set music
					}
				}
			}

			else if (e.getX() >= block * 8.5 && e.getX() <= block * 11.5 && e.getY() >= block * 23.5
					&& e.getY() <= block * 24.5) {
				hardMode = !hardMode;
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

	// mouseMotionListener methods
	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
	}

	// draw methods
	public static void drawBackground(Graphics g) {
		Tetris.drawTitle(g);
		drawMusicMenus(g);
		drawPlayMenu(g);
		drawHardMenu(g);
		drawCredits(g);
	}

	public static void drawMusicMenus(Graphics g) {
		for (int q = 0; q < 3; q++) // This loop draws three empty boxes
		{
			g.setColor(Color.gray);
			g.fillRect(q * (block * 5) + block * 3, block * 9, block * 4, (int) (block * .5)); // Top of box
			g.fillRect(q * (block * 5) + block * 3, block * 12, block * 4, (int) (block * .5)); // Bottom of box
			g.fillRect(q * (block * 5) + block * 3, block * 9, (int) (block * .5), block * 3); // Left of box
			g.fillRect(q * (block * 5) + block * 7, block * 9, (int) (block * .5), block * 3); // Right of box

			g.setColor(Color.DARK_GRAY);
			g.fillRect(q * (block * 5) + block * 7, (int) (block * 11.5), (int) (block * .5), block); // BR corner of
																										// box
			g.fillRect(q * (block * 5) + (int) (block * 6.5), block * 12, block, (int) (block * .5));

			g.setColor(Color.gray.brighter());
			g.fillRect(q * (block * 5) + block * 3, block * 9, block, (int) (block * .5)); // TL corner of box
			g.fillRect(q * (block * 5) + block * 3, block * 9, (int) (block * .5), block);

			g.setColor(Z.zColor); // Fill
			g.fillRect(q * (block * 5) + (int) (block * 3.5), (int) (block * 9.5), (int) (block * 3.5),
					(int) (block * 2.5));
		}

		// Fill selected box
		g.setColor(S.sColor);
		switch (musicSelect) {
		case 'A':
			g.fillRect((int) (block * 3.5), (int) (block * 9.5), (int) (block * 3.5), (int) (block * 2.5));
			break;
		case 'B':
			g.fillRect(block * 5 + (int) (block * 3.5), (int) (block * 9.5), (int) (block * 3.5), (int) (block * 2.5));
			break;
		case 'C':
			g.fillRect(2 * (block * 5) + (int) (block * 3.5), (int) (block * 9.5), (int) (block * 3.5),
					(int) (block * 2.5));
			break;
		default:
			g.setFont(new Font("TimesRoman", Font.BOLD, (int) (block * 1.5))); // set new font
			g.setColor(Color.white);
			g.drawString("(MUTED)", (int) (block * 7), (int) (block * 14.2));
		}

		g.setFont(new Font("TimesRoman", Font.BOLD, block * 3)); // set new font
		g.setColor(Color.black);
		g.drawString("A", (int) (block * 4.20), (int) (block * 11.75)); // draw in track names
		g.drawString("B", (int) (block * 9.20), (int) (block * 11.75));
		g.drawString("C", (int) (block * 14.20), (int) (block * 11.75));

		g.setFont(new Font("TimesRoman", Font.BOLD, block * 2)); // set new font
		g.setColor(Color.white);
		g.drawString("SELECT MUSIC", (int) (block * 2.75), (int) (block * 8.5));
	}

	public static void drawPlayMenu(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(block * 3, block * 16, block * 14, block); // Top of box
		g.fillRect(block * 3, block * 21, block * 14, block); // Bottom
		g.fillRect(block * 3, block * 16, block, block * 5); // Left
		g.fillRect(block * 16, block * 16, block, block * 5); // Right

		g.setColor(Color.DARK_GRAY); // BR Corner
		g.fillRect(block * 14, block * 21, block * 3, block);
		g.fillRect(block * 16, block * 19, block, block * 3);

		g.setColor(Color.gray.brighter()); // TL Corner
		g.fillRect(block * 3, block * 16, block * 3, block);
		g.fillRect(block * 3, block * 16, block, block * 3);

		g.setColor(Line.lineColor); // Fill
		g.fillRect(block * 4, block * 17, block * 12, block * 4);

		g.setFont(new Font("TimesRoman", Font.BOLD, block * 4)); // set new font
		g.setColor(Color.black);
		g.drawString("PLAY", (int) (block * 4.5), (int) (block * 20.5)); // PLAY text
	}

	public static void drawHardMenu(Graphics g) {
		g.setColor(Color.gray);
		g.fillRect(block * 8, block * 23, block * 4, (int) (block * .5)); // Top
		g.fillRect(block * 8, block * 23, (int) (block * .5), block * 2); // Left
		g.fillRect((int) (block * 11.5), block * 23, (int) (block * .5), block * 2); // Right
		g.fillRect(block * 8, (int) (block * 24.5), block * 4, (int) (block * .5)); // Bottom

		if (hardMode) {
			g.setColor(Color.red.brighter());
			g.fillRect(block * 8, block * 23, block, (int) (block * .5)); // TL
			g.fillRect(block * 8, block * 23, (int) (block * .5), block);

			g.setColor(Color.red.darker().darker());
			g.fillRect(block * 11, (int) (block * 24.5), block, (int) (block * .5)); // BR
			g.fillRect((int) (block * 11.5), block * 24, (int) (block * .5), block);

			g.setColor(Color.DARK_GRAY.darker());
			g.fillRect((int) (block * 8.5), (int) (block * 23.5), block * 3, block); // fill

			g.setFont(new Font("TimesRoman", Font.BOLD, (int) (block * .75))); // set new font
			g.setColor(Color.white);
			g.drawString("(>_<)", (int) (block * 9.2), (int) (block * 24.125));
		}

		else {
			g.setColor(Color.green.brighter());
			g.fillRect(block * 8, block * 23, block, (int) (block * .5)); // TL
			g.fillRect(block * 8, block * 23, (int) (block * .5), block);

			g.setColor(Color.cyan.darker());
			g.fillRect(block * 11, (int) (block * 24.5), block, (int) (block * .5)); // BR
			g.fillRect((int) (block * 11.5), block * 24, (int) (block * .5), block);

			g.setColor(Color.white);
			g.fillRect((int) (block * 8.5), (int) (block * 23.5), block * 3, block); // fill

			g.setFont(new Font("TimesRoman", Font.BOLD, (int) (block * .75))); // set new font
			g.setColor(Color.black);
			g.drawString("(n_n)", (int) (block * 9.2), (int) (block * 24.125));
		}
	}

	public static void drawCredits(Graphics g) {
		g.setColor(Color.white);
		g.setFont(new Font("Dialog", Font.PLAIN, (int) (block * 9 / 10)));
		g.drawString("I claim no credits!", (int) (block * .66), (int) (block * 24.25));
		g.setFont(new Font("Dialog", Font.PLAIN, (int) (block * 2 / 3)));
		g.drawString("Tetris was made by: ", block * 13, block * 23);
		g.drawString("Alexey Pajitnov", block * 13, block * 24); // Programmer
		g.drawString("Hirokazu Tanaka", block * 13, block * 25); // Composer
		g.setFont(new Font("Dialog", Font.BOLD, (int) (block * 4 / 3)));
		g.setColor(new Color(255, 235, 48)); // OLD GOLD
		g.drawString("Made by Ryan Gahagan", (int) (block * 2.5), (int) (block * 1.5));
	}
}
