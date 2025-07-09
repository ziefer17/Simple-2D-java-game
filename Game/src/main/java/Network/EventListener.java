
package Network;

import Content.Entity;
import Content.Player;
import Packets.AddPlayerPacket;
import Packets.MovementPacket;
import Packets.RemovePlayerPacket;
import com.mycompany.game.Game;
import com.mycompany.game.RenderHandler;

public class EventListener {
    
    private Entity player;
    private Game game;
    private RenderHandler handler;
    
    public EventListener(RenderHandler handler)
    {
        this.handler = handler;
        this.player = new Player(0, handler, 100,100, false);
    }
    public EventListener(Game game, RenderHandler handler) {
        this.handler = handler;
        this.game = game;
        this.player = new Player(0, handler, 100,100, false);
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
            
            Player newPlayer = new Player(packet.id, handler,100,100, true);
            handler.getWorld().getEntityManager().addEntity(newPlayer);
            
            System.out.println(packet.name + " has joined the game");
            if (game != null && packet.name.equals("Player" + packet.id)) {
                game.setPlayerId(packet.id);
                // Set the local player ID and mark as local
                for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
                    if (e.id == packet.id) {
                        ((Player) e).setLocal(true);
                        break;
                    }
                }
            }
        }
        else if (p instanceof RemovePlayerPacket)
        {
            RemovePlayerPacket packet = (RemovePlayerPacket) p;
            System.out.println(PlayerHandler.players.get(packet.id).name + " has left the game");
            PlayerHandler.players.remove(packet.id);  
            
            Entity toRemove = null;
            for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
                if (e.id == packet.id) {
                    toRemove = e;
                    break;
                }
            }
            if (toRemove != null) {
                handler.getWorld().getEntityManager().removeEntity(toRemove);
            }
        }
    }
}
