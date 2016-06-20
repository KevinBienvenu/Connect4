package assets;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import screens.ScreenGame;

public class Images {

	public static Image imageJetonJaune;
	public static Image imageJetonRouge;
	public static Image imageJetonJauneDark;
	public static Image imageJetonRougeDark;

	public static Image imageDraw;
	public static Image imageVictory;
	public static Image imageDefeat;
	
	public static Image imageGrille;
	
	public static void init() throws SlickException{
		Images.imageJetonJaune = new Image("assets/jetonJaune.png").getScaledCopy(ScreenGame.coefZoomGrille);
		Images.imageJetonRouge = new Image("assets/jetonRouge.png").getScaledCopy(ScreenGame.coefZoomGrille);
		Images.imageJetonJauneDark = Images.imageJetonJaune.getScaledCopy(1f);
		Images.imageJetonRougeDark = Images.imageJetonRouge.getScaledCopy(1f);
		Images.imageJetonJauneDark.setAlpha(0.7f);
		Images.imageJetonRougeDark.setAlpha(0.7f);
		Images.imageVictory = new Image("assets/bulleRouge.png");
		Images.imageDefeat = new Image("assets/bulleJaune.png");
		Images.imageDraw = new Image("assets/bulleFinDuMatch.png");
		Images.imageGrille = new Image("assets/puissance4grille.png").getScaledCopy(ScreenGame.coefZoomGrille);
	}
	

}
