package Core;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite {
	// This is a stored image that will be displayed at certain locations on
	// request.
	private BufferedImage img;
	private String name;

	public Sprite(BufferedImage img, String name) {
		this.img = img;
		this.name = name;
		Graphics2D graphic = img.createGraphics();
		AlphaComposite tranparencyComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
		graphic.setComposite(tranparencyComposite);
		
	}

	public void paint(Graphics2D screen, int xLocation, int yLocation, int height, int width) {
		// This method paints the sprite at the chosen location.
		
		// Draw it
		screen.drawImage(this.img, xLocation, yLocation, width, height, null);
	}
	
	//This method paints an image at a preset transparency
	public void paintTransparent(Graphics2D screen, int xLocation, int yLocation, int height, int width, float alpha) {
		screen.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		screen.drawImage(this.img, xLocation, yLocation, width, height, null);
		screen.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
	}

	public String getName() {
		return this.name;
	}

}
