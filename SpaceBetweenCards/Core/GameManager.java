package Core;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.Timer;

public class GameManager {
    final private int fieldXSize = 10;
    final private int fieldYSize = 12;
	private PlayField theField;
	private Deck[] theDecks;
	private Hand theHand;
	//private int handSize;
	// Create an reference to a resource manager
	ResourceManager theResourceManager;
	private static String gameState;

	public static void main(String args[]) {
		GameManager spaceBetweenCards = new GameManager();
		spaceBetweenCards.run();
	}

	public GameManager() {
		// This method is the constructor. Will make the starting instances of all
		// objects by calling the constructors of other classes
		
		//Set up all the decks
		theHand = new Hand(); 
		Deck thePlayerDeck = new Deck();
		Deck theEnemyDeck = new Deck();
		Deck theFieldDeck = new Deck();
		Deck[] theDecks = {thePlayerDeck, theFieldDeck, theEnemyDeck};

        theHand.SetDrawDeck(theDecks[0]);
		
		//Make the resource manager
		theResourceManager = new ResourceManager("The Space Between Cards", 0, 0, fieldXSize, fieldYSize, theDecks, theHand);
		
		//Get a reference to the Play Field from the resource manager
		theField = theResourceManager.getPlayField();
		
		theResourceManager.setupMenu();
	}

	public void run() {
		// This method contains the game logic loop (no rendering)
		// Firstly, make the player object at with id 1 at location 3, 3
		GameManager.gameState = "running";
		int cardChosen;
		
        //theResourceManager.repaintWindow();
		// Setting a timer to allow for animations that are
		// not triggered by either user input or a resizing of the window
		Timer timer = new Timer(50, e->
		{
		   theResourceManager.repaintWindow();
	    });
		timer.start();
		
        // Start the loop
		while (GameManager.gameState.equals("running")) {
			
			
			// paint the screen, then wait for player input
		    ResourceManager.GetRM().repaintWindow();
			cardChosen = theResourceManager.getInput();
			
			// Choose a card with the input and play it or close
			if ((int) cardChosen == -1) {
				
				// This is the escape key. Avada Kedavara!
				GameManager.setGameState("Closed");
				System.out.println("Goodbye!");
			} else if(cardChosen == 24)
			{
				
				// User has selected the letter 'o' for the options screen.
				timer.stop();
				theResourceManager.displayOptions();
				timer.restart();
			} else {
				//Play the chosen card, and draw new cards
			    theHand.PlayCard(theField, cardChosen);
			    //theHand.DrawCard(5);
			    //The end of turn stuff
			    
				//Move objects in motion
			    ResourceManager.GetRM().moveObjects();
			    
			    // Exit the loop early if gameOver has been set to true.
			    if(!theResourceManager.getGameOver())
			    {
			    	
			    
			    //Move the player field and destroy offscreen objects
			    //Also give enemies their movement for next turn.
			    theField.update();
			    
			    ResourceManager.GetRM().resetMove();
			    
			    //Do cleanup of destroyed objects
			    ResourceManager.GetRM().update();
			    } else
			    {
			    	// Exiting early - gameOver was set to true.
			    }
			}
		}

		timer.stop();
		// If gameOver is true, play the end of game sequence.
		if(theResourceManager.getGameOver()) theResourceManager.endOfGameSequence();
		restartMenu();
	}
	
	// This method is called when the game ends or the user has selected
	// escape to end the game. The game contents are reset and a new window
	// sets up the title screen.
	private void restartMenu()
	{
		theResourceManager.stopRendering();
		// Record dimensions of the window to use when resetting the game.
		int oldWidth = theResourceManager.getOldWidth();
		int oldHeight = theResourceManager.getOldHeight();
		int oldXCoordinate = theResourceManager.getOldXCoordinate();
		int oldYCoordinate = theResourceManager.getOldYCoordinate();
		if(theResourceManager.getGameFrame().getExtendedState() == JFrame.MAXIMIZED_BOTH)
		{
			oldWidth = 0;
			oldHeight = 0;
		}
		theResourceManager.getGameFrame().dispose();
		
		// Game details are now reset.
		theHand = new Hand(); 
		Deck thePlayerDeck = new Deck();
		Deck theEnemyDeck = new Deck();
		Deck theFieldDeck = new Deck();
		Deck[] theDecks = {thePlayerDeck, theFieldDeck, theEnemyDeck};
		
        theHand.SetDrawDeck(theDecks[0]);
        
        // A new resource manager is generated.
        theResourceManager = new ResourceManager("The Space Between Cards",
           oldWidth, oldHeight, fieldXSize, fieldYSize, theDecks, theHand);
        theResourceManager.getGameFrame().setLocation(oldXCoordinate,
           oldYCoordinate);
		theField = theResourceManager.getPlayField();
		theResourceManager.setupMenu();
		
		// The new game can now be run.
		run();
	}
	
	public static void setGameState(String gameState) {
		GameManager.gameState = "gameState";
	}
}
