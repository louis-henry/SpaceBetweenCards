package Core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class EndGameScreen extends JPanel implements KeyListener
{
	private ResourceManager resourceManager;
	private JFrame theGameFrame;
	private BufferedImage background;
	private int width;
	private int height;
	
	private Box gameOverBox;
	private boolean newHighScore;
	private int highScoreVal;
	private boolean highScoresUpdated;
	private char[] charInitials;
	private int charPointer;
	private String initials;
	private JLabel gameOver;
	private JLabel message1;
	private JLabel message2;
	
	private String chosenMessage1;
	private String chosenMessage2;
	private String[] messages =
    {"You ejected from warp drive too early and your crew's organic",
     "material has been scattered over 2 million parsecs",
     "Your engine officer just ate a depleted uranium sandwich",
     "",
     "Paranioa inducing gas valve opened: the crew have attacked", 
     "and devoured each other in a cannibalistic frenzy",
     "Hull breach confirmed: the vacuum of space has depleted",
     "your oxygen supply, leaving you speechless",
     "Fuel pod ignited: the residents of Alpha Centauri mistook",
     "your ship for a fireworks display"
    };
	
	public EndGameScreen(ResourceManager resourceManager, int width, int height,
	   boolean newHighScore)
	{
		this.resourceManager = resourceManager;
		theGameFrame = resourceManager.getGameFrame();
		theGameFrame.addKeyListener(this);
		this.width = width;
		this.height = height;
		this.newHighScore = newHighScore;
		highScoresUpdated = false;
		
		try
		{
			background = ImageIO.read(new File("Backgrounds/endgame.jpg"));
			
		} catch (Exception ex)
		{
			System.out.println("Failed to load background - check folder.");
		}
		
		//High Score scenario
		if(newHighScore)
		{
			// highScoreVal taken from the score in the resource manager.
			highScoreVal = resourceManager.getScore();
			chosenMessage1 = "We have a new high score!";
			chosenMessage2 = "Please enter your initials: ";
			charPointer = 0;
			charInitials = new char[3];
			charInitials[0] = ' ';
			charInitials[1] = ' ';
			charInitials[2] = ' ';
			initials = String.valueOf(charInitials[0]) + String.valueOf(charInitials[1])
			   + String.valueOf(charInitials[2]);
		} else
		{
			// Set up a random choice for the game over message.
			int choice = (int)(Math.random() * 5) * 2;
			chosenMessage1 = messages[choice];
			chosenMessage2 = messages[choice + 1];
			
			resourceManager.setResumeGame(true);
		}

		displayGameOverMessage();
	}
	
	public void paint(Graphics screen)
	{
		width = this.getWidth();
		height = this.getHeight();
		if(background != null)
		{
			
			screen.drawImage(background, 0, 0, width, height, null);
		} else
		{
			screen.setFont(new Font("Arial", Font.BOLD, 20));
			screen.drawString("Failed to load background image", width / 2 - 150,
		       100);
		}
		
		if(highScoresUpdated)
		{
			
		} else
		{
			paintMessage();
			
			if(newHighScore) paintInitials(screen);
		}
	}
	
	public void paintMessage()
	{
		gameOverBox.removeAll();
		gameOverBox.add(Box.createVerticalStrut(100));
		
		gameOverBox.add(gameOver);
		gameOver.repaint();
		gameOverBox.add(Box.createVerticalStrut(height / 40));
		
		gameOverBox.add(message1);
		message1.repaint();
		gameOverBox.add(message2);
		message2.repaint();
		gameOverBox.add(Box.createVerticalStrut(height / 40));
	}
	
	public void paintInitials(Graphics screen)
	{
		screen.setColor(Color.BLACK);
		screen.fillRect(width / 2 - 150, height / 2 - 50, 300, 100);
		
		screen.setColor(Color.CYAN);
		screen.setFont(new Font("Arial", Font.BOLD, 70));
		
		screen.drawString(initials, width / 2 - 70, height / 2 + 20);
		screen.drawLine(width / 2 - 70, height / 2 + 25, width / 2 + 80,
	       height / 2 + 25);
	}
	
	public void displayGameOverMessage()
	{
		gameOverBox = Box.createVerticalBox();
		gameOverBox.add(Box.createVerticalStrut(100));
			
		gameOver = new JLabel("G A M E   O V E R");
		MenuStyle.styleLabel(gameOver);
		gameOver.setForeground(Color.YELLOW);
		gameOver.setFont(new Font("Arial", Font.BOLD, 40));
		gameOverBox.add(gameOver);
		gameOverBox.add(Box.createVerticalStrut(height / 40));
		
		message1 = new JLabel(chosenMessage1);
		MenuStyle.styleLabel(message1);
		gameOverBox.add(message1);
		message2 = new JLabel(chosenMessage2);
		MenuStyle.styleLabel(message2);
		gameOverBox.add(message2);
		gameOverBox.add(Box.createVerticalStrut(height / 40));
		
		add(gameOverBox);
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		if(newHighScore)
		{
			int keyNumber = Character.getNumericValue(e.getKeyChar());
			
			if(keyNumber == -1)
			{
				// Enter or escape key was chosen - resume game (after another pause)
				resourceManager.setResumeGame(true);
				
				//Switch to high score screen to show updated values.
				setVisible(false);
				theGameFrame.add(new HighScoresScreen(resourceManager, width,
				   height, true, initials, highScoreVal));
				theGameFrame.setVisible(true);
				
			} else if(charPointer == 0 && keyNumber >= 10 && keyNumber <= 36)
			{
				charInitials[0] = Character.toUpperCase(e.getKeyChar());
				initials = String.valueOf(charInitials[0]) + String.valueOf(charInitials[1])
				   + String.valueOf(charInitials[2]);
				charPointer ++;
				repaint();
			} else if(charPointer == 1 && keyNumber >= 10 && keyNumber <= 36)
			{
				charInitials[1] = Character.toUpperCase(e.getKeyChar());
				initials = String.valueOf(charInitials[0]) + String.valueOf(charInitials[1])
				   + String.valueOf(charInitials[2]);
				charPointer ++;
				repaint();
			} else if(charPointer == 2 && keyNumber >= 10 && keyNumber <= 36)
			{
				charInitials[2] = Character.toUpperCase(e.getKeyChar());
				initials = String.valueOf(charInitials[0]) + String.valueOf(charInitials[1])
				   + String.valueOf(charInitials[2]);
				repaint();
				// Can resume loop (after a pause)
				resourceManager.setResumeGame(true);
				
				//Switch to high score screen to show updated values.
				setVisible(false);
				theGameFrame.add(new HighScoresScreen(resourceManager, width,
				   height, true, initials, highScoreVal));
				theGameFrame.setVisible(true);
			}
		}
	}
}
