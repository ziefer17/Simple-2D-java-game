/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Content;

import java.awt.Graphics;

import com.mycompany.game.RenderHandler;

public class Description {

    private Text nameText;
    private String name;
    private int level;
    private int x;
    private int y;
    private Level levelDescription;
    private HealthBar healthBar;
    private XPBar xpBar;
    private int baseHealth;
    private int health;
    private int type;
    private RenderHandler handler;

    public Description(int type, String name, int health, int baseHealth, int level, int x, int y, RenderHandler handler) {
        // Validate name to contain only letters, spaces, or '!'
        String safeName = name.replaceAll("[^a-zA-Z !]", ""); // Remove invalid characters
        if (safeName.isEmpty()) {
            safeName = "Unknown"; // Fallback name
        }
        System.out.println("Creating Description with name: '" + safeName + "', health: " + health + "/" + baseHealth);
        nameText = new Text(safeName, x - 4, y + 22, 4, -1);
        this.handler = handler;
        this.type = type;
        this.name = safeName;
        this.level = level;
        this.x = x;
        this.y = y;
        this.health = health;
        this.baseHealth = baseHealth;
        levelDescription = new Level(type, level, x, y);
        healthBar = new HealthBar(type, x, y, handler);
        xpBar = new XPBar(x, y);
    }

    public Description(int type, String name, int health, int baseHealth, int level, RenderHandler handler) {
        this(type, name, health, baseHealth, level, 0, 0, handler);
    }

    public void tick() {
        healthBar.tick();
    }

    public void render(Graphics g) {
        try {
            if (type == 0 || type == 1) {
                g.drawImage(Assets.enemyDescription, x, y, 102 * 4, 33 * 4, null);
                levelDescription.render(g);
                nameText.render(g);
                healthBar.render(g);
            } else if (type == 2) {
                xpBar.render(g);
                healthBar.render(g);
                g.drawImage(Assets.playerDescription, 0, 0, 105 * 4, 38 * 4, null);
            }
        } catch (Exception e) {
            System.err.println("Error rendering Description for name: " + name);
            e.printStackTrace();
        }
    }

}
