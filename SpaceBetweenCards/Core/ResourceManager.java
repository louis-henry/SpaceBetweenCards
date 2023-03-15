package Core;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import TypeListings.Direction;
import TypeListings.ObjectType;

public class ResourceManager implements KeyListener, MouseListener {
    private PlayField theField;
    private Deck[] theDecks;
    //private ArrayList<Card> theHand;
    private Hand theHand; 
    //private ArrayList<Card> theDeck;
    private ArrayList<Sprite> spriteList;
    private ArrayList<GameObject> objectList;
    private ArrayList<GameObject> objectKillList;
    private GameWindow theGameWindow;
    private JFrame theGameFrame;
    private int cardChosen;
    private boolean isCardChosen;
    private boolean readyToRender;
    private int maxHandSize;
    
    // Variables used to restore the location and size of
    // theGameFrame when resetting the game.
    private int oldWidth;
    private int oldHeight;
    private int oldXCoordinate;
    private int oldYCoordinate;
    
    private MusicManager theMusicManager;
    private MusicClips backGround;
    private MusicClips highScoreTrack;
    
    // The GameOptions class can be used to set specific settings
    // for the game.
    private GameOptions gameOptions;
    private boolean resumeGame;
    
    // Once scoring system implement, this variable can be used
    // to organise adding a new high score as part of the end
    // of game sequence.
    private boolean newHighScore;
    private int score;
    
    //made static so other's could use it as debug text
    public static String bottomLeftText;
    
    
    //Made instance available so classes can request objects.
    //Will need to be instantiated first by constructor
    static private ResourceManager instance = null;
    static public ResourceManager GetRM() {
        return instance;
    }
    
    // The boolean value gameOver is used to check if the end of game
    // sequence should be played.
    private boolean gameOver;

