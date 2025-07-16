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
    private int dir = 1; // 0=down, 1=up, 2=left, 3=right
    private int playerId;
    private float lastX, lastY;

    public NetworkedPlayer(RenderHandler handler, float x, float y, int playerId) {
        super(handler, x, y, Creature.PLAYER_WIDTH, Creature.PLAYER_HEIGHT);
        this.playerId = playerId;
        this.lastX = x;
        this.lastY = y;
        animDown = new Animation(120, Assets.player_down);
        animUp = new Animation(120, Assets.player_up);
        animLeft = new Animation(120, Assets.player_left);
        animRight = new Animation(120, Assets.player_right);
    }

    @Override
    public void tick() {
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
        float xMove = x - lastX;
        float yMove = y - lastY;

        if (xMove < 0) {
            dir = 2; // Left
            return animLeft.getCurrentFrame();
        } else if (xMove > 0) {
            dir = 3; // Right
            return animRight.getCurrentFrame();
        } else if (yMove < 0) {
            dir = 1; // Up
            return animUp.getCurrentFrame();
        } else if (yMove > 0) {
            dir = 0; // Down
            return animDown.getCurrentFrame();
        } else {
            // No movement, use current direction and reset animation index
            if (dir == 0) {
                animDown.setIndex(0);
                return animDown.getCurrentFrame();
            } else if (dir == 1) {
                animUp.setIndex(0);
                return animUp.getCurrentFrame();
            } else if (dir == 2) {
                animLeft.setIndex(0);
                return animLeft.getCurrentFrame();
            } else {
                animRight.setIndex(0);
                return animRight.getCurrentFrame();
            }
        }
    }

    public void setPosition(float x, float y) {
        this.lastX = this.x;
        this.lastY = this.y;
        this.x = x;
        this.y = y;
    }

    public int getPlayerId() {
        return playerId;
    }
}
