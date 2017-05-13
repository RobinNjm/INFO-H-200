package Model;

import java.util.Random;
import Model.Game;

public abstract class Monster extends Character implements Runnable, DamageableObserver, Demisable{	
	
	public Monster(int x, int y, int color, int lifes, int attack, Game game){
		super(x, y, color, lifes, attack, game);
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
	
	public abstract int moveMonster(int deltaX, int deltaY, Player player);
}
