package main;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Vector;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import game.Case;
import game.Grille;
import ia.IA;
import ia.IAKevin02;


public class Main {

	public static boolean pleinEcran = false;
	
	public static IA ia = new IAKevin02(2);

	public static void main(String[] args) throws GameError {


		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		int resolutionX;
		int resolutionY;
		if(pleinEcran){
			resolutionX = (int)screenSize.getWidth();		
			resolutionY = (int)screenSize.getHeight();
		} else {
			resolutionX = 1100;		
			resolutionY = 950;
		}
		try {
			Game game = new Game(resolutionX,resolutionY);
			AppGameContainer app = new AppGameContainer( game );
			app.setShowFPS(true);
			app.setDisplayMode(resolutionX, resolutionY,pleinEcran);
			app.setAlwaysRender(false);
			app.setUpdateOnlyWhenVisible(false);
			app.setClearEachFrame(false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}



}
