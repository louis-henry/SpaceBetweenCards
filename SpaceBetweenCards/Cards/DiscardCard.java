package Cards;

import Core.Card;
import Core.Deck;
import Core.Hand;
import Core.PlayField;
import Core.ResourceManager;
import TypeListings.Direction;

public class DiscardCard extends Card {

	public DiscardCard(String name, String description) {
		super(name, description);
	}
	
	public boolean OnPlay(PlayField theField, Hand theHand) {

		theField.moveObject(Direction.UP, ResourceManager.GetRM().getPlayer(), 1);
		theHand.DiscardAll();
		theHand.DrawCard(5);
		return true;
    }
	
	public String getCardFileName() {
        // This returns the name of the image that is rendered for this object
        return "Discard.png";
    }

}
