/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Content;

import Net.NetworkedPlayer;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Comparator;

import com.mycompany.game.RenderHandler;
import java.util.HashMap;
import java.util.Map;

public class EntityManager {

    private RenderHandler handler;
    private Player player;
    private Map<Integer, NetworkedPlayer> networkedPlayers;
    private ArrayList<Entity> entities;
    private Comparator<Entity> renderSorter = new Comparator<Entity>() {

        @Override
        public int compare(Entity a, Entity b) {
            if (a.getY() + a.getHeight() < b.getY() + b.getHeight()) {
                return -1;
            }
            return 1;
        }

    };

    public EntityManager(RenderHandler handler, Player player) {
        this.handler = handler;
        this.player = player;
        this.networkedPlayers = new HashMap<>();
        this.entities = new ArrayList<>();
        addEntity(player);
    }

    public void tick() {
        for (Entity e : entities) {
            e.tick();
        }
        for (NetworkedPlayer np : networkedPlayers.values()) {
            np.tick();
        }
    }

    public void render(Graphics g) {
        for (Entity e : entities) {
            e.render(g);
        }
        for (NetworkedPlayer np : networkedPlayers.values()) {
            np.render(g);
        }
        entities.sort(renderSorter);
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }
    
    public void addNetworkedPlayer(NetworkedPlayer np) {
        networkedPlayers.put(np.getPlayerId(), np);
    }
    
    public void removeNetworkedPlayer(int playerId) {
        networkedPlayers.remove(playerId);
    }
    
    public Map<Integer, NetworkedPlayer> getNetworkedPlayers() {
        return networkedPlayers;
    }

    public void removeEntity(Entity e) {
        entities.remove(e);
    }

    public RenderHandler getHandler() {
        return handler;
    }

    public void setHandler(RenderHandler handler) {
        this.handler = handler;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }
}
