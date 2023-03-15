package Core;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class CreditsScreen extends JPanel
{
	private ResourceManager resourceManager;
	private JFrame theGameFrame;
	private BufferedImage background;
	private int width;
	private int height;
	
	private Box buttonLayout;
	private JButton mainMenu;
	
	
	public CreditsScreen(ResourceManager resourceManager, int width, int height)
	{
		this.resourceManager = resourceManager;
		theGameFrame = resourceManager.getGameFrame();
		this.width = width;
		this.height = height;
		
		try
		{
			background = ImageIO.read(new File("Backgrounds/credits.jpg"));
			
		} catch (Exception ex)
		{
			System.out.println("Failed to load background - check folder.");
		}
		
		setupButton();
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
		
		paintButton();
	}
	
	public void paintButton()
	{
		buttonLayout.removeAll();
		buttonLayout.add(Box.createVerticalStrut(height * 5 / 6));
		buttonLayout.add(mainMenu);
		mainMenu.repaint();
	}
	
	public void setupButton()
	{
		mainMenu = new JButton("Main Menu");
		MenuStyle.styleButton(mainMenu);
		
		mainMenu.addActionListener(e ->
		{
			this.setVisible(false);
			theGameFrame.add(new TitleScreen(resourceManager, width, height));
		});
		
		buttonLayout = Box.createVerticalBox();
		buttonLayout.add(Box.createVerticalStrut(height * 5 / 6));
		buttonLayout.add(mainMenu);
		add(buttonLayout);
		
	}
}
