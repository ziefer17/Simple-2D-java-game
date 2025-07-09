package com.mycompany.game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseEventListener implements MouseListener, MouseMotionListener {

    private Game game;
    private boolean leftPressed, rightPressed;
    private int mouseX, mouseY;
    private UIManager uiManager;

//    public MouseEventListener(Game game) {
//        this.game = game;
//    }

    public void setUIManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void mouseClicked(MouseEvent event) {

    }

    public void mouseDragged(MouseEvent event) {

    }

    public void mouseEntered(MouseEvent event) {

    }

    public void mouseExited(MouseEvent event) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        if (uiManager != null) {
            uiManager.onMouseMove(e);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) { //left click
            leftPressed = true;
        } else if (e.getButton() == MouseEvent.BUTTON3) { //right click
            rightPressed = true;
        }

        if (uiManager != null) {
            uiManager.onMouseRelease(e);
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) { //left click
            leftPressed = false;
        } else if (e.getButton() == MouseEvent.BUTTON3) { //right click
            rightPressed = false;
        }
    }
}
