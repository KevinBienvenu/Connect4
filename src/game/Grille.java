package game;

import java.util.Vector;

import main.Debug;
import main.GameError;
import screens.ScreenGame;

public class Grille {

	private Case[][] cases;

	public static final int sizeX = 7;
	public static final int sizeY = 6;

	protected int nColonneSouris = -1;

	public Vector<Vector<Case>> vectorLignes;

	public Grille(){
		this.cases = new Case[sizeY][sizeX];
		for(int x = 0; x<sizeX; x++){
			for(int y = 0; y<sizeY; y++){
				this.cases[y][x] = new Case(x,y);
			}
		}
		this.createVectors();
	}

	public Grille(Grille grille){
		this.cases = new Case[sizeY][sizeX];
		for(int x = 0; x<sizeX; x++){
			for(int y = 0; y<sizeY; y++){
				this.cases[y][x] = new Case(x,y,grille.getCase(x, y).getState());
			}
		}
		this.createVectors();
	}

	private void createVectors(){
		vectorLignes = new Vector<Vector<Case>>();
		// creation des lignes
		for(int nLigne=0; nLigne<sizeY; nLigne++){
			vectorLignes.add(new Vector<Case>());
			for(int nColonne=0; nColonne<sizeX; nColonne++){
				vectorLignes.get(vectorLignes.size()-1).add(this.cases[nLigne][nColonne]);
			}
		}
		// création des colonnes
		for(int nColonne=0; nColonne<sizeX; nColonne++){
			vectorLignes.add(new Vector<Case>());
			for(int nLigne=0; nLigne<sizeY; nLigne++){
				vectorLignes.get(vectorLignes.size()-1).add(this.cases[nLigne][nColonne]);
			}
		}
		// création des diagonales 0
		for(int nDiag = -2; nDiag<4; nDiag++){
			vectorLignes.add(new Vector<Case>());
			int x = nDiag;
			for(int nLigne=0; nLigne<sizeY; nLigne++){
				if(x>=0 && x<sizeX){
					vectorLignes.get(vectorLignes.size()-1).add(this.cases[nLigne][x]);
				}
				x++;
			}
		}
		// création des diagonales 1
		for(int nDiag = 3; nDiag<9; nDiag++){
			vectorLignes.add(new Vector<Case>());
			int x = nDiag;
			for(int nLigne=0; nLigne<sizeY; nLigne++){
				if(x>=0 && x<sizeX){
					vectorLignes.get(vectorLignes.size()-1).add(this.cases[nLigne][x]);
				}
				x--;
			}
		}
	}



	public void jouerUnCoup(int idPlayer, int nColonne) throws GameError{
		// check if colonne non remplie
		if(this.cases[0][nColonne].getState()>0){
			// colonne remplie, on renvoie une erreur
			throw new GameError("colonne remplie : "+nColonne);
		}
		int nLigne = getLigneVide(nColonne);
		this.cases[nLigne][nColonne].setState(idPlayer);
		if(Debug.debugGrille)
			System.out.println("coup joué en "+nLigne+","+nColonne+" par le joueur "+idPlayer);
	}

	public void afficherDebugGrille(){
		for(int y=0; y<sizeY; y++){
			for(int x=0; x<sizeX; x++){
				System.out.print(this.cases[y][x].getState());
			}
			System.out.println();
		}
	}

	public int checkIfWin(){
		int a = 0;
		// check lignes
		for(Vector<Case> v : vectorLignes){
			a = this.checkIfWinSurVector(v);
			if(a>0){
				return a;
			}
		}
		return 0;
	}

	public Case getCase(int x, int y){
		return this.cases[y][x];
	}

	public int checkIfWinSurVector(Vector<Case> vector){
		int idPlayer = 0;
		int compteur = 0;
		for(Case cas : vector){
			if(idPlayer==cas.getState()){
				compteur++;
				if(compteur==4 && idPlayer!=0){
					return idPlayer;
				}
			} else {
				compteur = 1;
				idPlayer = cas.getState();
			}
		}
		return 0;
	}

	public int getLongestAlign(Vector<Case> vector, int idPlayer){
		boolean flag = false;
		int compteur = 0;
		int maxCompteur = 0;
		for(Case cas : vector){
			if(idPlayer==cas.getState()){
				compteur++;
				flag = false;
			} else {
				if(cas.getState()== 3-idPlayer || (cas.getState()==0 && flag)){
					maxCompteur = Math.max(compteur, maxCompteur);
					compteur = 0;
				} else {
					flag = true;
				}
			}
		} 
		maxCompteur = Math.max(compteur, maxCompteur);
		compteur = 0;
		return maxCompteur;
	}

	public Vector<Case> getWinningCases(){
		Vector<Case> cases = new Vector<Case>();
		for(Vector<Case> vector : this.vectorLignes){
			int idPlayer = 0;
			int compteur = 0;
			for(Case cas : vector){
				if(idPlayer==cas.getState()){
					compteur++;
					cases.add(cas);
					if(compteur==4 && idPlayer!=0){
						return cases;
					}
				} else {
					cases.clear();
					cases.add(cas);
					compteur = 1;
					idPlayer = cas.getState();
				}
			}
		}
		return new Vector<Case>();
	}

	public void majColonneSouris(int xMouse, int yMouse, int idPlayer){
		Case c;
		int nLigne = 0;
		for(int i=0; i<sizeX; i++){
			c = this.cases[0][i];
			if(xMouse>c.getXToDraw() && xMouse<c.getXToDraw()+ScreenGame.sizeJetonX){
				this.nColonneSouris = i;
				nLigne = this.getLigneVide(i);
				if(nLigne>=0){
					this.cases[this.getLigneVide(i)][i].setState(2+idPlayer);
				}
				return;
			}
		}

	}

	public void cleanGrille(){
		this.nColonneSouris = -1;
		for(int i=0; i<sizeX; i++){
			for(int j=0; j<sizeY; j++){
				if(this.cases[j][i].getState()>2)
					this.cases[j][i].setState(0);
			}
		}
	}

	public int getLigneVide(int nColonne){
		int nLigne = 0;
		if(nColonne==-1){
			return -1;
		}
		while(nLigne<sizeY && this.cases[nLigne][nColonne].isEmpty()){
			nLigne ++;
		}
		nLigne--;
		return nLigne;
	}

	public int getNColonneSouris(){
		return nColonneSouris;
	}

	public int getNbColonnesDispo(){
		int nb = 0;
		for(int i=0; i<sizeX; i++){
			if(this.getLigneVide(i)>=0){
				nb++;
			}
		}
		return nb;
	}

	public void printColonne(){
		// WARNING DESTRUCTION DE LA GRILLE !!!
		for(Vector<Case> vector: this.vectorLignes){
			this.cleanGrille();
			for(Case c : vector){
				c.setState(3);
			}
			this.afficherDebugGrille();
			System.out.println("");
		}
	}
}
