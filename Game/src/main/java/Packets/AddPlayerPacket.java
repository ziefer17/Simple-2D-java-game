
package Packets;

import java.io.Serializable;

public class AddPlayerPacket implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    public int id;
    public String name;

    public AddPlayerPacket(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
