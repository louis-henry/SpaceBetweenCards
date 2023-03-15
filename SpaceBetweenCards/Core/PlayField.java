package Core;
import java.lang.reflect.Array;
import java.util.Random;

import TypeListings.Direction;
import TypeListings.ObjectType;

public class PlayField {
    
    private static Random rand = new Random();
    private static boolean seedSet = false;
    private final static long seed = System.currentTimeMillis();
    
	private GameObjectList[][] playGrid;
    private int playGridXSize;
    private int playGridYSize;
    private int start;
	
	public PlayField(int x, int y) {
		this.playGridXSize = x;
	    this.playGridYSize = y;
	    
	    if (seedSet == false) {
            rand.setSeed(seed);
            seedSet = true;
        }
	    
		playGrid = new GameObjectList[x][y];
		for(int i = 0; i < x; i++) {
			for(int j = 0; j < y; j++) {
				playGrid[i][j] = new GameObjectList();
			}
		}
		
		spawnPlayer(x/2, y-3);
		start = 0;
		
	}
	
	//Converts Y array location to the Y location as shown on the grid
	private int GetVisualY(int y) {
	    return ToRangeY(y-start);
	}
	//Converts Y from visual location, to location in array 
	private int GetActualY(int y) {
	    return ToRangeY(y+start);	    
	}
	
	//Converts value to be within the array's range
	private int ToRangeY(int y) {
	    
	    //while used over if statement in case Y is more than one grid length out
        while(y<0) {
            y+=playGrid[0].length;
        }
        while(y>=playGrid[0].length) {
            y-=playGrid[0].length;
        }
        return y;
    }
	
	//Converts value to be within the array's range
	private int ToRangeX(int x) { //test
        //while used over if statement in case X is more than one grid length out
        while(x <0) {
            x +=playGrid.length;
        }
        while(x>=playGrid.length) {
            x-=playGrid.length;
        }
        return x;
    }
	
	public void update() {
		//This method updates the field
		
	    --start;
	    start = ToRangeY(start);
	    
	    
	    for(int i =0; i < playGridXSize; ++i) {
            playGrid[i][start].removedAll();
        }
	    
        if(rand.nextBoolean()) {
            spawnObject(ObjectType.ENEMYSHIP, rand.nextInt(playGridXSize), GetActualY(0));
        }
        
        for(int i = 0; i < playGrid.length; i++) {
            for(int j = 0; j < playGrid[0].length; j++) {
                playGrid[i][j].UpdateAll(this);
            }
        }
	}
	
	// Spawns player at requested point
	public void spawnPlayer(int x, int y) {
	    playGrid[x][GetActualY(y)].addObject(ResourceManager.GetRM().GetNewObject(ObjectType.PLAYERSHIP, x, GetActualY(y)));
	}
	
	public void spawnObject(ObjectType type, int x, int y) {
        if(x>=0 && x < playGridXSize && y >=0 && y < playGridYSize) {
            playGrid[x][y].addObject(ResourceManager.GetRM().GetNewObject(type, x, y));
        }
    }
	
	public void spawnObject(ObjectType type, int x, int y, int value) {
        if(x>=0 && x < playGridXSize && y >=0 && y < playGridYSize) {
            playGrid[x][y].addObject(ResourceManager.GetRM().GetNewObject(type, x, y, value));
        }
    }
	
	// Checks for collision and then supplies string of object ID's
	public void checkForCollision() {
	    //maybe playGridXSize and playGridYSize
		for (int i = 0; i < playGridXSize; i++) {
			for (int j = 0; j < playGridYSize; j++) {
				if(playGrid[i][j].isThereCollision()) {
					
					// Check if player's score should be incremented.
					if(playGrid[i][j].isScoreEvent())
					{
						System.out.println("Player has scored a direct hit!");
						ResourceManager.GetRM().incrementScore();
					}
					
					//For the moment, destroy all objects in that location
					playGrid[i][j].removedAll();
				}
			}
		}	
	}

	// Method for moving player around the board
	// Checks board for player object and then moves the object to another GameObjectList
	// Code can be reduced to check for player before case and then implement the move
	// Exception still needs to be implemented to stop player moving off board
	public void moveObject(Direction direction, GameObject target, int distance) {
		int x = target.getXCoordinates();
		int y = target.getYCoordinates();
	    
		boolean onGrid = true; 
		
	    switch(direction) {
		case LEFT:
			if(playGrid[x][y].removeObject(target)){
			    x -= distance;
			    if(x < 0) {
			        x=0;
			    }
			}
			break;
		case RIGHT :
		    if(playGrid[x][y].removeObject(target)){
                x += distance;
                if(x >= playGridXSize) {
                    x=playGridXSize-1;
                }
            }
			break;
		case UP :
		    if(playGrid[x][y].removeObject(target)){
                y -= distance;

                if(y < start && (y+distance) >= start) {
                    if(target instanceof PlayerObject) {
                        y=start;
                    } else {
                        ResourceManager.GetRM().removeGameObject(target);
                        onGrid = false;
                    }
                } else {
                    y = ToRangeY(y);
                }
                    
            }
			break;
		case DOWN :
		    if(playGrid[x][y].removeObject(target)){
                y += distance;
                
                if(GetVisualY(y-distance) > GetVisualY(ToRangeY(y))) {
                    if(target instanceof PlayerObject) {
                        y = GetActualY(playGridYSize-1);
                    }else {
                        ResourceManager.GetRM().removeGameObject(target);
                        onGrid = false;
                    }
                }
                y = ToRangeY(y);
                
            }
			break;
		}
	    
	    if(onGrid) {
	        playGrid[x][y].addObject(target);
	        target.setXYCoordinates(x, y);
	    }
	}
	
	public String getObjectFileName(int xLocation, int yLocation) {
		//Returns the filename for the sprite, so it can be rendered
		return playGrid[xLocation][GetActualY(yLocation)].getObjectFileName();
	}
	
	public int getObjectRemainingMove(int xLocation, int yLocation) {
		//Get the movement of an object at a location. Used for rendering
		return playGrid[xLocation][GetActualY(yLocation)].getRemainingMove();
	}
	
	public Direction getDirection(int xLocation, int yLocation) {
		//Gets the direction of an object at a location. Used for rendering
		return playGrid[xLocation][GetActualY(yLocation)].getDirection();
	}
	
	public int getPlayGridXSize() {
		//Returns the grid size
        return this.playGridXSize;
    }

    public int getPlayGridYSize() {
    	//Returns the grid size
        return this.playGridYSize;
    }

	public void removeEnemies() {
		for (int i = 0; i < playGridXSize; i++) {
			for (int j = ResourceManager.GetRM().getPlayer().getYCoordinates() + 1; j < playGridYSize; j++) {
				playGrid[i][j].removedAll();		
			}
		}
	}
}
