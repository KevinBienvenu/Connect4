package screens;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import assets.Images;
import game.Case;
import game.Grille;
import ia.IA;
import main.Game;
import main.GameError;

public class ScreenGame extends Screen{

	private Grille grille;

	public static final float offsetGrilleX = 100f;
	public static final float offsetGrilleY = 200f;

	public static final float coefZoomGrille = 0.8f;

	public static final float sizeGrilleX = 1100f * coefZoomGrille;
	public static final float sizeGrilleY = 950f * coefZoomGrille;
	public static final float offsetJetonX = (sizeGrilleX/11.82f);
	public static final float offsetJetonY = (sizeGrilleY/10.22f);
	public static final float sizeJetonX = 130f * coefZoomGrille;
	public static final float sizeJetonY = 130f * coefZoomGrille;

	public int currentPlayer = 1;

	// regarding cosmetic
	private boolean isBlocked = false;
	private int nColonnePlayed = 0;
	private Case droppingJeton = null;
	private int caseToGo = 0;
	private int nbFrameSkipped = 0;
	private int nbFrameToSkip = 3;

	// regarding IA
	private boolean joueur2IA = false;
	private IA ia;
	private String messageIA = "";

	public ScreenGame(Game game, Grille grille){
		super(game);
		this.grille = grille;
		this.joueur2IA = false;
	}

	public ScreenGame(Game game, Grille grille, IA ia){
		super(game);
		this.grille = grille;
		this.joueur2IA = true;
		this.ia = ia;
	}

	@Override
	public void render(Graphics g) {
		// On affiche le fond
		g.setColor(Color.white);
		g.fillRect(0, 0, Game.getResX(), Game.getResY());
		// On dessine tous les jetons
		int a;
		for(int y=0; y<grille.sizeY; y++){
			for(int x=0; x<grille.sizeX; x++){
				a = grille.getCase(x,y).getState();
				if(a>0){
					if(a==1){
						g.drawImage(Images.imageJetonRouge, grille.getCase(x,y).getXToDraw(), grille.getCase(x, y).getYToDraw());
					} else if(a==2) {
						g.drawImage(Images.imageJetonJaune, grille.getCase(x,y).getXToDraw(), grille.getCase(x, y).getYToDraw());
					} else if(a==3){
						g.drawImage(Images.imageJetonRougeDark, grille.getCase(x,y).getXToDraw(), grille.getCase(x, y).getYToDraw());
					} else if(a==4) {
						g.drawImage(Images.imageJetonJauneDark, grille.getCase(x,y).getXToDraw(), grille.getCase(x, y).getYToDraw());
					}
				}
			}
		}
		// On dessine l'animation en cours
		if(droppingJeton!=null){
			if(droppingJeton.getState()==1){
				g.drawImage(Images.imageJetonRouge, droppingJeton.getXToDraw(), droppingJeton.getYToDraw());
			} else if (droppingJeton.getState()==2){
				g.drawImage(Images.imageJetonJaune, droppingJeton.getXToDraw(), droppingJeton.getYToDraw());
			}
		}
		// On dessine la grille
		g.drawImage(Images.imageGrille, offsetGrilleX, offsetGrilleY);

		// On affiche le message de l'IA
		if(this.joueur2IA && this.ia!=null && !this.ia.getMessage().equals("")){
			g.setColor(Color.black);
			g.drawString("IA :"+this.ia.getMessage(),50,50);

		}

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	private void handleEndOfGame(){
		// check win
		int a = this.grille.checkIfWin();
		if(a>0){
			if(!this.isBlocked){
				this.isBlocked = true;
				Vector<Case> vector = this.grille.getWinningCases();
				this.grille.cleanGrille();
				for(int x=0; x<grille.sizeX; x++){
					for(int y=0; y<grille.sizeY; y++){
						if(this.grille.getCase(x, y).getState()>0 && !vector.contains(this.grille.getCase(x, y))){
							this.grille.getCase(x, y).setState(this.grille.getCase(x, y).getState()+2);
						}
					}
				}
			}
			if(this.nbFrameSkipped>=this.nbFrameToSkip){
				this.game.switchToScreen(new EndOfGameScreen(this.game, a));
			}
		}
		// check draw
		boolean flag = true;
		for(int i=0; i<grille.sizeX; i++){
			flag = flag && !grille.getCase(i, 0).isEmpty();
		}
		if(flag){
			if(!this.isBlocked){
				this.isBlocked = true;
			}
			if(this.nbFrameSkipped>=this.nbFrameToSkip){
				this.game.switchToScreen(new EndOfGameScreen(this.game, 0));
			}
		}
	}

	private void jouerUnCoup(int idPlayer, int nColonne){
		this.isBlocked = true;
		this.droppingJeton = new Case(nColonne, -1, idPlayer);
		this.caseToGo = this.grille.getLigneVide(nColonne);
		this.nbFrameSkipped = 0;
		this.nColonnePlayed = nColonne;
	}

	private void handleAnimationJeton() throws GameError{
		this.nbFrameSkipped++;
		if(this.nbFrameSkipped>=this.nbFrameToSkip){
			this.nbFrameSkipped=0;
			this.droppingJeton.dropY();
			this.caseToGo--;
			if(this.caseToGo<=0){
				this.grille.jouerUnCoup(this.droppingJeton.getState(), this.nColonnePlayed);
				this.droppingJeton = null;
				this.isBlocked = false;
			}
		}
	}

	@Override
	public void update(GameContainer gc) throws GameError {
		this.handleEndOfGame();
		Input input = gc.getInput();
		// jouer un tour
		if(!this.isBlocked){ 
			this.grille.cleanGrille();
			if(this.currentPlayer == 1 || !this.joueur2IA){
				this.grille.majColonneSouris(input.getAbsoluteMouseX(), input.getAbsoluteMouseY(), this.currentPlayer);
				boolean isMouseLeftClick = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
				if(isMouseLeftClick && this.grille.getLigneVide(this.grille.getNColonneSouris())>=0){
					this.jouerUnCoup(this.currentPlayer, this.grille.getNColonneSouris());
					this.currentPlayer = 3 - this.currentPlayer;
				}
			} else {
				this.jouerUnCoup(this.currentPlayer, this.ia.jouerIA());
				this.currentPlayer = 3 - this.currentPlayer;
			}
		} else {
			if(this.droppingJeton!=null){
				this.grille.cleanGrille();
				this.handleAnimationJeton();
			} else {
				this.nbFrameSkipped++;
			}
		}

	}
}
