package Core;

import TypeListings.Direction;

public abstract class GameObject {
    private int objectID;
    private int health;
    private int xCoordinate;
    private int yCoordinate;

    // The movement variables. All objects have them, but some will have no inbuilt
    // movement
    protected Direction direction;
    protected int speed;
    private int move;
    private int remainingMove;

    static private int ID = 1;

    public GameObject(int xCoordinate, int yCoordinate) {
        this.objectID = ID++;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.health = 0;
        this.speed = 0;
        this.move = 0;
        this.direction = null;
    }

    protected GameObject(int objectID, int xCoordinate, int yCoordinate) {
        this(xCoordinate, yCoordinate);
        this.objectID = objectID;
    }

    public int getID() {
        return objectID;
    }

    public void setYCoordinate(int y) {
        yCoordinate = y;
    }

    public void setXCoordinate(int x) {
        xCoordinate = x;
    }

    public int getObjectID() {
        return this.objectID;
    }

    public int getXCoordinates() {
        return this.xCoordinate;
    }

    public int getYCoordinates() {
        return this.yCoordinate;
    }

    public void setXYCoordinates(int x, int y) {
        this.xCoordinate = x;
        this.yCoordinate = y;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getHealth() {
        return this.health;
    }

    // This method must be implemented in any subclasses, so that they can be
    // rendered
    public abstract String getObjectFileName();

    public Direction getDirection() {
        return direction;
    }

    public void resetMove() {
        if (speed != 0) {
            move = speed;
        }
        remainingMove = move;
    }

    public int getMove() {
        return move;
    }

    public int getRemainingMove() {
        return remainingMove;
    }
    
    public int getUsedMove() {
    	return move - remainingMove;
    }

    public void reduceRemainingMove() {
        remainingMove--;
    }

    public void Update(PlayField field) {
    }
    
    public void setMove(int move, Direction direction) {
    	//This method manually sets the movement for this turn
    	this.move = move;
    	this.direction = direction;
    	this.speed = 0;
    }
}