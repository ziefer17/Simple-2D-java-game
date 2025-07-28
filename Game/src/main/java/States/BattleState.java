/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package States;

import java.awt.Color;

import java.awt.Graphics;
import java.util.ArrayList;

import Content.Arrow;
import Content.Assets;
import Content.AttackBarManager;
import Content.Bar;
import Content.InGamePlayer;
import Content.Monster;
import Content.Player;
import Content.Transition;
import com.mycompany.game.RenderHandler;
import com.mycompany.game.UIImageButton;
import com.mycompany.game.UIManager;
import com.mycompany.game.UIObject;
import com.mycompany.game.ClickListener;
import com.mycompany.game.Game;

public class BattleState extends State {

    RenderHandler handler;
    Bar bar;
    ArrayList<Monster> monsters = new ArrayList<>();
    Monster monster;
    InGamePlayer inGamePlayer;
    AttackBarManager attackBarManager;
    private UIManager uiManager;
    private UIManager arrowManager;
    public static boolean encounterFlag;
    public static boolean showBars;
    public static boolean playerAttack;
    public static boolean changedTurn;
    public static boolean switchGameStates;

//    public BattleState(RenderHandler handler) {
//        super(handler);
//        showBars = false;
//        playerAttack = true;
//        this.handler = handler;
//        bar = new Bar(handler);
//
//        monsters.add(new Monster("Rabbit", Assets.monsters[0], 38 * 4, 27 * 4, 440, 160, 50, 20, 1, handler));
//        //monsters.add(new Monster("Angry Radish", Assets.monsters[1], 23 * 4, 41 * 4, 480, 110, 1, 20, 1));
//
//        monster = monsters.get(0);
//        inGamePlayer = new InGamePlayer(handler);
//        attackBarManager = new AttackBarManager(handler);
//        switchGameStates = false;
//        initializeUI();
//    }
    
    public BattleState(RenderHandler handler) {
        super(handler);
        showBars = false;
        playerAttack = true;
        this.handler = handler;
        bar = new Bar(handler);

        monsters.add(new Monster("Rabbit", Assets.monsters[0], 38 * 4, 27 * 4, 440, 160, 50, 20, 1, handler));
        monster = monsters.get(0);
        inGamePlayer = new InGamePlayer(handler);
        attackBarManager = new AttackBarManager(handler);
        switchGameStates = false;
        encounterFlag = false; // Allow new encounters after battle ends
        initializeUI();
        System.out.println("BattleState initialized");
    }

    private boolean f;

    
    @Override
    public void tick() {
        if (showBars) {
            bar.tick();
            attackBarManager.tick();
        }
        uiManager.tick();
        monster.tick();
        inGamePlayer.tick();

        if (Monster.deathState != 0) {
            if (!f) {
                f = true;
                handler.getMouseManager().setUIManager(arrowManager);
            }
            arrowManager.tick();
            if (switchGameStates) {
                switchToGameState();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        try {
            drawBackground(g);
            monster.render(g);
            inGamePlayer.render(g);

            if (showBars) {
                bar.render(g);
                attackBarManager.render(g);
                if (Monster.deathState < 2) {
                    drawSquares(g);
                }
            } else {
                uiManager.render(g);
            }

            if (Monster.deathState <= 4 && Monster.deathState >= 2) {
                arrowManager.render(g);
            }

            if (Monster.deathState == 5) {
                switchToGameState();
                Monster.deathState = 0;
            }
        } catch (Exception e) {
            System.err.println("Error rendering BattleState");
            e.printStackTrace();
        }
    }

//    private void switchToGameState() {
//        encounterFlag = true;
//        handler.getMouseManager().setUIManager(null);
//        Transition.canStart = false;
//        Game.flag2 = false;
//        State.setState(handler.getGame().gameState);
//        BattleState.switchGameStates = true;
//        destroy();
//    }
    
    private void switchToGameState() {
        handler.getMouseManager().setUIManager(null);
        Transition.canStart = false;
        Game.flag2 = false;
        Game.flag = false;
        Player player = handler.getWorld().getEntityManager().getPlayer();
        player.endBattle();
        State.setState(handler.getGame().gameState);
        destroy();
        System.out.println("Returned to GameState");
    }

    private void initializeUI() {
        uiManager = new UIManager(handler);
        arrowManager = new UIManager(handler);
        handler.getMouseManager().setUIManager(uiManager);

        uiManager.addObject(new UIImageButton(50, 680, 49 * 4, 17 * 4, Assets.attackButton, new ClickListener() {
            @Override
            public void onClick() {
                showBars = true;
            }
        }));

        uiManager.addObject(new UIImageButton(300, 680, 49 * 4, 17 * 4, Assets.statsButton, new ClickListener() {
            @Override
            public void onClick() {
                if (!showBars) {

                }
            }
        }));

        uiManager.addObject(new UIImageButton(550, 680, 49 * 4, 17 * 4, Assets.escapeButton, new ClickListener() {
            @Override
            public void onClick() {
                if (!showBars) {
                    switchToGameState();
                    destroy();
                }
            }
        }));

        arrowManager.addObject(new Arrow(715, 728, 12 * 2, 12 * 2, Assets.arrow, new ClickListener() {
            @Override
            public void onClick() {
                if (Monster.deathState <= 4 && Monster.deathState >= 2) {
                    Monster.deathState++;
                }
            }

        }));

    }

    private void drawBackground(Graphics g) {
        Color c = new Color(0,0,0);
        g.setColor(c);
        g.fillRect(0, 0, 800, 800);
        g.drawImage(Assets.barGrass, -75, 524, 110 * 8, 85 * 4, null);

        //c = new Color(147, 147, 147);
        //g.setColor(c);
        //g.fillRect(25, 550, 750, 230);
        g.drawImage(Assets.greySquare, 28, 558, 185 * 4, 55 * 4, null);
        g.drawImage(Assets.grassPlatform, 356, 180, 86 * 4, 37 * 4, null);
    }

    private void drawSquares(Graphics g) {
        Color d = new Color(184, 184, 184);
        g.setColor(d);
        g.fillRect(32, Bar.height, 68, 50);
        d = new Color(73, 73, 73);
        g.setColor(d);
        g.fillRect(28, Bar.height, 4, 50);
        d = new Color(5, 169, 6);
        g.setColor(d);
        g.fillRect(0, Bar.height, 28, 50);

        d = new Color(184, 184, 184);
        g.setColor(d);
        g.fillRect(700, Bar.height, 64, 50);
        d = new Color(73, 73, 73);
        g.setColor(d);
        g.fillRect(764, Bar.height, 4, 50);
        d = new Color(5, 169, 6);
        g.setColor(d);
        g.fillRect(768, Bar.height, 28, 50);

        d = new Color(0, 124, 1);
        g.setColor(d);
        g.fillRect(796, Bar.height, 4, 50);

    }

    public void destroy() {
        attackBarManager.destroy();
    }

}
