package Cards;

import Core.Card;
import Core.PlayField;
import Core.PlayerObject;
import Core.ResourceManager;
import TypeListings.Direction;
import Core.ResourceManager;
import TypeListings.Direction;
import TypeListings.ObjectType;

public class ShieldCard extends Card {
    
	public ShieldCard(String name, String description) {
        super(name, description);
    }

    public String getCardFileName() {
        // This returns the name of the image that is rendered for this object
        return "Shield.png";
    }

    public boolean OnPlay(PlayField theField, PlayerObject thePlayer) {
    	
    	theField.moveObject(Direction.UP, ResourceManager.GetRM().getPlayer(), 1);
    	thePlayer.setShieldStatus();

    	return true;
    }
}