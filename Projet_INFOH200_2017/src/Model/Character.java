package Model;

import java.util.ArrayList;

public abstract class Character extends GameObject implements DamageableObserver, Demisable{
	/*
	 *  classe parente de tous les personnages du jeu
	 */
	protected int lifes;
	protected int attackValue;
	protected Game game;
	private ArrayList<DemisableObserver> observers = new ArrayList<DemisableObserver>();
	
	public Character (int x, int y, int color, int lifes, int attackValue, Game game){
		super(x, y, color);
		this.lifes = lifes;
		this.attackValue = attackValue;
		this.game = game;
	}
	
	public void move(int x, int y){
		/*
		 * fonction de base permettant � un perso de se d�placer
		 * dans le cas o� la case de destination est libre
		 */
		int nextX = this.getPosX() + x;
		int nextY = this.getPosY() + y;
		
		if(caseIsFree(nextX, nextY, x, y)){
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
	
	public synchronized boolean caseIsFree(int nextX, int nextY, int x, int y){
		/*
		 * permet de d�terminer si une case est libre d'acc�s
		 */
		boolean obstacle = false;		//case libre par d�faut
		for(GameObject object : game.getGameObjects()){
			if(object.isAtPosition(nextX, nextY)){
				if (!(object instanceof PushableBlock) && !(object instanceof Trap)){
					obstacle = object.isObstacle();
				} else {					//cas o� l'objet est soit un pi�ge ou un bloc poussable 
					 if (this instanceof Player){ //(effet sp�cial seulement pour le joueur (pas les ennemis)
						 if (object instanceof PushableBlock){
							 obstacle = ((PushableBlock) object).move(x, y, caseIsFree(nextX + x, nextY + y, x, y)); //demande au bloc d'essayer de bouger
						 } else {				//cas du pi�ge
							 new Thread ((Trap) object).start();	//d�marrage du thread du pi�ge
						 }
					 } else{
						 obstacle = true;		//cas o� ce n'est pas le joueur qui est consid�r� ----> obstacle
					 }
				}
			}
			if(obstacle){
				break;
			}
		}
		return !obstacle;
	}
	
	public synchronized boolean caseIsKillable(int x, int y){
		/*
		 * d�termine si l'attaque du laser peut appara�tre sur cette case
		 * et dans le cas o� un ennemi est atteint, lui enl�ve une vie
		 */
		boolean isKillable = true;
		for(GameObject object : game.getGameObjects()){
			if(object.isAtPosition(x, y)){
				if (object instanceof Block){
					isKillable = false;
				} else if (object instanceof Monster){
					isKillable = false;
					((Monster) object).removeLifes(attackValue);
				}
			}
		}
		return isKillable;
	}
	
	public void removeLifes(int hurt){
		/*
		 * enl�ve un certain nombre de point de vie au personnage consid�r�
		 */
		this.lifes = this.lifes - hurt;
		if(this.lifes == 0){
			if (this instanceof Monster){		//monstre mort ---> pr�vient game
				game.monsterDestroyed(this.posX, this.posY);
			}else if (this instanceof Player){	//joueur mort ---> pr�vient game
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
		/*
		 * d�termine si le joueur est touch� par une bombe
		 */
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
