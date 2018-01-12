/*
 *Ryan Gahagan
 *This is the GUI which handles Tetris events
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.BorderLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

import sun.audio.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class GUI extends JFrame
{
	private static JPanel myPanel;		//Creates panel for game
	public static int block = EnterIntGUI("How many pixels do you want in each box? (Type either 32, 40, or 48)");		//Gets user input for screen size
	public static Tetris game = new Tetris();  //handler class used to handle inputs
	public static MainMenu startMenu = new MainMenu();  //handler class initially
	public static JFrame jf;	//Create frame

    public GUI()
    {
    	super("Tetris"); //creates a GUI Window with title "Tetris"

    	myPanel = new JPanel() {
    		protected void paintComponent(Graphics g)
    		{
    			//Draw backgrounds
    			super.paintComponent(g);

    			g.clearRect(0,0,getWidth(),getHeight());   //clear background
    			g.setColor(Color.black);
    			g.fillRect(0,0,getWidth(),getHeight());		//draw black box

    			if(MainMenu.running)
    			{
    				MainMenu.drawBackground(g); //draw the menu before playing
    			}
    			else
    			{
    				Tetris.drawBackground(g);  //draw the tetris game while playing
    			}
    		}
    	};

    	myPanel.setBackground(Color.BLACK); //sets color to white
    	add(myPanel, BorderLayout.CENTER); //puts the panel onto the frame

    	myPanel.addMouseListener(startMenu);		//adds the mouse listners to the panel
    	myPanel.addMouseMotionListener(startMenu);

    	myPanel.addKeyListener(game);  //adds a key listener to the panel
    	myPanel.addMouseListener(game); //puts a key listener into the game itself
    	myPanel.setFocusable(true);  //allows the user to "focus" on the panel
    	setFocusTraversalKeysEnabled(true);
    }

    public static void main(String[] args)  throws Exception
    {
        jf = new GUI();  //instantiates the frame
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the x

        //Note: The visible tetris field is 20 rows x 10 columns
        //This size has extra room for hold, score, etc.
        jf.setSize(block*20+15, block*25+60); //sets the size of the frame
        jf.setVisible(true);  //makes the frame visible

        Tetris.fillGrid();	//Initially fills the color array with black (empty) spaces

        while(MainMenu.running)  //wait until the player clicks play
    	{
    		try
    		{
    			Thread.sleep(50);
    		}
    		catch(Exception e)
    		{
    			System.out.println("NO");
    		}
    	}

        AudioInputStream inputStream;   //declare audio variables
        Clip clip = AudioSystem.getClip();

        switch(MainMenu.musicSelect)		//get the chosen music
        {
        	//in each case, read in the file, open the music
        	case 'A':
        		inputStream = AudioSystem.getAudioInputStream(new File("aTheme.wav"));
		    	clip.open(inputStream);
		    	clip.setLoopPoints(0, 1701850);
		    	break;
		   	case 'B':
		   		inputStream = AudioSystem.getAudioInputStream(new File("bTheme.wav"));
		   		clip.open(inputStream);
		   		clip.setLoopPoints(26915, 1586555);
		   		break;
		   	case 'C':
		   		inputStream = AudioSystem.getAudioInputStream(new File("cTheme.wav"));
		   		clip.open(inputStream);
		   		clip.setLoopPoints(13220, 1715000);
		   		break;
		   	case 'M':
		   		//no music
		   		break;
		    default:
		    	System.out.println("ERROR");
        }

        Tetris.start = System.nanoTime() / 1000000;  //reset the timer in game
        Tetris.placeTimer = Tetris.start;
        Tetris.curTetromino.draw();					//draw the first tetromino

        if(MainMenu.hardMode)  //if the player is in hard mode
        {
        	Tetris.level = 9;
        	Tetris.delay = 24 * Tetris.frame;
        	Tetris.nextLines = 50;
        }
		
		while(true)
		{
			if(MainMenu.musicSelect != 'M')
	        {
	        	clip.loop(Clip.LOOP_CONTINUOUSLY);  //loop the music forever
	        }
	        
			while(Tetris.running == true)	//While game is running
			{
				try
	    		{
	    			Thread.sleep(2);
	    		}
	    		catch(Exception e)
	    		{
	    			System.out.println("NO");
	    		}
	
				Tetris.play();		//Play game
			}
	
			if(MainMenu.musicSelect != 'M')
			{
				AudioInputStream as = AudioSystem.getAudioInputStream(new File("gameOver.wav"));  //read in the game over sound
				clip.close();
				Clip clip2 = AudioSystem.getClip();
				clip2.open(as);
				clip2.loop(0);  //play the "game over" sound once
			}
			
			while(!Tetris.running)
			{
				try
	    		{
	    			Thread.sleep(100);
	    		}
	    		catch(Exception e)
	    		{
	    			System.out.println("NO");
	    		}
			}
			
			switch(MainMenu.musicSelect)		//get the chosen music
	        {
	        	//in each case, read in the file, open the music
	        	case 'A':
	        		inputStream = AudioSystem.getAudioInputStream(new File("aTheme.wav"));
			    	clip.open(inputStream);
			    	clip.setLoopPoints(0, 1701850);
			    	break;
			   	case 'B':
			   		inputStream = AudioSystem.getAudioInputStream(new File("bTheme.wav"));
			   		clip.open(inputStream);
			   		clip.setLoopPoints(26915, 1586555);
			   		break;
			   	case 'C':
			   		inputStream = AudioSystem.getAudioInputStream(new File("cTheme.wav"));
			   		clip.open(inputStream);
			   		clip.setLoopPoints(13220, 1715000);
			   		break;
			   	case 'M':
			   		//no music
			   		break;
			    default:
			    	System.out.println("ERROR");
	        }
		}
}

    /*
     *Graphics g refers to frame graphics, which inclueds the perimeter
     *myPanel.getGraphics() refers only to the screen
     *The latter will be used more often to avoid math.
     */
    public void paint(Graphics g)
    {
    	myPanel.repaint();
    }

    //Uses JOptionPanes to retrieve an integer
    public static int EnterIntGUI(String prompt)
    {
    	String tempString = JOptionPane.showInputDialog(prompt);
    	int temp = Integer.parseInt(tempString);
    	return temp;
    }

    //Allows access to the panel if necessary
    public static JPanel getMyPanel(){return myPanel;}
}