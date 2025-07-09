/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.game;
import java.awt.image.BufferedImage;

/**
 *
 * @author USER
 */
public class SpriteSheet {
    private int[] pixels;
    private BufferedImage image;
    public final int SIZEX;
    public final int SIZEY;
    private Sprite[] loadedSprites = null;
    private boolean spritesLoaded = false;

    private int spriteSizeX;
    
    public SpriteSheet(BufferedImage sheetImage)
    {
        image = sheetImage;
        SIZEX = sheetImage.getWidth();
        SIZEY = sheetImage.getHeight();
        
        pixels = new int[SIZEX * SIZEY];
        pixels = sheetImage.getRGB(0, 0, SIZEX, SIZEY, pixels, 0, SIZEX);    
    }
    
    public BufferedImage crop(int x, int y, int width, int height) {
        //return image x position and y position
        return image.getSubimage(x, y, width, height);
    }
    
    public void loadSprites(int spriteSizeX,int spriteSizeY)
    {
        this.spriteSizeX = spriteSizeX; 
        loadedSprites = new Sprite[(SIZEX / spriteSizeX) * (SIZEY/ spriteSizeY)];
        
        int spriteID = 0;
        for (int y =0; y< SIZEY; y +=spriteSizeY)
        {
            for (int x = 0; x< SIZEX; x +=spriteSizeX)
            {
                loadedSprites[spriteID] = new Sprite(this, x, y, spriteSizeX, spriteSizeY);
                spriteID++;
            }
        }
        
        spritesLoaded = true;
    }
    
    public Sprite getSprite(int x,int y)
    {
        if (spritesLoaded)
        {
            int spriteID = x + y * (SIZEX / spriteSizeX);
            if (spriteID < loadedSprites.length)
            {
            return loadedSprites[spriteID];
            }
            else
                System.out.println("SpriteID:" + spriteID + " out of range");
        }
        else
            System.out.println("no sprite loaded");
        return null;
    }
    
    public Sprite[] getLoadedSprites() {
        return loadedSprites;
    }
    
    public int[] getPixels()
    {
        return pixels;
    }
    
    public BufferedImage getImage()
    {
        return image;
    }
}
