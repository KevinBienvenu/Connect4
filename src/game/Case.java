package game;

import screens.ScreenGame;

public class Case {
	
	private float xToDraw, yToDraw;
	
	private int state;
	
	public Case(int x, int y){
		this.xToDraw = ScreenGame.offsetGrilleX+ScreenGame.offsetJetonX+ScreenGame.sizeJetonX*x;
		this.yToDraw = ScreenGame.offsetGrilleY+ScreenGame.offsetJetonY+ScreenGame.sizeJetonY*y;
		this.state = 0;
	}
	
	public Case(int x, int y, int state){
		this.xToDraw = ScreenGame.offsetGrilleX+ScreenGame.offsetJetonX+ScreenGame.sizeJetonX*x;
		this.yToDraw = ScreenGame.offsetGrilleY+ScreenGame.offsetJetonY+ScreenGame.sizeJetonY*y;
		this.state = state;
	}
	
	public int getState(){
		return this.state;
	}
	
	public void setState(int state){
		this.state = state;
	}

	public float getXToDraw(){
		return xToDraw;
	}
	
	public float getYToDraw(){
		return yToDraw;
	}
	
	public boolean isEmpty(){
		return this.state==0 || this.state>2;
	}
	
	public void dropY(){
		this.yToDraw+=ScreenGame.sizeJetonY;
	}
}
