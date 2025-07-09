
package Network;

import Content.Entity;
import Content.Player;
import Packets.AddPlayerPacket;
import Packets.MovementPacket;
import Packets.RemovePlayerPacket;
import States.GameState;
import com.mycompany.game.Game;
import com.mycompany.game.RenderHandler;

public class EventListener {
    
    private Entity player;
    private Game game;
    
    public EventListener(RenderHandler handler)
    {//Player(RenderHandler handler, float x, float y)
        this.player = new Player(0, handler, 100,100);
    }
    public EventListener(Game game, RenderHandler handler) {
        this.game = game;
        this.player = new Player(0, handler, 100,100);
    }
    
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
            if (game != null && packet.name.equals("Player" + packet.id)) {
                game.setPlayerId(packet.id);
            }
        }
        else if (p instanceof RemovePlayerPacket)
        {
            RemovePlayerPacket packet = (RemovePlayerPacket) p;
            System.out.println(PlayerHandler.players.get(packet.id).name + " has left the game");
            PlayerHandler.players.remove(packet.id);          
        }
    }
}
