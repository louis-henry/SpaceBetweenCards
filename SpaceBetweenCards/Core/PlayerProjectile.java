package Core;

import TypeListings.Direction;

public class PlayerProjectile extends GameObject {

    public PlayerProjectile(PlayerObject shooter, int speed) {
        super(shooter.getXCoordinates(), shooter.getYCoordinates());
        if(speed >0) {
            direction = Direction.UP;
            this.speed = speed;
        } else {
            direction = Direction.DOWN;
            this.speed = 0-speed;
        }
    }
    
    public PlayerProjectile(int x, int y) {
        super(x, y);
        direction = Direction.UP;
        speed = 2;
        
    }
    
    public PlayerProjectile(int x, int y, int speed) {
        super(x, y);
              
        this.speed = speed;
        if(speed >0) {
            direction = Direction.UP;
            this.speed = speed;
        } else {
            direction = Direction.DOWN;
            this.speed = 0-speed;
        }
    } 
    
    public String getObjectFileName() {
        //This method returns null, because an object should never be of just GameObject, it should always be an extended version
        if(direction == Direction.UP) {
            return "projectile.png";
        } else {
            return "projectile_down.png";
        }
        
    }
        
}