
package States;
import java.awt.Graphics;

import com.mycompany.game.RenderHandler;

public abstract class State {
	
	private static State currentState = null;
	
	public static void setState(State state) {
		currentState = state;
	}
	
	public static State getState() {
		return currentState;
	}
	
	protected RenderHandler handler;
	
	public State(RenderHandler handler) {
		this.handler = handler;
	}
	
	public abstract void tick();
	
	public abstract void render(Graphics g);	
}
