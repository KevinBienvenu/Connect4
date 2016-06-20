package ia;

import java.util.Vector;

import game.Case;

public class IAKevin02 extends IA{

	/*
	 *  IA qui vérifie si elle gagne en un coup
	 *  et empêche l'opposant de gagner en un coup
	 *  elle ne joue pas si son coup permet de faire gagner l'adversaire au prochain coup
	 */

	public IAKevin02(int idPlayer) {
		super(idPlayer);
	}

	
	@Override
	public int jouer() {
		if(getScoreGrille(3-idPlayer)<5){
			this.sendMessagePerso(messageBonjour);
		}
		int c;
		// joue si elle gagne
		c = this.checkIfWin();
		if(c>=0){
			this.sendMessagePerso(messageVictoire);
			return c;
		}
		// joue si elle perdrait sans jouer
		c = this.checkIfLoose();
		if(c>=0){
			this.sendMessagePerso(messageNiceMove);
			return c;
		}
		// checke les fourchettes
		c = this.checkFourchette();
		if(c>=0)
			return c;
		// checke les doubles fourchettes
		c = this.checkDoubleFourchette();
		if(c>=0){
			this.sendMessagePerso(messageBadMove);
			return c;
		}
		// evite de jouer aux endroits où elle perdrait sinon
		Vector<Integer> colonnesToAvoid = new Vector<Integer>();
		this.checkColonnesToAvoid(colonnesToAvoid);
		if(colonnesToAvoid.size()==grille.getNbColonnesDispo()){
			this.sendMessagePerso(messageDefaite);
			return this.comportementRandom();
		}
		
		// joue à l'endroit qui maximimise ses alignements
		int scoreMax = 0, score;
		int colonneMax = 0;
		int nLigne;
		for(int i=0; i<grille.sizeX; i++){
			if(colonnesToAvoid.contains(i)){
				continue;
			}
			nLigne = grille.getLigneVide(i);
			if(nLigne<0)
				continue;
			grille.getCase(i, nLigne).setState(this.idPlayer);
			score = this.getScoreGrille(this.idPlayer);
			if(score>scoreMax){
				scoreMax = score;
				colonneMax = i;
			}
			grille.getCase(i, nLigne).setState(3-this.idPlayer);
			score = this.getScoreGrille(3-this.idPlayer);
			if(score>scoreMax){
				scoreMax = score;
				colonneMax = i;
			}
			grille.getCase(i, nLigne).setState(0);
		}		
		// check if loose
		nLigne = grille.getLigneVide(colonneMax);
		grille.getCase(colonneMax, nLigne).setState(this.idPlayer);
		if(checkIfLoose()>=0){
			this.sendMessagePerso(this.messageDefaite);
		}
		grille.getCase(colonneMax, nLigne).setState(0);
		return colonneMax;
	}

	private int getScoreGrille(int idPlayer){
		int somme = 0;
		for(Vector<Case> vector: this.grille.vectorLignes){
			somme += Math.pow(this.grille.getLongestAlign(vector, idPlayer),3);
		}
		return somme;
	}

	private int checkIfWin(){
		int nLigne;
		for(int i=0; i<grille.sizeX; i++){
			nLigne = grille.getLigneVide(i);
			if(nLigne<0)
				continue;
			grille.getCase(i, nLigne).setState(this.idPlayer);
			if(grille.checkIfWin()==this.idPlayer){
				grille.getCase(i, nLigne).setState(0);
				return i;
			}
			grille.getCase(i, nLigne).setState(0);
		}
		return -1;
	}
	
	private int checkIfLoose(){
		int nLigne;
		for(int i=0; i<grille.sizeX; i++){
			nLigne = grille.getLigneVide(i);
			if(nLigne<0)
				continue;
			grille.getCase(i, nLigne).setState(3-this.idPlayer);
			if(grille.checkIfWin()==3-this.idPlayer){
				grille.getCase(i, nLigne).setState(0);
				return i;
			}
			grille.getCase(i, nLigne).setState(0);
		}
		return -1;
	}
	
