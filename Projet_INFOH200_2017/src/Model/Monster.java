package Model;

import java.util.ArrayList;
import java.util.Random;
import Model.Game;

public class Monster extends Character implements Runnable, DamageableObserver, Demisable{
	
	private Game game;
	private int sightDistance = 5;
	private int intervalRandom = 500;
	private int intervalRun = 200;
	private int intervalKill = 300;
	
	public Monster(int x, int y, int lifes, int attack, Game game){
		super(x, y, 2, lifes, attack, game);
		new Thread(this).start();
		this.game = game;
	}
	
	Random rd = new Random();
	
	public void moveRandom(){
		/*
		 * déplace le monstre aléatoirement
		 */
		int direction = rd.nextInt(4);
		int x = 0;
		int y = 0;
		if(direction == 0){          //vers la droite
			x = 1;
		} else if(direction == 1){   //vers le bas
			y = 1;
		} else if(direction == 2){   //vers la gauche
			x = -1;
		} else{                      //vers le haut
			y = -1;
		}
		this.move(x, y);
	}
	
	public void moveToKill(int deltaX, int deltaY){
		/*
		 * déplace le monstre vers le joueur
		 */
		if (Math.abs(deltaX) > Math.abs(deltaY)){	//choisi dans quelle direction se déplacer
			if(deltaX < 0){
				this.move(-1, 0);
			} else{
				this.move(1, 0);
			}
		} else if(deltaY < 0){
			this.move(0, -1);
		} else{
			this.move(0, 1);
		}	
	}
	
	public void kill(Player player){
		/*
		 * inflige des dégâts au joueur si le monstre a encore de la vie
		 */
		if (this.lifes > 0){
			player.removeLifes(attackValue);
		}
	}
	
	public synchronized int moveMonster(int deltaX, int deltaY, Player player){
		/*
		 * coordine les mouvements du monstre, déterminant comment il doit bouger et si il doit attaquer
		 */
		int interval = intervalRandom;
		
		if (Math.abs(deltaX) < sightDistance && Math.abs(deltaY) < sightDistance){	//cas où le joueur est dans la zone de l'ennemi
			if (Math.abs(deltaX) + Math.abs(deltaY) < 2){	//cas où le joueur est contre l'ennemi
				interval = intervalKill;
				
				try {
					int count = 0;
					while(player.getPosX() - this.getPosX() + Math.abs(player.getPosY() - this.getPosY()) < 2 && count < intervalKill){
						Thread.sleep(50);	//vérifie toutes les 50ms si le joueur est toujours là et fait des dégâts
						count += 50;		//après intervalKill ms
					}
					if (Math.abs(player.getPosX() - this.getPosX()) + Math.abs(player.getPosY() - this.getPosY()) < 2){
						this.kill(player);	//fait perdre de la vie au joueur
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			else{
				interval = intervalRun;
				this.moveToKill(deltaX, deltaY);	//mouvement vers le joueur dans le cas où il est proche mais pas juste à côté
			}
		} else{
			this.moveRandom();	//mouvement aléatoire si le joueur n'est pas assez proche
		}
		
		return interval;
	}

	@Override
	public synchronized void run() {
		try{
			while (true){	//tourne en boucle tout le temps de vie de l'ennemi
				ArrayList<GameObject> objects = game.getGameObjects();
				Player player = ((Player) objects.get(0));
				int xPlayer = player.getPosX();
				int yPlayer = player.getPosY();
				int xMonster = this.getPosX();
				int yMonster = this.getPosY();
				int deltaX = xPlayer - xMonster;
				int deltaY = yPlayer - yMonster;
				int interval = this.moveMonster(deltaX, deltaY, player);
				game.notifyView();
				Thread.sleep(interval);
				if (player.getLifes() < 1){
					break;	//si le joueur est mort, le thread s'arrête
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
