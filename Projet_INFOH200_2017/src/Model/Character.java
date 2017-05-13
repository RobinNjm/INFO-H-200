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
		 * fonction de base permettant à un perso de se déplacer
		 * dans le cas où la case de destination est libre
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
		 * permet de déterminer si une case est libre d'accès
		 */
		boolean obstacle = false;		//case libre par défaut
		for(GameObject object : game.getGameObjects()){
			if(object.isAtPosition(nextX, nextY)){
				if (!(object instanceof PushableBlock) && !(object instanceof Trap)){
					obstacle = object.isObstacle();
				} else {					//cas où l'objet est soit un piège ou un bloc poussable 
					 if (this instanceof Player){ //(effet spécial seulement pour le joueur (pas les ennemis)
						 if (object instanceof PushableBlock){
							 obstacle = ((PushableBlock) object).move(x, y, caseIsFree(nextX + x, nextY + y, x, y)); //demande au bloc d'essayer de bouger
						 } else {				//cas du piège
							 new Thread ((Trap) object).start();	//démarrage du thread du piège
						 }
					 } else{
						 obstacle = true;		//cas où ce n'est pas le joueur qui est considéré ----> obstacle
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
		 * détermine si l'attaque du laser peut apparaître sur cette case
		 * et dans le cas où un ennemi est atteint, lui enlève une vie
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
		 * enlève un certain nombre de point de vie au personnage considéré
		 */
		this.lifes = this.lifes - hurt;
		if(this.lifes == 0){
			if (this instanceof Monster){		//monstre mort ---> prévient game
				game.monsterDestroyed(this.posX, this.posY);
			}else if (this instanceof Player){	//joueur mort ---> prévient game
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
		 * détermine si le joueur est touché par une bombe
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
