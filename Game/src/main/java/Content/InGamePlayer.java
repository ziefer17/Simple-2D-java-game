/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Content;

import java.awt.Color;
import java.awt.Graphics;

import com.mycompany.game.RenderHandler;

public class InGamePlayer {

    Description description;
    RenderHandler handler;

    public InGamePlayer(RenderHandler handler) {
        this.handler = handler;
        description = new Description(1, Player.name != null ? Player.name : "Hero", Player.health, Player.baseHealth, Player.level, 392, 396, handler);
    }

    public void tick() {
        description.tick();
    }

    public void render(Graphics g) {
        try {
            description.render(g);
            Color c = new Color(184, 184, 184);
            g.setColor(c);
        } catch (Exception e) {
            System.err.println("Error rendering InGamePlayer");
            e.printStackTrace();
        }
    }

}

