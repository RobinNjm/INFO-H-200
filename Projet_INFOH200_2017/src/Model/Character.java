package Model;

import java.util.ArrayList;

public abstract class Character extends GameObject implements DamageableObserver, Demisable{
	protected int lifes;
	protected int attackValue;
	protected Game game;
	 // TODO Vérifier si les protected sont nécessaires
	private ArrayList<DemisableObserver> observers = new ArrayList<DemisableObserver>();
	
	public Character (int x, int y, int color, int lifes, int attackValue, Game game){
		super(x, y, color);
		this.lifes = lifes;
		this.attackValue = attackValue;
		this.game = game;
	}
	
	public void move(int x, int y){
		int nextX = this.getPosX() + x;
		int nextY = this.getPosY() + y;
		
		if(caseIsFree(nextX, nextY)){
			posX = posX + x;
			posY = posY + y;
		}
		
	}
	
	public int getAttackValue(){
		return this.attackValue;
	}
	
	public int getLifes(){
		return this.lifes;
	}
	
	public synchronized boolean caseIsFree(int x, int y){
		boolean obstacle = false;
		for(GameObject object : game.getGameObjects()){
			if(object.isAtPosition(x, y)){
				obstacle = object.isObstacle();
			}
			if(obstacle){
				break;
			}
		}
		return !obstacle;
	}
	
	public void removeLifes(int hurt){
		this.lifes = this.lifes - hurt;
		if(this.lifes == 0){
			if (this instanceof Monster){
				game.monsterDestroyed(this.posX, this.posY);
			}else if (this instanceof Player){
				game.gameOver();
			}
			demisableNotifyObserver();
		}
	}
	
	@Override
	public void demisableAttach(DemisableObserver po) {
		observers.add(po);				
	}
	@Override
	public void demisableNotifyObserver() {
		for (DemisableObserver o : observers) {
			o.demise(this, null);
		}	
		
	}
	@Override
	public void damaged(Damageable e) {
		Bomb bomb = (Bomb) e;
		boolean distanceX = Math.abs(this.getPosX() - bomb.getPosX()) <= bomb.getRange();
		boolean distanceY = Math.abs(this.getPosY() - bomb.getPosY()) <= bomb.getRange();
		if(distanceX && distanceY){
			this.removeLifes(1);		
		}	
	}
	
	@Override
	public boolean isObstacle() {
		return true;
	}

}
