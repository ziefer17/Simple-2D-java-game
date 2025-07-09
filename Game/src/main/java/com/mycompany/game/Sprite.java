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
public class Sprite {
    private int width, height;
    private int[] pixels;
    
    public Sprite()
    {             
    }
    public Sprite(SpriteSheet sheet, int startX, int startY, int width, int height)
    {
        this.width = width;
        this.height = height;
        
        pixels = new int[width * height];
        sheet.getImage().getRGB(startX, startY, width, height, pixels, 0 , width);       
    }
    
    public Sprite(BufferedImage image)
    {
        width = image.getWidth();
        height = image.getHeight();
        
        pixels = new int[width*height];
        image.getRGB(0,0,width,height,pixels,0,width);
    }
    

    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public int[] getPixels()
    {
        return pixels;
    }
}
