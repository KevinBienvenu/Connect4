package ia;

import game.Grille;

public class IAKevin01 extends IA{
	/*
	 * IA random
	 */

	public IAKevin01(int idPlayer){
		super(idPlayer);
	}
	
	@Override
	public int jouer() {
		return this.comportementRandom();
	}

}
