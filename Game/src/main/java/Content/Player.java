/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Content;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.mycompany.game.Game;
import com.mycompany.game.RenderHandler;
import com.mycompany.game.World;
import com.mycompany.game.Animation;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player extends Creature {

    private Animation animDown, animUp, animLeft, animRight;
    public static int dir = 1;
    public static float xPosition;
    public static float yPosition;
    public static int health;
    public static int baseHealth;
    public static int level;
    public static String name;

    private boolean flag;
    private boolean flag2;
    private boolean flag3 = false;
    private int a;
    private int b;
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int playerId;
    private boolean inBattle = false;
    private long lastBattleTime = 0; // Track time of last battle
    private static final long BATTLE_COOLDOWN = 10000; // 1 second cooldown in milliseconds

    public Player(RenderHandler handler, float x, float y, int playerId, Socket socket, PrintWriter out, BufferedReader in) {
        super(handler, x, y, Creature.PLAYER_WIDTH, Creature.PLAYER_HEIGHT);
        this.playerId = playerId;
        this.socket = socket;
        this.out = out;
        this.in = in;

        bounds.x = 0;
        bounds.height = 35;
        bounds.y = PLAYER_HEIGHT - bounds.height - 1;
        bounds.width = 43;

        health = 100;
        baseHealth = 100;
        level = 1;
        name = "Hero" + playerId; // Unique name per player

        animDown = new Animation(120, Assets.player_down);
        animUp = new Animation(120, Assets.player_up);
        animLeft = new Animation(120, Assets.player_left);
        animRight = new Animation(120, Assets.player_right);
    }
    
//    @Override
//    public void move() {
//        if (xMove != 0 && !checkEntityCollisions(xMove, 0f)) {
//            moveX();
//            sendPosition();
//        }
//        if (yMove != 0 && !checkEntityCollisions(0f, yMove)) {
//            moveY();
//            sendPosition();
//        }
//    }
    
    @Override
    public void move() {
        if (inBattle) return; // Prevent movement during battle
        if (xMove != 0 && !checkEntityCollisions(xMove, 0f)) {
            moveX();
            sendPosition();
        }
        if (yMove != 0 && !checkEntityCollisions(0f, yMove)) {
            moveY();
            sendPosition();
        }
    }

    private void sendPosition() {
        out.println("MOVE:" + x + "," + y);
    }

    public int getPlayerId() {
        return playerId;
    }

//    @Override
//    public void tick() {
//        if (!Game.flag2) {
//            animDown.tick();
//            animUp.tick();
//            animRight.tick();
//            animLeft.tick();
//            getInput();
//            move();
//            checkEncounter();
//        }
//        handler.getCamera().centerOnEntity(this);
//    }
    
    @Override
    public void tick() {
        if (!Game.flag2) { // Only update animations if not transitioning
            animDown.tick();
            animUp.tick();
            animRight.tick();
            animLeft.tick();
        }
        if (!inBattle) { // Only get input and move if not in battle
            getInput();
            move();
            checkEncounter();
        }
        handler.getCamera().centerOnEntity(this);
    }
    
    public void endBattle() {
        inBattle = false;
        flag3 = false;
        lastBattleTime = System.currentTimeMillis(); // Set cooldown start
        sendPosition();
        System.out.println("Battle ended for player " + playerId);
    }

    private void getInput() {
        xMove = 0;
        yMove = 0;

        if (handler.getMouseManager().isRightPressed() && !flag) {
            flag = true;
            if (this.speed == 16.0f) {
                this.speed = 4.0f;
            } else {
                this.speed = 16.0f;
            }
        } else if (!handler.getMouseManager().isRightPressed() && flag) {
            flag = false;
        }

        if (handler.getKeymanager().q && !flag2) {
            flag2 = true;
            Game.showHitboxes = !Game.showHitboxes;
        } else if (!handler.getKeymanager().q && flag2) {
            flag2 = false;
        }

        if (handler.getKeymanager().up || handler.getKeymanager().Up) {
            yMove = -speed;
            dir = 1;
            return;
        }
        if (handler.getKeymanager().down || handler.getKeymanager().Down) {
            yMove = speed;
            dir = 0;
            return;
        }
        if (handler.getKeymanager().left || handler.getKeymanager().Left) {
            xMove = -speed;
            dir = 2;
            return;
        }
        if (handler.getKeymanager().right || handler.getKeymanager().Right) {
            xMove = speed;
            dir = 3;
            return;
        }

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(getCurrentAnimationFrame(), (int) (x - handler.getCamera().getxOffset()),
                (int) (y - handler.getCamera().getyOffset()), width, height, null);
        if (Game.showHitboxes) {
            g.setColor(Color.red);
            g.drawRect((int) (x + bounds.x - handler.getCamera().getxOffset()),
                    (int) (y + bounds.y - handler.getCamera().getyOffset()), bounds.width, bounds.height);
        }
    }

    private BufferedImage getCurrentAnimationFrame() {
        if (xMove < 0) {
            return animLeft.getCurrentFrame();
        } else if (xMove > 0) {
            return animRight.getCurrentFrame();
        } else if (yMove < 0) {
            return animUp.getCurrentFrame();
        } else if (yMove > 0) {
            return animDown.getCurrentFrame();
        } else {
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
                animRight.setIndex(1);
                return animRight.getCurrentFrame();
            }
        }
    }

//    private void checkEncounter() {
//        World w = handler.getWorld();
//        if ((w.getTile(w.getSpawnX() + ((int) Creature.xPosition) / 64,
//                w.getSpawnY() + ((int) Creature.yPosition) / 64) == Tile.bush) && !BattleState.encounterFlag
//                && Math.random() >= 0.0) {
//            a = w.getSpawnX() + ((int) Creature.xPosition) / 64;
//            b = w.getSpawnY() + ((int) Creature.yPosition) / 64;
//            if (!flag3) {
//                flag3 = true;
//                Game.flag = true;
//            }
//        } else if (BattleState.encounterFlag) {
//            if (a != w.getSpawnX() + ((int) Creature.xPosition) / 64
//                    || b != w.getSpawnY() + ((int) Creature.yPosition) / 64) {
//                BattleState.encounterFlag = false;
//                flag3 = false;
//            }
//        }
//    }

    private void checkEncounter() {
        World w = handler.getWorld();
        int tileX = (int) (x / Tile.TILEWIDTH);
        int tileY = (int) (y / Tile.TILEHEIGHT);
        Tile currentTile = w.getTile(tileX, tileY);

        long currentTime = System.currentTimeMillis();
        if (currentTile == Tile.bush && !inBattle 
            && currentTime - lastBattleTime > BATTLE_COOLDOWN 
            && Math.random() < 0.1) { // 10% chance for encounter
            a = tileX;
            b = tileY;
            if (!flag3) {
                flag3 = true;
                inBattle = true;
                Game.flag = true;
                //System.out.println("Battle triggered at tile (" + tileX + ", " + tileY + ")");
            }
        } else if (inBattle && (tileX != a || tileY != b)) {
            inBattle = false;
            flag3 = false;
            //System.out.println("Battle reset: Moved out of bush");
        }
    }
}
