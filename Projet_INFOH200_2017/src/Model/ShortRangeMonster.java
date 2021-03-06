package Model;

import java.util.ArrayList;

public class ShortRangeMonster extends Monster{
	private Game game;
	private int sightDistance = 5;
	private int intervalRandom = 500;
	private int intervalRun = 200;
	private int intervalKill = 200;	
	
	public ShortRangeMonster(int x, int y, int lifes, int attack, Game game) {
		super(x, y, 2, lifes, attack, game);
		new Thread(this).start();
		this.game = game;
	}
	
	public void moveToKill(int deltaX, int deltaY){
		/*
		 * d�place le monstre vers le joueur
		 */
		if (Math.abs(deltaX) > Math.abs(deltaY)){	//choisi dans quelle direction se d�placer
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
		 * inflige des d�g�ts au joueur si le monstre a encore de la vie
		 */
		if (this.lifes > 0){
			player.removeLifes(attackValue);
		}
	}
	
	public synchronized int moveMonster(int deltaX, int deltaY, Player player){
		/*
		 * coordine les mouvements du monstre, d�terminant comment il doit bouger et si il doit attaquer
		 */
		int interval = intervalRandom;
		
		if (Math.abs(deltaX) < sightDistance && Math.abs(deltaY) < sightDistance){	//cas o� le joueur est dans la zone de l'ennemi
			if (Math.abs(deltaX) + Math.abs(deltaY) < 2){	//cas o� le joueur est contre l'ennemi
				interval = intervalKill;
				
				try {
					int count = 0;
					while(player.getPosX() - this.getPosX() + Math.abs(player.getPosY() - this.getPosY()) < 2 && count < intervalKill){
						Thread.sleep(50);	//v�rifie toutes les 50ms si le joueur est toujours l� et fait des d�g�ts
						count += 50;		//apr�s intervalKill ms
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
				this.moveToKill(deltaX, deltaY);	//mouvement vers le joueur dans le cas o� il est proche mais pas juste � c�t�
			}
		} else{
			this.moveRandom();	//mouvement al�atoire si le joueur n'est pas assez proche
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
					break;	//si le joueur est mort, le thread s'arr�te
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
