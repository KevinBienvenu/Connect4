package screens;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import main.Game;
import main.GameError;

public abstract class Screen {
	
	Game game;
	
	public Screen(Game game){
		this.game = game;
	}
	
	public abstract void render(Graphics g);

	public abstract void init();

	public abstract void update(GameContainer gc) throws GameError ;
}
