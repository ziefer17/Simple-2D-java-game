
package Content;

import java.awt.Color;

import java.awt.Graphics;

import com.mycompany.game.Game;
import com.mycompany.game.RenderHandler;

public class Tree extends Entity {

    private int type;

    public Tree(RenderHandler handler, float x, float y, int type) {
        super(handler, x, y, Tile.TILEWIDTH * 3, Tile.TILEHEIGHT * 3);
        this.type = type;
        bounds.width = this.width / 2 + 30;
        bounds.height = this.height / 6 + 1;
        bounds.x = Tile.TILEWIDTH * 2 - bounds.width;
        bounds.y = Tile.TILEHEIGHT * 2;

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Assets.trees[type], (int) (x - handler.getCamera().getxOffset()), (int) (y - handler.getCamera().getyOffset()), width, height, null);

        if (Game.showHitboxes) {
            g.setColor(Color.red);
            g.drawRect((int) (x + bounds.x - handler.getCamera().getxOffset()), (int) (y + bounds.y - handler.getCamera().getyOffset()), bounds.width, bounds.height);
        }
    }

}
