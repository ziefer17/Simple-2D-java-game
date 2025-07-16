/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.game;




public class RenderHandler {

	private Game game;
	private World world;
	
	public RenderHandler(Game game) {
		this.game = game;
	}
	
	public Rectangle getCamera() {
		return game.getCamera();
	}
	
	public KeyBoardListener getKeymanager() {
		return game.getKeyBoardListener();
	}
	
	public MouseEventListener getMouseManager() {
		return game.getMouseEventListener();
	}
	
	public int getWidth() {
		return game.getWidth();
	}
	
	public int getHeight() {
		return game.getHeight();
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
}

