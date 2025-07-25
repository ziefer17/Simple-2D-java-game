
package com.mycompany.game;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class UIManager {

    private RenderHandler handler;
    private ArrayList<UIObject> objects;

    public UIManager(RenderHandler handler) {
        this.setHandler(handler);
        objects = new ArrayList<UIObject>();
    }

    public void tick() {
        for (UIObject o : objects) {
            o.tick();
        }
    }

    public void render(Graphics g) {
        for (UIObject o : objects) {
            o.render(g);
        }
    }

    public void onMouseMove(MouseEvent e) {
        for (UIObject o : objects) {
            o.onMouseMove(e);
        }
    }

    public void onMouseRelease(MouseEvent e) {
        for (UIObject o : objects) {
            o.onMouseRelease(e);
        }
    }

    public void addObject(UIObject o) {
        objects.add(o);
    }

    public void removeObject(UIObject o) {
        objects.remove(o);
    }

    public RenderHandler getHandler() {
        return handler;
    }

    public void setHandler(RenderHandler handler) {
        this.handler = handler;
    }

}
