package Model;

import java.util.ArrayList;
import java.util.Random;
import Model.Game;

public class Monster extends Character implements Runnable, DamageableObserver, Demisable{
	
	private Game game;
	private int sightDistance = 5;
	private int intervalRandom = 500;
	private int intervalRun = 300;
	private int intervalKill = 100;
	
	public Monster(int x, int y, int lifes, int attack, Game game){
		super(x, y, 2, lifes, attack, game);
		new Thread(this).start();
		this.game = game;
	}
	
	Random rd = new Random();
	
	public void moveRandom(){
		
		int direction = rd.nextInt(4);
		int x = 0;
		int y = 0;
		if(direction == 0){          //vers la droite
			x = 1;
		} else if(direction == 1){   //bas
			y = 1;
		} else if(direction == 2){   //gauche
			x = -1;
		} else{                      //haut
			y = -1;
		}
		this.move(x, y);
	}
	
	public void moveToKill(int deltaX, int deltaY){
		if (Math.abs(deltaX) > Math.abs(deltaY)){
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
			if (this.lifes > 0){
				player.removeLifes(1);
			}
	}
	
	public int moveMonster(int deltaX, int deltaY, Player player){
		int interval = intervalRandom;
		
		if (Math.abs(deltaX) < sightDistance && Math.abs(deltaY) < sightDistance){
			if (Math.abs(deltaX) + Math.abs(deltaY) < 2){
				interval = intervalKill;
				
				try {
					int count = 0;
					while(player.getPosX() - this.getPosX() + Math.abs(player.getPosY() - this.getPosY()) < 2 && count < intervalKill){
						Thread.sleep(50);
						count += 50;
					}
					if (Math.abs(player.getPosX() - this.getPosX()) + Math.abs(player.getPosY() - this.getPosY()) < 2){
						this.kill(player);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			else{
				interval = intervalRun;
				this.moveToKill(deltaX, deltaY);
			}
		} else{
			this.moveRandom();
		}
		
		return interval;
	}

	@Override
	public synchronized void run() {
		try{
			while (true){
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
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
