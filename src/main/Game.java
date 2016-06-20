
package main;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import assets.Images;
import game.Grille;
import ia.IAKevin01;
import screens.Screen;
import screens.ScreenGame;

public class Game extends BasicGame {

	protected static float resX, resY;
	
	protected Screen currentScreen;
	protected Grille grille;

	public Game(float resX, float resY) {
		super("Puissance 4");
		Game.resX = resX;
		Game.resY = resY;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		this.currentScreen.render(g);

	}

	@Override
	public void init(GameContainer arg0) throws SlickException {
		// On charge les images
		Images.init();
		this.newGame();		
	}

	@Override
	public void update(GameContainer gc, int arg1) throws SlickException {
		this.currentScreen.update(gc);
	}
	
	public void newGame(){
		// On intialise une partie
		this.grille = new Grille();
		Main.ia.init(grille);
		this.currentScreen = new ScreenGame(this, this.grille, Main.ia);
	}

	public void switchToScreen(Screen screen){
		this.currentScreen = screen;
	}
	
	public static float getResX(){
		return resX;
	}

	public static float getResY(){
		return resY;
	}
}