package Cards;

import java.util.Random;

import Core.Card;
import Core.Enemy;
import Core.GameObject;
import Core.PlayField;
import Core.ResourceManager;
import TypeListings.Direction;
import TypeListings.ObjectType;

public class LightningStrikeCard extends Card {

	public LightningStrikeCard(String name, String description) {
		super(name, description);
	}
	
	public String getCardFileName() {
        // This returns the name of the image that is rendered for this object
        return "LightningStrike.png";
    }
	
    public boolean OnPlay(PlayField theField) {
    	theField.moveObject(Direction.UP, ResourceManager.GetRM().getPlayer(), 1);
    	theField.removeEnemies();
    	return true;
    	
    }

}