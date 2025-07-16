/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.game;

import java.awt.image.BufferStrategy;
import Content.Assets;
import Content.Entity;
import Content.Player;
import Content.Transition;
import States.BattleState;
import States.GameState;
import States.MenuState;
import States.State;
import java.awt.Graphics;


public class Game implements Runnable {
 
    private Display display;

    private int width;
    private int height;
    public String title;
    public boolean running = false;

    public static boolean showHitboxes = false;
    public static boolean flag = false;
    public static boolean flag2 = false;
    public static boolean battling = false;

    private Thread thread; //own separate game loop

    private BufferStrategy bs;
    private Graphics g;

    //States
    public State gameState;
    public State menuState;
    public State battleState;

    //Input
    private KeyBoardListener keyManager;
    private MouseEventListener mouseManager;

    private Rectangle gameCamera;

    private RenderHandler handler;
    private Transition transition;

    private int fps;
    private double timePerTick;

    double delta;
    long now;
    long lastTime;
    long timer;
    int ticks;
    
    public Game() {
        this.width = 800;
        this.height = 800;
        this.title = "Title";
        keyManager = new KeyBoardListener();
        mouseManager = new MouseEventListener();
    }

    private void init() {
        display = new Display("RPG Game", width, height);
        display.getFrame().addKeyListener(keyManager);
        display.getFrame().addMouseListener(mouseManager);
        display.getFrame().addMouseMotionListener(mouseManager);
        display.getCanvas().addMouseListener(mouseManager);
        display.getCanvas().addMouseMotionListener(mouseManager);
        Assets.init();

        handler = new RenderHandler(this);
        gameCamera = new Rectangle(handler, 0, 0);

        gameState = new GameState(handler);
        menuState = new MenuState(handler);
        //battleState = new BattleState(handler);
        State.setState(gameState);
        
    }

    private void tick() { //updates all variables
        keyManager.tick();

        if (State.getState() != null) {
            State.getState().tick();
        }

        if (flag) {
            flag = false;
            transition = new Transition();
            flag2 = true;
        }
        if (Transition.canStart) {
            Transition.canStart = false;
            battling = true;
            battleState = new BattleState(handler);
            State.setState(handler.getGame().battleState);
        }
    }

    private void render() { //renders all objects
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        //Clears screen
        g.clearRect(0, 0, width, height);

        //Draws stuff in the screen-
        if (State.getState() != null) {
            State.getState().render(g);
        }

        if (flag2) {
            transition.render(g);
        }

        //End drawings-
        bs.show();
        g.dispose();
    }

    //starting the thread runs this method
    public void run() {
        init();
        fps = 60;
        timePerTick = 1000000000 / fps; //1 billion bcus 1 billion nanoseconds in one second
        delta = 0;
        lastTime = System.nanoTime();
        timer = 0;
        ticks = 0;

        while (running) {           
            
            now = System.nanoTime();
            delta += (now - lastTime) / timePerTick;
            //System.out.println(delta);
            timer += now - lastTime;
            lastTime = now;
            if (delta >= 1) {
                tick();
                render();
                ticks++;
                delta--;
            }
            if (timer >= 1000000000) { //if timer exceeds one second
                System.out.println("FPS: " + ticks);
                ticks = 0;
                timer = 0;
            }
        }

        stop();
    }

    public KeyBoardListener getKeyBoardListener() {
        return keyManager;
    }

    public MouseEventListener getMouseEventListener() {
        return mouseManager;
    }

    public Rectangle getCamera() {
        return gameCamera;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public synchronized void start() {

        if (running == true) {
            return; //checks if already running
        }
        running = true;

        //runs this class on a new thread
        thread = new Thread(this);

        //calls the run method
        thread.start();
    }

    public synchronized void stop() {
        if (running == false) {
            return;
        }
        running = false;

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public RenderHandler getRenderHandler() {
        return handler;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
