package main;

import org.newdawn.slick.SlickException;

public class GameError extends SlickException {
	
	public String message;
	
	public GameError(String message){
		super(message);
		System.out.println("Erreur dans le jeu");
		System.out.println("message :");
		System.out.println(message);
		this.message = message;
	}
}
