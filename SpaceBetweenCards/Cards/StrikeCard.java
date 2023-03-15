package Cards;

import Core.Card;
import Core.PlayField;
import Core.ResourceManager;
import TypeListings.Direction;
import TypeListings.ObjectType;

public class StrikeCard extends Card{

	int speed;
    public StrikeCard(String name, String description) {
        super(name, description);
        speed = 1;
    }

    public StrikeCard(String name, String description, int speed) {
        super(name, description);
        this.speed = speed;
    }

    public String getCardFileName() {

        // This returns the name of the image that is rendered for this object
        return "Strike.png";
    }

    public boolean OnPlay(PlayField theField) {
        // Moves the player one space to the Up.
        // Assumed a movePlayer() + getPlayer() method
        // both in the PlayField class but can be changed
    	theField.moveObject(Direction.UP, ResourceManager.GetRM().getPlayer(), 1);
    	theField.spawnObject(ObjectType.PROJECTILE, ResourceManager.GetRM().getPlayer().getXCoordinates(), ResourceManager.GetRM().getPlayer().getYCoordinates()-1);
    	theField.spawnObject(ObjectType.PROJECTILE, ResourceManager.GetRM().getPlayer().getXCoordinates() + 1, ResourceManager.GetRM().getPlayer().getYCoordinates()-1);
    	theField.spawnObject(ObjectType.PROJECTILE, ResourceManager.GetRM().getPlayer().getXCoordinates() - 1, ResourceManager.GetRM().getPlayer().getYCoordinates()-1);
    	ResourceManager.GetRM().getMM().addMusic(ObjectType.PROJECTILE);
        return true;
    }

}
