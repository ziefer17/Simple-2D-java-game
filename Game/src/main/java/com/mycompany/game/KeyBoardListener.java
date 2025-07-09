
package com.mycompany.game;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class KeyBoardListener implements KeyListener, FocusListener {
    public boolean[] keys ;
    private Game game;
    public boolean up, down, left, right;
	public boolean Up, Down, Left, Right;
	public boolean q, space;
	private int current = -1;
	private int backup = -1;
	private int keysPressed = 0;
	public static boolean isMoving = false;
	private boolean flag = false;
    
    public KeyBoardListener(Game game) {
        this.game = game;
    }
    
    public KeyBoardListener() {
        keys = new boolean[256];
    }

    public void focusLost(FocusEvent event)
    {
        for(int i = 0; i < keys.length; i++)
        {
            keys[i] =false;
        }
    }
    public void keyTyped(KeyEvent event)
    {
        
    }
    public void focusGained(FocusEvent event)
    {
        
    }
    
    public void tick() {
        up = keys[KeyEvent.VK_W];
        down = keys[KeyEvent.VK_S];
        left = keys[KeyEvent.VK_A];
        right = keys[KeyEvent.VK_D];
        Up = keys[KeyEvent.VK_UP];
        Down = keys[KeyEvent.VK_DOWN];
        Left = keys[KeyEvent.VK_LEFT];
        Right = keys[KeyEvent.VK_RIGHT];
        q = keys[KeyEvent.VK_Q];
        space = keys[KeyEvent.VK_SPACE];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (current != e.getKeyCode() && current != -1) {
            backup = current;
            keys[backup] = false;
        }
        current = e.getKeyCode();
        keys[current] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (backup != -1) { //pressing more than one keys

            if (e.getKeyCode() != backup) {
                keys[current] = false;
                keys[backup] = true;
                current = backup;
            }
            backup = -1;
        } else {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) {
                keys[keyCode] = true;
            } else if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
                keys[keyCode] = true;
            }
            keys[keyCode] = false;
            current = -1;
        }
    }
}
