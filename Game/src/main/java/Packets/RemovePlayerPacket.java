
package Packets;

import java.io.Serializable;


public class RemovePlayerPacket implements Serializable{
    private static final long serialVersionUID = 1L;
    
    public int id;
    
    public RemovePlayerPacket(int id)
    {
        this.id = id;
    }
    
    public RemovePlayerPacket() {
        this.id = -1; // Used by client when disconnecting
    }
}
