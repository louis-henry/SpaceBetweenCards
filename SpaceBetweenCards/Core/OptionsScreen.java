package Core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

// This class sets up an options screen for the user.
// Options available connect with variables in the
// GameOptions class.
public class OptionsScreen extends JPanel
{
   private ResourceManager resourceManager;
   private boolean fromTitleScreen;
   private MusicClips backGround;
   private GameOptions gameOptions;
   private JFrame theGameFrame;
   private BufferedImage background;
   private int width;
   private int height;
   
   private JButton returnButton;
   
   private Box optionsBox;
   private JLabel optionsTitle;
   private JLabel volumeLabel;
   private JSlider volumeSlider;
   private int sliderValue;
   
   private JLabel seedLabel1;
   private JLabel seedLabel2;
   private JButton saveSeed;
   private JLabel saveStatus;
   
   public OptionsScreen(ResourceManager resourceManager, int width, int height,
      MusicClips backGround, boolean fromTitleScreen)
   {
	    this.fromTitleScreen = fromTitleScreen;
		this.resourceManager = resourceManager;
		this.backGround = backGround;
		gameOptions = this.resourceManager.getGameOptions();
		theGameFrame = resourceManager.getGameFrame();
		this.width = width;
		this.height = height;
		setBackground(Color.BLACK);
		
		try
		{
			background = ImageIO.read(new File("Backgrounds/options.jpg"));
			
		} catch (Exception ex)
		{
			System.out.println("Failed to load background - check folder.");
		}
		
		setupOptions();
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
		
		paintOptions();
   }
   
   public void paintOptions()
   {
	   optionsBox.removeAll();
	   optionsBox.add(Box.createVerticalStrut(height / 16));
	   
	   optionsBox.add(returnButton);
	   returnButton.repaint();
	   optionsBox.add(Box.createVerticalStrut(height / 8));
	   optionsBox.add(optionsTitle);
	   optionsTitle.repaint();
	   optionsBox.add(Box.createVerticalStrut(height / 8));
	   optionsBox.add(volumeLabel);
	   volumeLabel.repaint();
	   optionsBox.add(volumeSlider);
	   volumeSlider.repaint();
	   optionsBox.add(Box.createVerticalStrut(height / 16));
	   optionsBox.add(seedLabel1);
	   seedLabel1.repaint();
	   optionsBox.add(seedLabel2);
	   seedLabel2.repaint();
	   optionsBox.add(saveSeed);
	   optionsBox.add(saveStatus);
	   saveStatus.repaint();
	   saveSeed.repaint();
	   
   }
   
   public void setupOptions()
   {
	   optionsBox = Box.createVerticalBox();
	   optionsBox.add(Box.createVerticalStrut(height / 16));
	   
	   setupButton();
	   optionsBox.add(Box.createVerticalStrut(height / 8));
	   
	   optionsTitle = new JLabel("Options");
	   MenuStyle.styleLabel(optionsTitle);
	   optionsBox.add(optionsTitle);
	   optionsBox.add(Box.createVerticalStrut(height / 8));
	   
	   volumeLabel = new JLabel("Volume");
	   MenuStyle.styleLabel(volumeLabel);
	   volumeLabel.setForeground(Color.BLACK);
	   volumeLabel.setBackground(Color.LIGHT_GRAY);
	   optionsBox.add(volumeLabel);
	   
	   sliderValue = (int)(gameOptions.getVolume() * 5);
	   volumeSlider = new JSlider(0, 10, sliderValue);
	   volumeSlider.setName("Volume");
	   volumeSlider.setMinorTickSpacing(1);
	   volumeSlider.setPaintTicks(true);
	   volumeSlider.setSnapToTicks(true);
	   volumeSlider.addChangeListener(e ->
	   {
		   sliderValue = volumeSlider.getValue();
		   gameOptions.setVolume(sliderValue / 5.0);
		   MusicClips.setVol(0.15 * gameOptions.getVolume(),
		      backGround.getClip());
	   });
	   optionsBox.add(volumeSlider);
	   optionsBox.add(Box.createVerticalStrut(height / 16));
	   
	   seedLabel1 = new JLabel("Save the seed to recreate");
	   MenuStyle.styleLabel(seedLabel1);
	   seedLabel1.setForeground(Color.BLACK);
	   seedLabel1.setBackground(Color.LIGHT_GRAY);
	   optionsBox.add(seedLabel1);
	   seedLabel2 = new JLabel("game conditions later");
	   MenuStyle.styleLabel(seedLabel2);
	   seedLabel2.setForeground(Color.BLACK);
	   seedLabel2.setBackground(Color.LIGHT_GRAY);
	   optionsBox.add(seedLabel2);
	   saveSeed = new JButton("Save Seed");
	   MenuStyle.styleButton(saveSeed);
	   
	   saveSeed.addActionListener(e ->
	   {
	      if(gameOptions.saveSeed())
	      {
	    	  saveStatus.setText("Saved current seed");
	      } else
	      {
	    	  saveStatus.setText("Error could not save");
	      }
	   });
	   
	   optionsBox.add(saveSeed);
	   saveStatus = new JLabel("");
	   MenuStyle.styleLabel(saveStatus);
	   saveStatus.setForeground(Color.BLACK);
	   saveStatus.setBackground(Color.LIGHT_GRAY);
	   
	   add(optionsBox);
   }
   
   public void setupButton()
   {
      returnButton = new JButton("Return");
	  MenuStyle.styleButton(returnButton);
		
	  returnButton.addActionListener(e ->
	  {
	     this.setVisible(false);
	     if(fromTitleScreen)
	     {
	    	gameOptions.saveOptions();
	        theGameFrame.add(new TitleScreen(resourceManager, width, height));
	     } else
	     {
	        resourceManager.setResumeGame(true);
	     }
	  });
		
	  optionsBox.add(returnButton);
   }
}
