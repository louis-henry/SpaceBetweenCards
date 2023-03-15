package Core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameWindow extends JPanel {
	private PlayField theField;
	private ArrayList<Sprite> spriteList;
	// private ArrayList<Card> theHand;
	private Hand theHand;
	private String bottomLeftText;
	private String folderName;
	private int bottomPercentageAllocated;
	private int rightPercentageAllocated;
	private double cardRatio;
	private StarField starField;

	public GameWindow(PlayField theField, ArrayList<Sprite> spriteList, Hand theHand, String bottomLeftText) {
		// This creates a new window, passing the things that need to be displayed by
		// reference
		this.theField = theField;
		this.spriteList = spriteList;
		this.theHand = theHand;
		this.cardRatio = 2;
		this.bottomPercentageAllocated = 20;
		this.rightPercentageAllocated = 20;
		this.bottomLeftText = bottomLeftText;

		folderName = "Sprites/";
		// Load the default sprite at space 0
		loadNewSprite("Strawberry.jpg");

		// Creating a star field pattern to be drawn with the
		// background for the play field
		starField = new StarField(this);
	}

	public void paint(Graphics Gscreen) {
		// This method is called by the system, but can be called by the resource
		// manager via the "repaint" method

		// Change the graphics object to be 2D, in order to do composites for
		// transparency
		Graphics2D screen = (Graphics2D) Gscreen;

		// Set variables
		int bottomSpacePixels = this.getHeight() * bottomPercentageAllocated / 100;
		int rightSpacePixels = this.getWidth() * rightPercentageAllocated / 100;
		// Make the left offset the same as the right offset,
		// so that the hand is in the middle
		int leftSpacePixels = rightSpacePixels;
		// Make the top space allocated to the enemy deck the same as the bottom space
		// allocated to the player deck, for symmetry
		int topSpacePixels = bottomSpacePixels;
		// Set the spacing around the bolts for the plating. This number is from 0 -
		// 100, with 100 showing nothing at all
		int cardSpacing = 10;

		// Set the screen font
		screen.setFont(new Font("Game Font", Font.PLAIN, 24));

		// Clear the screen with black
		screen.setColor(Color.BLACK);
		screen.fillRect(0, 0, this.getWidth(), this.getHeight());

		// Draw stars on the background before other objects
		starField.drawStarField(screen);

		paintBackGround(screen, bottomSpacePixels, topSpacePixels, leftSpacePixels, rightSpacePixels);
		// paintCards(screen);

		// Render the player deck in the bottom left corner
		paintPlayerDeck(screen, 0, this.getHeight() - bottomSpacePixels, leftSpacePixels,
				(95 * bottomSpacePixels) / 100);

		// Render the discard pile in the bottom right corner
		paintPlayerDiscardDeck(screen, this.getWidth() - rightSpacePixels, this.getHeight() - bottomSpacePixels,
				rightSpacePixels, bottomSpacePixels);

		// Render the hand in between the two, with the space left over
		paintPlayerHand(screen,
				leftSpacePixels + (this.getWidth() - leftSpacePixels - rightSpacePixels) * cardSpacing / 100,
				this.getHeight() - ((100 - cardSpacing) * bottomSpacePixels) / 100,
				(this.getWidth() - leftSpacePixels - rightSpacePixels) * (100 - 2 * cardSpacing) / 100,
				((100 - 2 * cardSpacing) * bottomSpacePixels) / 100);

		// Render the enemy deck in the top right corner
		paintEnemyDeck(screen, this.getWidth() - rightSpacePixels, 0, rightSpacePixels, topSpacePixels);

		// Also render score in the top right area of the plating.
		paintScore(screen, this.getWidth() - rightSpacePixels + 20, topSpacePixels, rightSpacePixels - 40, topSpacePixels / 2);
		
		// Render the player field in the top left corner
		paintField(screen, 0, 0, getWidth() - rightSpacePixels, getHeight() - bottomSpacePixels);
	}

	private void paintBackGround(Graphics2D Screen, int bottomSpacePixels, int topSpacePixels, int leftSpacePixels,
			int rightSpacePixels) {
		// This method paints the background

		// Only render the interface background if all the sprites can be loaded
		if (loadNewSprite("plating.png") && loadNewSprite("bolt.png")) {

			// Get the location of the sprites
			int platingSpriteNumber = 0;
			int boltSpriteNumber = 0;
			for (int i = 0; i < spriteList.size(); i++) {
				if (spriteList.get(i).getName().equals("bolt.png")) {
					boltSpriteNumber = i;
				}
			}
			for (int i = 0; i < spriteList.size(); i++) {
				if (spriteList.get(i).getName().equals("plating.png")) {
					platingSpriteNumber = i;
				}
			}

			int screenHeight = this.getHeight();
			int screenWidth = this.getWidth();

			// Render plating via a method with inbuilt bullet placing
			paintPlating(Screen, platingSpriteNumber, boltSpriteNumber, 0, screenHeight - bottomSpacePixels,
					bottomSpacePixels, leftSpacePixels);
			paintPlating(Screen, platingSpriteNumber, boltSpriteNumber, leftSpacePixels,
					screenHeight - bottomSpacePixels, bottomSpacePixels,
					screenWidth - leftSpacePixels - rightSpacePixels);
			paintPlating(Screen, platingSpriteNumber, boltSpriteNumber, screenWidth - rightSpacePixels,
					screenHeight - bottomSpacePixels, bottomSpacePixels, rightSpacePixels);
			paintPlating(Screen, platingSpriteNumber, boltSpriteNumber, screenWidth - rightSpacePixels, 0,
					screenHeight - bottomSpacePixels, rightSpacePixels);
		}

		// Paint the bottom left text
		paintText(Screen, this.bottomLeftText, this.getHeight(), 0, 100, 20);

	}

	private void paintPlating(Graphics2D Screen, int platingSpriteNumber, int boltSpriteNumber, int xLocation,
			int yLocation, int height, int width) {
		// This method paints the plating for the interface

		// Paint the plating itself
		spriteList.get(platingSpriteNumber).paint(Screen, xLocation, yLocation, height, width);

		// Decide the bolt sizes
		int boltHeight = 0;
		if (height > width) {
			boltHeight = width / 6;
		} else {
			boltHeight = height / 6;
		}
		int boltWidth = boltHeight;

		spriteList.get(boltSpriteNumber).paint(Screen, xLocation, yLocation, boltHeight, boltWidth);
		spriteList.get(boltSpriteNumber).paint(Screen, xLocation, yLocation + height - boltHeight, boltHeight,
				boltWidth);
		spriteList.get(boltSpriteNumber).paint(Screen, xLocation + width - boltWidth, yLocation, boltHeight, boltWidth);
		spriteList.get(boltSpriteNumber).paint(Screen, xLocation + width - boltWidth, yLocation + height - boltHeight,
				boltHeight, boltWidth);
	}

	private boolean loadNewSprite(String fileName) {
		// Loads a sprite with the mentioned filename and adds it to the loaded sprites
		// Returns true if the sprite is loaded already or loaded successfully
		BufferedImage img = null;
		Boolean doLoad = true;
		// Make sure the sprite isn't already loaded
		for (int i = 0; i < spriteList.size(); i++) {
			if (fileName.contentEquals(spriteList.get(i).getName())) {
				doLoad = false;
			}
		}
		// Load the sprite
		if (doLoad) {
			try {
				img = ImageIO.read(new File(folderName + fileName));
			} catch (IOException e) {
				System.err.println("Failed to load sprite with filename " + fileName);
			}
			if (img != null) {
				// Add the sprite to the list
				spriteList.add(new Sprite(img, fileName));
				System.out.println("Successfully added file: " + fileName);
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	private void paintPlayerHand(Graphics2D screen, int xPosition, int yPosition, int xSize, int ySize) {
		// This method paints the players hand in the space specified

		// First, find out if the hand is more constrained by height or width
		double heightConstraint = 0;
		double widthConstraint = 0;
		if (theHand.GetCardCount() > 0) {
			widthConstraint = xSize / theHand.GetCardCount();
		}
		if (cardRatio > 0) {
			heightConstraint = ySize / cardRatio;
		}

		// declare/initialize other needed variables
		int cardHeight = 0;
		int cardWidth = 0;
		int occupiedSpace = 0;
		int currentXPosition = xPosition;
		String fileName;
		boolean foundSprite = false;

		// Find the restricting dimension on the cards
		if (heightConstraint <= widthConstraint) {
			// If the cards are more constrained by height, base the cards on the available
			// height
			cardHeight = ySize;
			cardWidth = (int) (ySize / cardRatio);
			// Find the excess space, and remove it
			occupiedSpace = cardWidth * theHand.GetCardCount();
			xPosition += (xSize - occupiedSpace) / 2;
			xSize = occupiedSpace;
		} else {
			// If the cards are more constrained by width, base the cards on the available
			// width
			cardWidth = xSize / theHand.GetCardCount();
			cardHeight = (int) (cardWidth * cardRatio);
			// Find the excess space, and remove it
			occupiedSpace = cardHeight;
			yPosition += (ySize - occupiedSpace) / 2;
			ySize = occupiedSpace;
		}

		// Finally, render the cards
		for (int cardNo = 0; cardNo < theHand.GetCardCount(); cardNo++) {
			// Find the name of the cards sprite
			fileName = theHand.GetHandList().get(cardNo).getCardFileName();
			if (fileName != null) {
				// Assume the sprite is not found, then search the sprite list for that sprite
				foundSprite = false;
				for (int spriteNo = 0; spriteNo < spriteList.size(); spriteNo++) {
					if (spriteList.get(spriteNo).getName().equals(fileName)) {
						// Sprite found, render.
						foundSprite = true;
						currentXPosition = xPosition + cardNo * cardWidth;
						spriteList.get(spriteNo).paint(screen, currentXPosition, yPosition, cardHeight, cardWidth);
					}
				}
				// If the sprite was never found, load it
				if (!foundSprite) {
					if (loadNewSprite(fileName)) {
						// If the sprite is successfully loaded, render the most recent sprite
						currentXPosition = xPosition + cardNo * cardWidth;
						spriteList.get(spriteList.size() - 1).paint(screen, currentXPosition, yPosition, cardHeight,
								cardWidth);
					}
				}
			}
		}
	}

	private void paintPlayerDeck(Graphics2D screen, int xPosition, int yPosition, int xSize, int ySize) {
		// This method paints the players deck in the space specified
	}

	private void paintPlayerDiscardDeck(Graphics2D screen, int xPosition, int yPosition, int xSize, int ySize) {

	}

	private void paintField(Graphics2D screen, int xStartingPosition, int yStartingPosition, int xSize, int ySize) {
		// This method paints the field in the space specified

		screen.drawString(bottomLeftText, 20, this.getHeight() - 20);

		int xCentering = 0;
		int yCentering = 0;

		// Get the sprite sizes
		if (xSize < ySize) {
			yStartingPosition += (ySize - xSize) / 2;
			yCentering += (ySize - xSize) / 2;
			ySize = xSize;

		} else {
			xStartingPosition += (xSize - ySize) / 2;
			xCentering += (xSize - ySize) / 2;
			xSize = ySize;
		}

		int spriteWidth = xSize / theField.getPlayGridXSize();
		int spriteHeight = ySize / theField.getPlayGridYSize();

		int currentXPosition = 0;
		int currentYPosition = 0;

		int xMove = 0;
		int yMove = 0;

		// Declare and initialize other variables
		String fileName = null;
		boolean foundSprite = false;

		// Set the color to cyan
		screen.setColor(Color.CYAN);

		// Render the lines from top to bottom
		for (int x = 0; x < theField.getPlayGridXSize() + 1; x++) {
			currentXPosition = (x * xSize) / theField.getPlayGridXSize() + xCentering;
			screen.drawLine(currentXPosition, yStartingPosition, currentXPosition, yStartingPosition + ySize);
		}

		// Render the lines from left to right
		for (int y = 0; y < theField.getPlayGridYSize() + 1; y++) {
			currentYPosition = (y * ySize) / theField.getPlayGridYSize() + yCentering;
			screen.drawLine(xStartingPosition, currentYPosition, xStartingPosition + xSize, currentYPosition);
		}

		// Reduce the size of the sprites slightly, so that they fit within the lines
		spriteHeight--;
		spriteWidth--;

		// Iterate through the player field
		for (int x = 0; x < theField.getPlayGridXSize(); x++) {
			for (int y = 0; y < theField.getPlayGridYSize(); y++) {

				// Get the filename of the sprite at that location, or null if there is none
				fileName = theField.getObjectFileName(x, y);

				// If there is an object at that location to render, continue
				if (fileName != null) {

					// Find the expected move of the object at that location
					xMove = 0;
					yMove = 0;
					if (theField.getDirection(x, y) != null) {
						switch (theField.getDirection(x, y)) {
						case LEFT:
							xMove = -theField.getObjectRemainingMove(x, y);
							break;
						case RIGHT:
							xMove = theField.getObjectRemainingMove(x, y);
							break;
						case UP:
							yMove = -theField.getObjectRemainingMove(x, y);
							break;
						case DOWN:
							yMove = theField.getObjectRemainingMove(x, y);
							break;
						}
					}

					// Assume the sprite is not found, then search the sprite list for that sprite
					foundSprite = false;
					for (int spritePosition = 0; spritePosition < spriteList.size(); spritePosition++) {
						if (fileName.equals(spriteList.get(spritePosition).getName())) {

							// Sprite was found, render it
							foundSprite = true;

							// Increase the position by one to fit the lines in to the left rather than the
							// right
							currentXPosition = yStartingPosition + (x * xSize) / theField.getPlayGridXSize() + 1
									+ xCentering;
							currentYPosition = yStartingPosition + (y * ySize) / theField.getPlayGridYSize() + 1
									+ yCentering;
							spriteList.get(spritePosition).paint(screen, currentXPosition, currentYPosition,
									spriteHeight, spriteWidth);

							// Same as above, but render a transparent version to display their next move,
							// but only if they are moving somewhere onfield
							if (x + xMove >= 0 && x + xMove < theField.getPlayGridXSize() && y + yMove >= 0
									&& y + yMove < theField.getPlayGridYSize()) {
								currentXPosition = yStartingPosition
										+ ((x + xMove) * xSize) / theField.getPlayGridXSize() + 1 + xCentering;
								currentYPosition = yStartingPosition
										+ ((y + yMove) * ySize) / theField.getPlayGridYSize() + 1 + yCentering;
								spriteList.get(spritePosition).paintTransparent(screen, currentXPosition,
										currentYPosition, spriteHeight, spriteWidth, (float) 0.2);
							}
						}
					}

					// If the sprite wasn't found, load it
					if (!foundSprite) {
						if (loadNewSprite(fileName)) {

							// If the sprite is successfully loaded, render the most recently loaded sprite
							// Increase the position by one to fit the lines in to the left rather than the
							// right
							currentXPosition = yStartingPosition + (x * xSize) / theField.getPlayGridXSize() + 1
									+ xCentering;
							currentYPosition = yStartingPosition + (y * ySize) / theField.getPlayGridYSize() + 1
									+ yCentering;
							spriteList.get(spriteList.size() - 1).paint(screen, currentXPosition, currentYPosition,
									spriteHeight, spriteWidth);

							// Same as above, but render a transparent version to display their next move,
							// but only if they are moving somewhere onfield
							if (x + xMove >= 0 && x + xMove < theField.getPlayGridXSize() && y + yMove >= 0
									&& y + yMove < theField.getPlayGridYSize()) {
								currentXPosition = yStartingPosition
										+ ((x + xMove) * xSize) / theField.getPlayGridXSize() + 1 + xCentering;
								currentYPosition = yStartingPosition
										+ ((y + yMove) * ySize) / theField.getPlayGridYSize() + 1 + yCentering;
								spriteList.get(spriteList.size() - 1).paintTransparent(screen, currentXPosition,
										currentYPosition, spriteHeight, spriteWidth, (float) 0.2);
							}
						}
					}
				}

			}
		}

	}
	
	// This method will paint the current score value in the top right corner.
	private void paintScore(Graphics2D Screen, int x, int y, int width, int height)
	{
		int scoreValue = ResourceManager.GetRM().getScore();
		String score = "";
		if(scoreValue < 10)
		{
			score = "00" + scoreValue;
		} else if(scoreValue < 100)
		{
			score = "0" + scoreValue;
		} else
		{
			score = String.valueOf(scoreValue);
		}
		Screen.setColor(Color.BLACK);
		Screen.fillRect(x, y, width, height);
		Screen.setColor(Color.GREEN);
		Screen.setFont(new Font("Arial", Font.BOLD, 40));
		Screen.drawString(score, x + width / 2 - 30, y + height / 2 + 15);
		
		Screen.setFont(new Font("Arial", Font.BOLD, 20));
		Screen.setColor(Color.DARK_GRAY);
		Screen.drawString("SCORE", x + width / 2 - 35, y + height + 20);
		
		Screen.setColor(Color.BLACK);
	}

	private void paintEnemyDeck(Graphics2D Screen, int xPosition, int yPosition, int xSize, int ySize) {
		// This method paints the enemy deck in the space specified
	}

	private void paintText(Graphics2D screen, String text, int xPosition, int yPosition, int xSize, int ySize) {
		// This method renders text in the specified location, scaled to the correct
		// size

		// Firstly, set the font size to a large amount
		screen.setFont(new Font(screen.getFont().getFontName(), screen.getFont().getStyle(), 100));

		// While the font is too big
		while (screen.getFontMetrics().stringWidth(text) > xSize || screen.getFontMetrics().getHeight() > ySize) {

			// Make a new font that is the same as the old font, but one smaller
			screen.setFont(new Font(screen.getFont().getFontName(), screen.getFont().getStyle(),
					screen.getFont().getSize() - 1));
		}

		// Render the font
		screen.drawString(text, xPosition, yPosition);
	}
}
