package Core;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class HighScoresScreen extends JPanel
{
	private ResourceManager resourceManager;
	private JFrame theGameFrame;
	private BufferedImage background;
	private int width;
	private int height;
	
	private Box highScores;
	private JLabel highScoreTitle;
    private JLabel[] highScoreLabels;
	private JButton mainMenu;
	
	private String[] highScoreNames;
	private int[] highScoreValues;
	
	private boolean fromEndGameScreen;
	private String newInitials;
	private int newHighScore;
	
	public HighScoresScreen(ResourceManager resourceManager, int width, int height)
	{
		this.resourceManager = resourceManager;
		theGameFrame = resourceManager.getGameFrame();
		this.width = width;
		this.height = height;
		highScoreLabels = new JLabel[10];
		highScoreNames = new String[10];
		highScoreValues = new int[10];
		fromEndGameScreen = false;
		
		try
		{
			background = ImageIO.read(new File("Backgrounds/high_scores.jpg"));
			
		} catch (Exception ex)
		{
			System.out.println("Failed to load background - check folder.");
		}
		
		// First load data from the high_scores.txt file.
		getHighScores();
		// Then create labels with the data.
		addHighScores();
	}
	
	// Second constructor is used when moving from end game screen after
	// obtaining a new high score.
	public HighScoresScreen(ResourceManager resourceManager, int width, int height,
       boolean fromEndGameScreen, String initials, int newHighScore)
	{
		this.resourceManager = resourceManager;
		theGameFrame = resourceManager.getGameFrame();
		this.width = width;
		this.height = height;
		highScoreLabels = new JLabel[10];
		highScoreNames = new String[10];
		highScoreValues = new int[10];
		this.fromEndGameScreen = fromEndGameScreen;
		newInitials = initials;
		this.newHighScore = newHighScore;
		
		try
		{
			background = ImageIO.read(new File("Backgrounds/high_scores.jpg"));
			
		} catch (Exception ex)
		{
			System.out.println("Failed to load background - check folder.");
		}
		
		// First load data from the high_scores.txt file.
		getHighScores();
		
		// Re-tally the high scores including the new value and save to file.
		saveHighScores();
		
		// Then create labels with the data.
		addHighScores();
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
		
		
		// Reconfigure the lables and button so that they will be painted
		// when the window is resized.
		paintHighScores();
	}
	
	public void paintHighScores()
	{
		highScores.removeAll();
		highScores.add(Box.createVerticalStrut(height / 8));
		
		if(!fromEndGameScreen)
		{
			highScores.add(mainMenu);
			mainMenu.repaint();
			highScores.add(Box.createVerticalStrut(height / 40));
		}
		
		highScores.add(highScoreTitle);
		highScoreTitle.repaint();
		highScores.add(Box.createVerticalStrut(height / 40));
		
		if(highScoreNames[0] == null)
		{
			highScores.add(highScoreLabels[0]);
			highScoreLabels[0].repaint();
		} else
		{
			for(int i = 0; i < highScoreLabels.length; i++)
			{
				highScores.add(highScoreLabels[i]);
				highScoreLabels[i].repaint();
				highScores.add(Box.createVerticalStrut(height / 40));
			}
		}
	}
	
	// This method creates a box and lists the high score data that
	// has been loaded from the high_score.txt file.
	public void addHighScores()
	{
		highScores = Box.createVerticalBox();
		highScores.add(Box.createVerticalStrut(height / 8));
		
		// Add the return button
		if(!fromEndGameScreen) setupButton();
		
		// Label for the title of the page
		highScoreTitle = new JLabel("High Scores");
		MenuStyle.styleLabel(highScoreTitle);
		highScoreTitle.setForeground(Color.YELLOW);
		highScoreTitle.setFont(new Font("Arial", Font.BOLD, 40));
		highScores.add(highScoreTitle);
		highScores.add(Box.createVerticalStrut(height / 40));
		
		// Each entry comes from the high score data that has been
		// loaded. If the data was not loaded successfully then send
		// an error message to the screen.
		if(highScoreNames[0] == null)
		{
			highScoreLabels[0] =
		       new JLabel("Failed to load high scores - check folder.");
			MenuStyle.styleLabel(highScoreLabels[0]);
			highScores.add(highScoreLabels[0]);
		} else
		{
			// Can now create each label with the data.
			for(int i = 0; i < highScoreLabels.length; i++)
			{
				String content = highScoreNames[i] + "           " +
			       highScoreValues[i];
				highScoreLabels[i] = new JLabel(content);
				MenuStyle.styleLabel(highScoreLabels[i]);
				highScores.add(highScoreLabels[i]);
				highScores.add(Box.createVerticalStrut(height / 40));
			}
		}
		
		add(highScores);
	}
	
	// Sets up a return to main menu button.
	public void setupButton()
	{
		mainMenu = new JButton("Main Menu");
		MenuStyle.styleButton(mainMenu);
		
		mainMenu.addActionListener(e ->
		{
			this.setVisible(false);
			theGameFrame.add(new TitleScreen(resourceManager, width, height));
		});
		
		highScores.add(mainMenu);
		highScores.add(Box.createVerticalStrut(height / 40));
		
	}
	
	// This method is used to load high score data from the high_scores.txt
	// file. The file must have entries for 10 players (name and score)
	// and uses a comma as a delimiter.
	private void getHighScores()
	{
		String loadData = "";
		
		try
		{
			File file = new File("Data/high_scores.txt");
			Scanner scanner = new Scanner(file);
			loadData = scanner.nextLine();
			
			scanner.close();
			
			String[] data = loadData.split(",");
			
			// Throw exception if data for 10 players was not present
			// in the file.
			if(data.length != 20) throw new Exception();
			for(int i = 0; i < highScoreNames.length; i++)
			{
				highScoreNames[i] = data[i * 2];
				// Convert text data to an integer.
				highScoreValues[i] = Integer.parseInt(data[i * 2 + 1]);
			}
			
		} catch(Exception ex)
		{
			System.out.println("Failed to load high scores - check folder.");
		}
	}
	
	public void saveHighScores()
	{
		if(highScoreNames[0] == null)
		{
			System.out.println("Cannot add new high score - check folder.");
		} else
		{
			// First obtain the new ranking.
			int rank = 0;
			boolean ranked = false;
			for(int i = 0; i < highScoreValues.length && !ranked; i++)
			{
				if(newHighScore >= highScoreValues[i])
				{
					ranked = true;
				} else
				{
					rank ++;
				}
			}
			
			// Now include the new score and push down all other scores
			// that are equal to or lower.
			String tempName = highScoreNames[rank];
			int tempValue = highScoreValues[rank];
			highScoreNames[rank] = newInitials;
			highScoreValues[rank] = newHighScore;
			for(int i = rank + 1; i < highScoreValues.length; i++)
			{
				String newTempName = highScoreNames[i];
				int newTempValue = highScoreValues[i];
				highScoreNames[i] = tempName;
				highScoreValues[i] = tempValue;
				tempName = newTempName;
				tempValue = newTempValue;
			}
			
			// Now save the new high score list to high_scores.txt
			String saveData = "";
			for(int i = 0; i < highScoreValues.length; i++)
			{
				saveData += highScoreNames[i] + ",";
				saveData += highScoreValues[i];
				if(i < highScoreValues.length - 1) saveData += ",";
			}
			saveData += "\n";
			
			try
			{
				File file = new File("Data/high_scores.txt");
				PrintWriter printWriter = new PrintWriter(file);
				printWriter.println(saveData);
				printWriter.close();
				
			}catch (Exception ex)
			{
			   System.out.println("Cannot add new high score - check folder.");
			}
		}
	}
}
