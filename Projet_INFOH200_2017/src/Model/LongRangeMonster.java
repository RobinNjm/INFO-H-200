package Model;

import java.util.ArrayList;

public class LongRangeMonster extends Monster{
	private Game game;
	private int intervalRandom = 500;
	private int intervalKill = 200;	
	private Player player;
	
	public LongRangeMonster(int x, int y, int lifes, int attack, Game game) {
		super(x, y, 15, lifes, attack, game);
		new Thread(this).start();
		this.game = game;
	}

	
	public boolean inSight(int deltaX, int deltaY){
		/*
		 * permet de déterminer si le monstre peut voir le joueur (pas d'obstacle entre les deux)
		 */
		boolean inSight = true;
		if (deltaX == 0){
			if (deltaY > 0){	//joueur sous le monstre
				for (int i = 1; i<deltaY; i++){	//regarde sur toutes es cases entre si obstacle
					if (!caseIsKillable(this.posX, this.posY + i)){
						inSight = false;
						break;
					}
				}
			} else{				//joueur au-dessus du monstre
				for (int i = 1; i<Math.abs(deltaY); i++){
					if (!caseIsKillable(this.posX, this.posY - i)){
						inSight = false;
						break;
					}
				}
			}
		} else if (deltaY == 0){
			if (deltaX > 0){	//joueur à droite du monstre
				for (int i = 1; i<deltaX; i++){
					if (!caseIsKillable(this.posX + i, this.posY)){
						inSight = false;
						break;
					}
				}
			} else {			//joueur à gauche du monstre
				for (int i = 1; i<Math.abs(deltaX); i++){
					if (!caseIsKillable(this.posX - i, this.posY)){
						inSight = false;
						break;
					}
				}
			}
		}
		return inSight;	
	}
	
	public void shoot(int deltaX, int deltaY){
		/*
		 * crée les lasers sur les cases où ils peuvent apparaître si le joueur est en vue
		 * et enlève de la vie au joueur dans le cas où il est toujours présent au bout du laser
		 */
		try {
			Thread.sleep(intervalKill);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (deltaX == 0){
			if (deltaY > 0){
				for (int i = 1; i < deltaY; i++){
					new Laser(this.posX, this.posY + i, game, deltaX!=0);
				}
			}else{
				for (int i = 1; i < Math.abs(deltaY); i++){
					new Laser(this.posX, this.posY - i, game, deltaX!=0);
				}
			}
		} else if (deltaX > 0){
			for (int i = 1; i < deltaX; i++){
				new Laser(this.posX + i, this.posY, game, deltaX!=0);
			}
		} else{
			for (int i = 1; i < Math.abs(deltaX); i++){
				new Laser(this.posX - i, this.posY, game, deltaX!=0);
			}
		}
		if (player.isAtPosition(this.posX + deltaX, this.posY + deltaY)){
			player.removeLifes(attackValue);
		}
	}
	
	@Override
	public int moveMonster(int deltaX, int deltaY, Player player) {
		int interval;
		if (deltaX == 0 || deltaY == 0){	//si le joueur est sur la même ligne
			if (inSight(deltaX, deltaY)){	//si il n'y a pas d'obstacle entre les deux
				interval = intervalKill;
				shoot(deltaX, deltaY);		//tire
			}else{							//obstacle entre le joueur et le monstre
				interval = intervalRandom;
				moveRandom();				//mouvement aléatoire
			}
		} else{								//joueur pas sur la même ligne
			interval = intervalRandom;
			moveRandom();					//mouvement aléatoire
		}
		return interval;
	}

	
	@Override
	public void run() {
		try{
			while (true){	//tourne en boucle tout le temps de vie de l'ennemi
				ArrayList<GameObject> objects = game.getGameObjects();
				this.player = ((Player) objects.get(0));
				int xPlayer = player.getPosX();
				int yPlayer = player.getPosY();
				int xMonster = this.getPosX();
				int yMonster = this.getPosY();
				int deltaX = xPlayer - xMonster;
				int deltaY = yPlayer - yMonster;
				int interval = this.moveMonster(deltaX, deltaY, player);
				game.notifyView();
				Thread.sleep(interval);
				if (player.getLifes() < 1 ||this.lifes < 1){
					break;	//si le joueur ou le monstre est mort, le thread s'arrête
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}