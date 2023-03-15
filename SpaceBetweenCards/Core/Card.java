package Core;

public abstract class Card {
    private static int nextID = 0;
    private final int cardID;

	private String name;
	private String description;
	
	public Card(String name, String description) {
		this.description = description;
		this.name = name;
		
        cardID = nextID++; // sets a unique card ID and incriments the ID count        
	}
	
	public String GetDescription()
	{
	    return description;
	}
	//calls when card is drawn from deck
	public void OnDraw() {
    }

    
    //calls when discard from hand to deck
	public void OnDiscard() {
    }

	public int GetCardID() {
        return cardID;
    }
	
	//calls when card is played
    public boolean OnPlay(PlayField theField) {
		// This method does the cards effect. Is overwritten for individual cards. Uses
		// methods on the passed PlayField to do so. See PlayField for the methods.
		// If the card cannot be played (it would move the player off the board, for
		// example) then the method returns false. Otherwise, it returns true.
		return false;
	}
    public boolean OnPlay(PlayField theField, Hand theHand) {
    	return false;
    }
    public boolean OnPlay(PlayField theField, PlayerObject thePlayer) {
    	return false;
    }
	
	public String getCardFileName() {
		//This method returns a string of the filename of the JPG file that has the image of the card.
		//This method is overwritten for individual cards.
		return "strawberry.jpg";
	}
	
	public String getName() {
		return this.name;
	}
}
