
package Network;

public class NetPlayer {
    public int id;
    public String name;
    public float x;
    public float y;
    
    public NetPlayer(int id, String name)
    {
        this.id = id;
        this.name = name;
        this.x = 0; // Default position
        this.y = 0;
    }
}
