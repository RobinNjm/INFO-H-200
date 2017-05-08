package Model;


public abstract class GameObject {
	protected int posX;
	protected int posY;
	protected int color;
	
	public GameObject(int x, int y, int color){
		posX = x;
		posY = y;
		this.color = color;
	}
	
	public int getPosX(){
		return this.posX;
	}
	
	public int getPosY(){
		return this.posY;
	}
	
	public int getColor(){
		return this.color;
	}
	
	public boolean isAtPosition(int x, int y){
		return posX == x && posY == y;
	}
	
	public abstract boolean isObstacle();

}
