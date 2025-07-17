/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.game;

import java.awt.image.BufferStrategy;
import Content.Assets;
import Content.Transition;
import Net.NetworkedPlayer;
import States.BattleState;
import States.GameState;
import States.MenuState;
import States.State;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


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
    
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int playerId;
    
    private AudioManager audioManager;

    public Game(String title, int width, int height) {
        this.width = width;
        this.height = height;
        this.title = title;
        keyManager = new KeyBoardListener();
        mouseManager = new MouseEventListener();
        audioManager = new AudioManager();
        connectToServer();
    }
    
    private void connectToServer() {
        try {
            socket = new Socket("172.32.4.80", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = in.readLine();
            if (line != null && line.startsWith("ID:")) {
                playerId = Integer.parseInt(line.substring(3));
                System.out.println("Assigned Player ID: " + playerId);
            }
            new Thread(this::listenForServerUpdates).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void listenForServerUpdates() {
        try {
            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("POSITIONS:")) {
                    updatePlayerPositions(message.substring(10));
                }
            }
        } catch (IOException e) {
            System.out.println("Disconnected from server");
        }
    }
    
    private void updatePlayerPositions(String data) {
        if (data.isEmpty()) return;
        String[] playerData = data.split(";");
        System.out.println("Received positions: " + data); // Debug
        for (String pd : playerData) {
            if (pd.isEmpty()) continue;
            try {
                String[] parts = pd.split(",");
                int id = Integer.parseInt(parts[0]);
                float x = Float.parseFloat(parts[1]);
                float y = Float.parseFloat(parts[2]);
                if (id != playerId) {
                    NetworkedPlayer np = handler.getWorld().getEntityManager().getNetworkedPlayers().get(id);
                    if (np == null) {
                        np = new NetworkedPlayer(handler, x, y, id);
                        handler.getWorld().getEntityManager().addNetworkedPlayer(np);
                        System.out.println("Added NetworkedPlayer ID: " + id + " at (" + x + ", " + y + ")");
                    } else {
                        np.setPosition(x, y);
                        System.out.println("Updated NetworkedPlayer ID: " + id + " to (" + x + ", " + y + ")");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error parsing position data: " + pd);
                e.printStackTrace();
            }
        }
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

        gameState = new GameState(handler, socket, out, in, playerId);
        menuState = new MenuState(handler);
        State.setState(gameState);
        updateSoundtrack();
    }
//    private void tick() { //updates all variables
//        keyManager.tick();
//
//        if (State.getState() != null) {
//            State.getState().tick();
//        }
//
//        if (flag) {
//            flag = false;
//            transition = new Transition();
//            flag2 = true;
//        }
//        if (Transition.canStart) {
//            Transition.canStart = false;
//            battling = true;
//            battleState = new BattleState(handler);
//            State.setState(handler.getGame().battleState);
//        }
//    }
    
    private void tick() {
        keyManager.tick();

        if (State.getState() != null) {
            State.getState().tick();
        }

        if (flag) {
            flag = false;
            transition = new Transition();
            flag2 = true;
            System.out.println("Starting transition to battle");
        }
        if (Transition.canStart) {
            Transition.canStart = false;
            battleState = new BattleState(handler);
            State.setState(battleState);
            System.out.println("Switched to BattleState");
        }
        updateSoundtrack();
    }
    
    private void updateSoundtrack() {
        String desiredTrack = null;
        if (State.getState() == menuState) {
            desiredTrack = "src/main/resources/audio/main_theme.wav";
        } else if (State.getState() == gameState) {
            desiredTrack = "src/main/resources/audio/main_theme.wav";
        } else if (State.getState() == battleState) {
            desiredTrack = "src/main/resources/audio/main_theme.wav";
        }

        if (desiredTrack != null && !desiredTrack.equals(audioManager.getCurrentTrack())) {
            audioManager.playSound(desiredTrack, true);
        }
    }

    private void render() {
        bs = display.getCanvas().getBufferStrategy();
        if (bs == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = bs.getDrawGraphics();

        // Clear screen
        g.clearRect(0, 0, width, height);

        // Draw stuff
        try {
            if (State.getState() != null) {
                State.getState().render(g);
            }

            if (flag2) {
                transition.render(g);
            }
        } catch (Exception e) {
            System.err.println("Error rendering game state");
            e.printStackTrace();
        }

        // End drawings
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
        audioManager.stopSound();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Game game = new Game("Title", 800, 800);
        game.start();
    }
}
