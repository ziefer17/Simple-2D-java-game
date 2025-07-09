
package Network;

import Content.Entity;
import Packets.AddPlayerPacket;
import Packets.MovementPacket;
import Packets.RemovePlayerPacket;
import States.GameState;

public class EventListener {
    
    private Entity player;
    
    public void received(Object p)
    {
        if(p instanceof MovementPacket)
        {
            MovementPacket packet = (MovementPacket)p;
            player.updatePlayerPosition(packet.id, packet.x, packet.y);
        }
        else if(p instanceof AddPlayerPacket)
        {
            AddPlayerPacket packet = (AddPlayerPacket) p;
            PlayerHandler.players.put(packet.id,new NetPlayer(packet.id,packet.name));
            System.out.println(packet.name + " has joined the game");
        }
        else if (p instanceof RemovePlayerPacket)
        {
            RemovePlayerPacket packet = (RemovePlayerPacket) p;
            System.out.println(PlayerHandler.players.get(packet.id).name + " has left the game");
            PlayerHandler.players.remove(packet.id);          
        }
    }
}
