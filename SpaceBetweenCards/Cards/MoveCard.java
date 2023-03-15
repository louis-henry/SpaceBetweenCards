package Cards;
import Core.Card;
import Core.PlayField;
import Core.ResourceManager;
import TypeListings.Direction;

public class MoveCard extends Card {

    private int distance;
    private Direction direction;

    public MoveCard(String name, String description, Direction direction) {
        super(name, description);
        distance = 1;
        this.direction = direction;
    }

    public MoveCard(String name, String description, Direction direction, int dist) {
        super(name, description);
        distance = dist;

        this.direction = direction;
    }

    public String getCardFileName() {

        // This returns the name of the image that is rendered for this object
        String hold;
        switch(direction) {
        case UP:
            hold = "cardShipUp.png";
            break;

        case DOWN:
            hold = "cardShipDown.png";
            break;

        case LEFT:
            hold = "cardShipLeft.png";
            break;

        case RIGHT:
            hold = "cardShipRight.png";
            break;
        default:
            hold = "";
        }
        return hold;
    }
    
    public int getDistance() {
        return distance;
    }
    
    public Direction GetDirection() {
        return direction;
    }

    public boolean OnPlay(PlayField theField) {

        // Moves the player direction one space
        // Assumed a movePlayer() + getPlayer() method
        // both in the PlayField class but can be changed
    	for(int i = 0; i < distance; i++) {
    		theField.moveObject(direction, ResourceManager.GetRM().getPlayer(), 1);
    		theField.checkForCollision();
    		//Repaint the window, then wait for 100ms, to make the movement clear
    		ResourceManager.GetRM().repaintWindow();
    		try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Thread stopped while waiting
            }
    	}
    	theField.moveObject(direction.UP, ResourceManager.GetRM().getPlayer(), 1);
        
        return true;
    }
}
