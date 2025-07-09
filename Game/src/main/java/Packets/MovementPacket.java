
package Packets;

import java.io.Serializable;


public class MovementPacket implements Serializable {
    public int id; 
    public float x, y;  // Player coordinates

    public MovementPacket(int playerId, float x, float y){
        this.id = playerId;
        this.x = x;
        this.y = y;
    }
}
