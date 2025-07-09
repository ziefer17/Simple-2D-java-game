/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package States;


import java.awt.Color;

import java.awt.Graphics;

import com.mycompany.game.ClickListener;
import com.mycompany.game.RenderHandler;
import com.mycompany.game.UIImageButton;
import com.mycompany.game.UIManager;
import Content.Assets;

public class MenuState extends State {

    private UIManager uiManager;

    public MenuState(RenderHandler handler) {
        super(handler);
        uiManager = new UIManager(handler);
        handler.getMouseManager().setUIManager(uiManager);

        uiManager.addObject(new UIImageButton(200, 200, 128, 64, Assets.buttonStart, new ClickListener() {

            @Override
            public void onClick() {
                handler.getMouseManager().setUIManager(null);
                State.setState(handler.getGame().gameState);
            }
        }));
    }

    @Override
    public void tick() {
        uiManager.tick();
    }

    @Override
    public void render(Graphics g) {
        uiManager.render(g);
    }

}

