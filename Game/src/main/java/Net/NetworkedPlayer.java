/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Net;

import Content.Assets;
import Content.Creature;
import Content.Entity;
import com.mycompany.game.Animation;
import com.mycompany.game.RenderHandler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author USER
 */
public class NetworkedPlayer extends Entity {
    private Animation animDown, animUp, animLeft, animRight;
    private int dir = 1;
    private int playerId;

    public NetworkedPlayer(RenderHandler handler, float x, float y, int playerId) {
        super(handler, x, y, Creature.PLAYER_WIDTH, Creature.PLAYER_HEIGHT);
        this.playerId = playerId;
        animDown = new Animation(120, Assets.player_down);
        animUp = new Animation(120, Assets.player_up);
        animLeft = new Animation(120, Assets.player_left);
        animRight = new Animation(120, Assets.player_right);
    }

    @Override
    public void tick() {
        // Animations tick for smooth rendering
        animDown.tick();
        animUp.tick();
        animRight.tick();
        animLeft.tick();
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getCamera().getxOffset()),
                (int) (y - handler.getCamera().getyOffset()), width, height, null);
    }

    private BufferedImage getCurrentAnimationFrame() {
        // Simplified; you can enhance with direction logic if needed
        return animDown.getCurrentFrame();
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getPlayerId() {
        return playerId;
    }
}
