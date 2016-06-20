package ia;

import java.util.Vector;

import game.Grille;

public abstract class IA {

	private Grille grilleOrigin;
	protected Grille grille;
	private String message;
	
	protected int idPlayer;
	
	public IA(int idPlayer){
		this.idPlayer = idPlayer;
	}
	
	/**
	 * Fonction à overrider
	 */
	protected abstract int jouer();
	
	/**
	 * Fonction d'initialisation de l'IA
	 */
	public final void init(Grille grille){
		this.message = "";
		this.grilleOrigin = grille;
	}
	
	/**
	 * Fonction appelée dans la boucle principale du jeu
	 */
	public final int jouerIA(){
		int a = 0;
		this.message = "";
		this.regenerateGrille();
		a = this.jouer();
		return a;
	}


	/**
	 * Fonction qui (re)set la grille dans l'état initial du tour
	 */
	protected final void regenerateGrille(){
		this.grille = new Grille(this.grilleOrigin);
	}
	
	/**
	 * Fonction qui renvoie une colonne possible au hasard
	 */
	protected final int comportementRandom(){
		while(true){
			int a = (int)(Math.random()*7);
			if(this.grille.getLigneVide(a)>=0){
				return a;
			}
		}
	}
	
	/**
	 * Fonction qui renvoie une colonne possible au hasard 
	 * en évitant si possible les colonnes dans colonnesToAvoid
	 */
	protected final int comportementRandom(Vector<Integer> colonnesToAvoid){
		if(colonnesToAvoid.size()==this.grille.getNbColonnesDispo()){
			return comportementRandom();
		}
		while(true){
			int a = (int)(Math.random()*7);
			if(!colonnesToAvoid.contains(a) && this.grille.getLigneVide(a)>=0){
				return a;
			}
		}
	}
	
	/**
	 * Fonction qui permet de dialoguer avec les autres utilisateurs
	 * en envoyant un message en paramètre.
	 * @param message
	 */
	protected void sendMessage(String message){
		this.message = message;
	}
	
	/**
	 * Fonction qui renvoit le message contenu dans l'IA
	 * (notamment utilisé par l'interface graphique pour l'utiliser
	 * @return message
	 */
	public final String getMessage(){
		if(this.message!=null)
			return this.message;
		else
			return "";
	}
	
}
