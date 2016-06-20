package screens;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import assets.Images;
import main.Game;
import main.GameError;

public class EndOfGameScreen extends Screen {

	private int state;
	
	public EndOfGameScreen(Game game, int state){
		super(game);
		this.state = state;
	}
	
	@Override
	public void render(Graphics g) {
		Image image = Images.imageDraw;
		if(state==1)
			image = Images.imageVictory;
		else if(state==2)
			image = Images.imageDefeat;
		g.drawImage(image,(Game.getResX()-image.getWidth())/2f,50f);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(GameContainer gc) throws GameError {
		Input input = gc.getInput();
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON) ||
				input.isKeyPressed(Input.KEY_RETURN)){
			// on repasse sur l'écran de jeu
			this.game.newGame();
		}
	}

}