	private int checkFourchette(){
		int nLigne, nLigne2;
		for(int i=0; i<grille.sizeX; i++){
			nLigne = grille.getLigneVide(i);
			if(nLigne<0)
				continue;
			grille.getCase(i, nLigne).setState(3-this.idPlayer);
			int nbWinPlace = 0;
			for(int j=0; j<grille.sizeX; j++){
				nLigne2 = grille.getLigneVide(j);
				if(nLigne2<0)
					continue;
				grille.getCase(j, nLigne2).setState(3-this.idPlayer);
				if(grille.checkIfWin()==3-this.idPlayer){
					nbWinPlace += 1;
				}
				grille.getCase(j, nLigne2).setState(0);
			}
			if(nbWinPlace>=2){
				grille.getCase(i, nLigne).setState(0);
				return i;
			}
			grille.getCase(i, nLigne).setState(0);
		}
		return -1;
	}
	
	private int checkDoubleFourchette(){
		int nLigne, nLigne2, nLigne3;
		for(int i=0; i<grille.sizeX; i++){
			nLigne = grille.getLigneVide(i);
			if(nLigne<0)
				continue;
			grille.getCase(i, nLigne).setState(3-this.idPlayer);
			for(int j=0; j<grille.sizeX; j++){
				nLigne2 = grille.getLigneVide(j);
				if(nLigne2<0)
					continue;
				grille.getCase(j, nLigne2).setState(3-this.idPlayer);
				if(grille.checkIfWin()==3-this.idPlayer){
					nLigne3 = nLigne2-1;
					if(nLigne3>=0){
						grille.getCase(j, nLigne2).setState(this.idPlayer);
						grille.getCase(j, nLigne3).setState(3-this.idPlayer);
						if(grille.checkIfWin()==3-this.idPlayer){
							grille.getCase(i, nLigne).setState(0);
							grille.getCase(j, nLigne2).setState(0);
							grille.getCase(j, nLigne3).setState(0);
							return i;
						}
						grille.getCase(j, nLigne3).setState(0);
					}
				}
				grille.getCase(j, nLigne2).setState(0);
			}
			grille.getCase(i, nLigne).setState(0);
		}
		return -1;
	}

	private void checkColonnesToAvoid(Vector<Integer> colonnesToAvoid){
		int nLigne;
		for(int i=0; i<grille.sizeX; i++){
			// où elle perdrait en un coup
			nLigne = grille.getLigneVide(i)-1;
			if(nLigne<0)
				continue;
			grille.getCase(i, nLigne).setState(3-this.idPlayer);
			if(grille.checkIfWin()==3-this.idPlayer){
				grille.getCase(i, nLigne).setState(0);
				colonnesToAvoid.add(i);
			}
			// où son adversaire pourrait faire une fourchette
			grille.getCase(i, nLigne).setState(this.idPlayer);
			int cF = this.checkFourchette();
			if(cF>=0)
				colonnesToAvoid.add(i);
			grille.getCase(i, nLigne).setState(0);
		}
	}

	private String generateRandomMessage(){
		String[] messages = {"Philippe !", "You'll never win !","Take this !","Nice Try"};
		if(Math.random()>0.5){
			return messages[(int)(Math.random()*messages.length)];
		}
		return "";
	}

	private void sendMessagePerso(String[] messages){
		this.sendMessage(messages[(int)(Math.random()*messages.length)]);
	}
	
	private String[] messageVictoire = {
			"I'm the best !", 
			"Go back to school :)",
			"You didn't see that coming, did you?",
			"Try again later... noob!"
	};
	private String[] messageDefaite = {
			"Nicely done, you win.",
			"Who, too strong for me",
			"Sorry I still have to learn..."
	};
	private String[] messageBadMove = {
			"Ouch, that hurts !",
			"Hmmm, nice move."
	};
	private String[] messageNiceMove = {
			"Take this !",
			"What are you going to do, hmm ?",
			"No one can defeat me... almost ;)",
			"You really thought it would be easy?"
	};
	private String[] messageBonjour = {
			"Hello !",
			"Hello !",
			"Hello !",
			"Hello !",
			"Fine since last time ?"
	};
}
