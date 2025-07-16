/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package States;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import Content.Assets;
import Content.Description;
import Content.Player;
import Content.Text;
import com.mycompany.game.RenderHandler;
import com.mycompany.game.World;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameState extends State {

    private Player player;
    private World world;
    public static boolean flag;
    private Description playerDescription;
    public static int coins = 0;
    public static int xp = 0;
    private Text coinsText;

    public GameState(RenderHandler handler, Socket socket, PrintWriter out, BufferedReader in, int playerId) {
        super(handler);
        world = new World(handler, "src/main/resources/Graphics/Maps/world1.txt", socket, out, in, playerId);
        handler.setWorld(world);
        player = new Player(handler, 100, 100, playerId, socket, out, in);
        world.getEntityManager().setPlayer(player);
        playerDescription = new Description(2, Player.name, Player.health, Player.baseHealth, Player.level, handler);
    }

    @Override
    public void tick() {
        world.tick();

    }

    @Override
    public void render(Graphics g) {
        world.render(g);
        playerDescription.render(g);
        coinsText = new Text(coins + "", 50, 126, 4, 4);
        coinsText.render(g);

    }

}
