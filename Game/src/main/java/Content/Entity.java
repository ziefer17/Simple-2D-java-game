/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Content;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.mycompany.game.RenderHandler;

public abstract class Entity {

    protected RenderHandler handler;
    protected float x, y;
    protected int width, height;
    protected Rectangle bounds;
    public int id;
    
    

    public Entity(RenderHandler handler, float x, float y, int width, int height) {
        this.handler = handler;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        bounds = new Rectangle(0, 0, width, height);
    }
    
    public Entity(int id, RenderHandler handler, float x, float y, int width, int height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.handler = handler;
        this.width = width;
        this.height = height;
        bounds = new Rectangle(0, 0, width, height);
    }

    public abstract void tick();

    public abstract void render(Graphics g);

    public boolean checkEntityCollisions(float xOffset, float yOffset) {
        for (Entity e : handler.getWorld().getEntityManager().getEntities()) {
            if (e.equals(this)) {
                continue;
            }
            if (e.getCollisionBounds(0f, 0f).intersects(getCollisionBounds(xOffset, yOffset))) {
                Creature.collided = true;
                return true;
            }
        }
        return false;
    }

    public Rectangle getCollisionBounds(float xOffset, float yOffset) {
        return new Rectangle((int) (x + bounds.x + xOffset), (int) (y + bounds.y + yOffset), bounds.width, bounds.height);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public void updatePlayerPosition(int id, float x, float y) {
        if (this.id == id) {
            this.x = x;
            this.y = y;
            System.out.println("Updated entity (ID: " + id + ") to position (" + x + ", " + y + ")");
        } else {
            System.out.println("Player with ID " + id + " not found.");
        }
    }
}
