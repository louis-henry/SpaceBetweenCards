package Core;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TitleScreen extends JPanel
{
	private ResourceManager resourceManager;
	private JFrame theGameFrame;
	private BufferedImage background;
	private int width;
	private int height;
	
	private Box buttons;
	private JButton startGame;
	private JButton loadGame;
	private JButton highScores;
	private JButton options;
	private JButton credits;
	private JButton quitGame;
	
	private HighScoresScreen highScoresScreen;
	
	public TitleScreen(ResourceManager resourceManager, int width, int height)
	{
		this.resourceManager = resourceManager;
		theGameFrame = resourceManager.getGameFrame();
		this.width = width;
		this.height = height;
		setBackground(Color.BLACK);
		
		try
		{
			background = ImageIO.read(new File("Backgrounds/title_screen.jpg"));
			
		} catch (Exception ex)
		{
			System.out.println("Failed to load background - check folder.");
			
		}
		
		createButtons();
	}
	
	public void paint(Graphics screen)
	{
		if(background != null)
		{
			width = this.getWidth();
			height = this.getHeight();
			screen.drawImage(background, 0, 0, width, height, null);
		} else
		{
			screen.setFont(new Font("Arial", Font.BOLD, 20));
			screen.drawString("Failed to load background image", width / 2 - 150,
		       100);
		}
		
		paintButtons();
	}
	
	public void paintButtons()
	{
		buttons.removeAll();
		buttons.add(Box.createVerticalStrut(height / 4));
		buttons.add(startGame);
		startGame.repaint();
		buttons.add(Box.createVerticalStrut(height / 24));
		buttons.add(highScores);
		highScores.repaint();
		buttons.add(Box.createVerticalStrut(height / 24));
		buttons.add(options);
		options.repaint();
		buttons.add(Box.createVerticalStrut(height / 24));
		buttons.add(credits);
		credits.repaint();
		buttons.add(Box.createVerticalStrut(height / 24));
		buttons.add(quitGame);
		quitGame.repaint();
	}
	
	// Buttons for the title screen - some extra buttons may be included
	// later if more functionality becomes available - load game and options.
	public void createButtons()
	{
		buttons = Box.createVerticalBox();
		buttons.add(Box.createVerticalStrut(height / 4));
		
		startGame = new JButton("Start Game");
		MenuStyle.styleButton(startGame);
		startGame.addActionListener(e ->
		{
			this.setVisible(false);
			resourceManager.switchToGameWindow();
		});
		buttons.add(startGame);
		buttons.add(Box.createVerticalStrut(height / 24));
		
		/*loadGame = new JButton("Load Game");
		MenuStyle.styleButton(loadGame);
		loadGame.addActionListener(e ->
		{
			System.out.println("Load Game pressed");
		});
		buttons.add(loadGame);
		buttons.add(Box.createVerticalStrut(height / 20));
		*/
		
		highScores = new JButton("High Scores");
		MenuStyle.styleButton(highScores);
		highScores.addActionListener(e ->
		{
			this.setVisible(false);
			theGameFrame.add(new HighScoresScreen(resourceManager, width, height));
		});
		buttons.add(highScores);
		buttons.add(Box.createVerticalStrut(height / 24));
		
		options = new JButton("Options");
		MenuStyle.styleButton(options);
		options.addActionListener(e ->
		{
			setVisible(false);
			theGameFrame.add(new OptionsScreen(resourceManager,
		       theGameFrame.getWidth(), theGameFrame.getHeight(),
		       resourceManager.getBackGround(), true));
		});
		buttons.add(options);
		buttons.add(Box.createVerticalStrut(height / 24));
		
		credits = new JButton("Credits");
		MenuStyle.styleButton(credits);
		credits.addActionListener(e ->
		{
			this.setVisible(false);
			theGameFrame.add(new CreditsScreen(resourceManager, width, height));
		});
		buttons.add(credits);
		buttons.add(Box.createVerticalStrut(height / 24));
		
		quitGame = new JButton("Quit Game");
		MenuStyle.styleButton(quitGame);
		quitGame.addActionListener(e ->
		{
			System.exit(0);
		});
		buttons.add(quitGame);
		buttons.add(Box.createVerticalStrut(height / 24));
		
		add(buttons);
	}
}