    // This method is the constructor for the resource manager. If xSize and ySize
    // are 0, it will full screen
    public ResourceManager(String gameName, int xScreenSize, int yScreenSize, int xBoardSize, int yBoardSize,
            Deck[] theDecks, Hand theHand) {

        instance = this;
        
        // Set gameOver and newHighScore to false;
        gameOver = false;
        newHighScore = false;
        score = 0;
        
        // Set up an instance of GameOPtions
        gameOptions = new GameOptions();

        // Set up the cards
        this.theDecks = theDecks;
        this.theHand = theHand;
        maxHandSize = 5;
        theHand.DrawCard(maxHandSize);
        
        // Store seed value in gameOptions if user wishes to save.
        gameOptions.setSeed(Deck.getSeed());
        
        //Create an instance of the music manager
        this.theMusicManager = new MusicManager(gameOptions);
        backGround = new MusicClips("BACKGROUND",
           0.15 * gameOptions.getVolume());
        
        // Create a new list of sprites, to be added to as needed
        this.spriteList = new ArrayList<Sprite>();
        ResourceManager.bottomLeftText = "Welcome";

        
        
        // Create a new list of game objects.
        this.objectList = new ArrayList<GameObject>(0);
        //buffer for objects to remove
        this.objectKillList = new ArrayList<GameObject>(0);
        

        // Create a new playing field
        theField = new PlayField(xBoardSize, yBoardSize);

        // The size of the window will be gotten dynamically as the user can change the
        // size, it should not be stored as
        // a separate variable. Begin the rendering process
        this.beginRendering(gameName, xScreenSize, yScreenSize);

        // Prepare to take input
        cardChosen = 0;
        isCardChosen = false;

        // Wait until the game is ready to render
        while (!readyToRender) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // Thread stopped while waiting
            }
        }

    }
    
    //This method gets the music manager
    public MusicManager getMM() {
    	return this.theMusicManager;
    }

    // This method should only ever be called by the game manager once in order to
    // be
    // able to move things around the player field
    public PlayField getPlayField() {
        return this.theField;
    }
    
    // Allows the GameWindow to get the current score value.
    public int getScore()
    {
    	return score;
    }
    
    // This method will increment the score value each time a PlayerProjectile
    // is found to collide with an enemy
    public void incrementScore()
    {
    	score++;
    }

    // This method creates a separate thread for rendering the screen
    private void beginRendering(String gameName, int xScreenSize, int yScreenSize) {
        readyToRender = false;
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createWindow(gameName, xScreenSize, yScreenSize);
            }
        });
    }

    // This creates a container for the game, known as a "JFrame"
    // Then adds the extended "JPanel" object called "theGameWindow" to the frame
    // that we can use to render things with.
    private void createWindow(String gameName, int xScreenSize, int yScreenSize) {
        theGameFrame = new JFrame(gameName);
        theGameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Pass the playing field and sprite list by reference so they can be rendered
        // by the game window.
        this.theGameWindow = new GameWindow(this.theField, this.spriteList, this.theHand, ResourceManager.bottomLeftText);
        theGameFrame.add(this.theGameWindow);
        if (xScreenSize == 0 && yScreenSize == 0) {
            theGameFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            theGameFrame.setSize(new Dimension(xScreenSize, yScreenSize));
        }
        theGameFrame.setResizable(true);
        theGameFrame.setVisible(true);
        theGameFrame.setBackground(Color.BLACK);
        // Menu update - moved key and mouse listeners to theGameFrame
        // to allow for resetting focus - needed when changing contents
        // of theGameFrame - see the switchToGameWindow() method below.
        //theGameWindow.setFocusable(true);
        //theGameWindow.addKeyListener(this);
        //theGameWindow.addMouseListener(this);
        // The game is now ready to render, and the resourceManager can finish it's
        // constructor
        readyToRender = true;

    }

    public int getObjectListSize()
    {
        return objectList.size();
    }
    
    public GameObject getObjectListElement(int index) {
        return objectList.get(index);
    }
    
    public void removeGameObject(GameObject target) {
        objectKillList.add(target);
    }
    
    public void update() {
    	//This method kills all objects from the object kill list
    	for(int i = 0; i < objectKillList.size(); i++)
    	{
    		boolean objectFound = false;
    		for(int j = 0; j < objectList.size() && !objectFound; j++)
    		{
    			// Safest method to check for equality is with the ID value.
    			if(objectKillList.get(i).getID() == objectList.get(j).getID())
    			{
    				if(objectList.get(j) instanceof PlayerObject)
    				{
                		GameManager.setGameState("The player was lost to the void");
                		gameOver = true;
    				}
    				objectList.remove(j);
    				objectFound = true;
    			}
    		}
    	}
    	
    	// Some objects in the kill list may not have been in the object list.
    	// (For example, because the play field has moved downward) so flush
    	// any extra items from the kill list...
    	objectKillList = new ArrayList<GameObject>(0);  
    }

    public PlayerObject getPlayer() {
        PlayerObject hold = null;
        for(int i=0;i< objectList.size(); ++i)
        {
            if(objectList.get(i).getID() == 0)
            {
                hold = (PlayerObject) objectList.get(i);
                i = objectList.size();
            } else {
                hold = null;
            }
        }
        return hold;
    }
    
    //returns 1 element array on the selected game object.
    public GameObject GetNewObject(ObjectType type, int X, int Y) {
        GameObject hold = null;
        switch (type) {
        case PLAYERSHIP:
            hold = new PlayerObject(0, X, Y, false);
            objectList.add(hold);
            System.out.println("Generated player");
            break;
        case PROJECTILE:
            hold = new Projectile(X, Y);
            objectList.add(hold);
            System.out.println("Generated Projectile: " + hold.getID());
            break;
        case PLAYERPROJECTILE:
            hold = new PlayerProjectile(X, Y);
            objectList.add(hold);
            System.out.println("Generated PlayerProjectile: " + hold.getID());
            break;
        case ENEMYSHIP:
            hold = new Enemy(X, Y);
            objectList.add(hold);
            System.out.println("Generated Enemy: " + hold.getID());
            break;
        default:
            return null;
        }
        return hold;

    }

    public GameObject GetNewObject(ObjectType type, int X, int Y, int value) {
        GameObject hold = null;
        switch (type) {
        case PLAYERSHIP:
            hold = new PlayerObject(0, X, Y, false);
            objectList.add(hold);
            System.out.println("Generated player");
            break;
        case PROJECTILE:
            hold = new Projectile(X, Y, value);
            objectList.add(hold);
            System.out.println("Generated Projectile: " + hold.getID());
            break;
        case PLAYERPROJECTILE:
            hold = new PlayerProjectile(X, Y, value);
            objectList.add(hold);
            System.out.println("Generated PlayerProjectile: " + hold.getID());
            break;
        default:
            return null;
        }
        return hold;

    }

    public void repaintWindow() {
        // repaints the window
        theGameWindow.repaint();
    }

    // This method gets input for the GameManager
    public int getInput() {
        while (!isCardChosen) {
            try {
                Thread.sleep(10);
                // Sleep until the key has been pressed
            } catch (InterruptedException e) {
                // Thread has been closed while sleeping
                System.out.println("bye!");
            }
        }
        // When a key has been pressed, return the key that was pressed only once
        isCardChosen = false;
        //bottomLeftText[0] = "Played the " + cardChosen + " card!";
        return cardChosen;
    }

    // The key press events
    @Override
    public void keyPressed(KeyEvent e) {
        // No actions need to be taken on key pressed, we are only logging keys typed
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No actions need to be taken on key release, we are only logging keys typed
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // When a key is pressed, store the pressed key and let the primary thread know
        // to read it if it is a valid input
        char keyPressed = e.getKeyChar();
        cardChosen = Character.getNumericValue(keyPressed);
        if (cardChosen > 0 && cardChosen <= theHand.GetCardCount()) {
            isCardChosen = true;
        }
        
        // Select letter 'o' for options.
        if(cardChosen == 24) isCardChosen = true;
        
        if ((int) cardChosen == -1) {
            // This is the escape key
            isCardChosen = true;
        }
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // This method will choose the clicked card

    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // No actions need to be taken on this method, we are only logging mouse clicks.
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // No actions need to be taken on this method, we are only logging mouse clicks.
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        // No actions need to be taken on this method, we are only logging mouse clicks.
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        // No actions need to be taken on this method, we are only logging mouse clicks.
    }

    // This method ends the rendering process
    public void stopRendering() {
    	// Frame dimensions are recorded for use with the resetting of the
    	// game.
    	oldWidth = theGameFrame.getWidth();
    	oldHeight = theGameFrame.getHeight();
    	oldXCoordinate = theGameFrame.getX();
    	oldYCoordinate = theGameFrame.getY();
        theGameWindow.removeKeyListener(this);
        theGameWindow.removeMouseListener(this);
        theGameWindow.setVisible(false);
        
        // The track is stopped - a new track will commence when the game is
        // reset.
        backGround.stopMusic();
    }
    
    public JFrame getGameFrame()
    {
    	return theGameFrame;
    }
    
    public int getOldWidth()
    {
    	return oldWidth;
    }
    
    public int getOldHeight()
    {
    	return oldHeight;
    }
    
    public int getOldXCoordinate()
    {
    	return oldXCoordinate;
    }
    
    public int getOldYCoordinate()
    {
    	return oldYCoordinate;
    }
    
    // The title screen is presented at the start of the game - or at the
    // commencement of a new game.
    public void setupMenu()
    {
    	theGameWindow.setVisible(false);
    	theGameFrame.add(new TitleScreen(this, theGameFrame.getWidth(),
           theGameFrame.getHeight()));
    	theGameFrame.setVisible(true);
    }
    
    // This method is called when the user chooses to start a new game.
    // The key and mouse listeners are added to theGameFrame and focus
    // is reset.
    public void switchToGameWindow()
    {
    	theGameFrame.add(theGameWindow);
    	theGameFrame.setFocusable(true);
    	theGameFrame.addKeyListener(this);
    	theGameFrame.addMouseListener(this);
    	theGameFrame.requestFocusInWindow();
    	theGameWindow.setVisible(true);
    }
    
    // This method is called when displaying the options screen. Select
    // letter 'o' during the game to display.
    public void displayOptions()
    {
    	resumeGame = false;
    	theGameWindow.setVisible(false);
    	theGameFrame.add(new OptionsScreen(this, theGameFrame.getWidth(),
           theGameFrame.getHeight(), backGround, false));
    	theGameFrame.setVisible(true);
    	
    	// This loop will run until the user has selected the return
    	// button on the options screen.
    	while(!resumeGame)
    	{
    		try{Thread.sleep(200);}catch(Exception ex){}
    	}
    	
    	// Save changes to options in Data/game_options.txt
    	gameOptions.saveOptions();
    	
    	// Now return to the normal game loop.
    	theGameFrame.add(theGameWindow);
    	theGameWindow.setVisible(true);
    	theGameFrame.toFront();
    	
    	return;
    }
    
    // The GameOptions instance is accessed by the OptionsScreen class.
    public GameOptions getGameOptions()
    {
    	return gameOptions;
    }
    
    // The getBackGround() method allows the options screen to access
    // a reference to the background music from the title screen.
    public MusicClips getBackGround()
    {
    	return backGround;
    }
    
    // Called when options screen is ready to return to the game.
    public void setResumeGame(boolean resumeGame)
    {
       this.resumeGame = resumeGame;
    }
    
    public void moveObjects() {
    	//This method consumes all the remaining movement for objects, making them move
    	//find the highest movement value
    	int highestMove = 0;
    	for(int i = 0; i < objectList.size(); i++) {
    		if(objectList.get(i).getMove() > highestMove) {
    			highestMove = objectList.get(i).getMove();
    		}
    	}
    	
    	//Repeat for each value, up to and including the highest movement value
    	for(int i = 1; i <= highestMove; i++) {
    		
    		//Repeat for every object
    		for(int j = 0; j < objectList.size(); j++) {
    			
    			//If it is time for the object to move
    			if((i * objectList.get(j).getMove() / highestMove) - objectList.get(j).getUsedMove() > 0) {
    				
    				//Reduce the remaining movement
    				objectList.get(j).reduceRemainingMove();
    				
    				//Move the object
    				theField.moveObject(objectList.get(j).getDirection(), objectList.get(j), 1);
    			}
    				
    		}
    		
    		//After every object moved, check for collisions and remove objects
    		theField.checkForCollision();
    		update();
    		
    		//Repaint the window, then wait for 100ms
    		repaintWindow();
    		try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Thread stopped while waiting
            }
    		
    	}
    }
    
    public void resetMove() {
    	//This method converts all objects speed into movement, to be consumed by the moveObject method later
    	for(int i = 0; i < objectList.size(); i++) {
    		objectList.get(i).resetMove();
    		if((objectList.get(i) instanceof Projectile || objectList.get(i) instanceof PlayerProjectile)
    	       && objectList.get(i).getRemainingMove() == 0) {
    			System.err.println("Error: projectile with no movement value");
    		}
    	}
    }
    
    public boolean getGameOver()
    {
    	return gameOver;
    }
    
    public void endOfGameSequence()
    {
    	theGameWindow.setVisible(false);
    	theGameFrame.removeMouseListener(this);
    	theGameFrame.removeKeyListener(this);
    	resumeGame = false;
    	
    	System.out.println("Player's final score is: " + score);
    	
    	newHighScore = isNewHighScore();
    	// If a new high score achieved - switch tracks.
    	if(newHighScore)
    	{
    		backGround.stopMusic();
        	highScoreTrack = new MusicClips("BachWellTempKlavier",
     	           0.15 * gameOptions.getVolume());
     	    highScoreTrack.playMusic();
    	}
    	
    	theGameFrame.add(new EndGameScreen(this, theGameFrame.getWidth(),
           theGameFrame.getHeight(), newHighScore));
    	theGameFrame.setVisible(true);
    	
    	// Pause game to allow user to enter read message or enter high score.
    	while(!resumeGame)
    	{
    		// Loop will exit when EndGameScreen sets resumeGame to true.
           try{Thread.sleep(100);}catch(Exception ex)
     	      {System.out.println("Exited thread sleep early.");}
    	}
    	
    	// Pause game for the end of game sequence - system should then restart.
    	try{Thread.sleep(5000);}catch(Exception ex)
    	   {System.out.println("Exited thread sleep early.");}
    	
    	// End high score track if running
    	if(highScoreTrack != null) highScoreTrack.stopMusic();
    }
    
    // This method checks to see if the score is equal to or higher than the
    // lowest high score listed in the high_score.txt file.
    public boolean isNewHighScore()
    {
    	boolean isNewHighScore = false;
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
		   
		   if(score >= Integer.parseInt(data[19]))
		   {
			   isNewHighScore = true;
			   return isNewHighScore;
		   }
		
	    } catch(Exception ex)
	    {
		   System.out.println("Failed to check high scores - check folder.");
	    }
    	
    	return isNewHighScore;
    }
    
}