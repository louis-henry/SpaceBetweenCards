package Core;

import java.awt.*;

// This class can be used to draw some stars in
// the background to give the impression of
// movement through space.

public class StarField
{
	private GameWindow gameWindow;
	private Toolkit toolkit;
	private int width;
	private int height;
	
	private Point[] stars;
	private Color[] starColours;
	private Color defaultColour = Color.WHITE;
   
   public StarField(GameWindow gameWindow)
   {
	   this.gameWindow = gameWindow;
	   toolkit = Toolkit.getDefaultToolkit();
	   width = toolkit.getScreenSize().width;
	   height = toolkit.getScreenSize().height;
	   
	   stars = new Point[600];
	   starColours = new Color[stars.length];
	   
	   for(int i = 0; i < stars.length; i++)
	   {
		 stars[i] = new Point((int)(Math.random() * width),
		    (int)(Math.random() * height));
		 starColours[i] = new Color((int)(Math.random() * 256),
		    (int)(Math.random() * 256),
		    (int)(Math.random() * 256));
	   }
   }
   
   void drawStarField(Graphics screen)
   {
      width = toolkit.getScreenSize().width;
	  height = toolkit.getScreenSize().height;
	   
      for(int i = 0; i < stars.length; i++)
	  {
    	 screen.setColor(starColours[i]);
    	 screen.drawLine(stars[i].x, stars[i].y, stars[i].x, stars[i].y);
		 stars[i].y += (i % 4) + 1;
			
		 if(stars[i].y >= height)
		 {
			stars[i].x = (int)(Math.random() * width);
			stars[i].y = 0;
			starColours[i] = new Color((int)(Math.random() * 256),
			   (int)(Math.random() * 256),
			   (int)(Math.random() * 256));
		 }
	  }
      
      screen.setColor(defaultColour);
   }
}
