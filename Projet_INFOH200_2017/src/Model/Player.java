package Model;

import java.util.ArrayList;


public class Player extends Character implements DemisableObserver{
	
	protected int countBomb;
	public Inventory inventory;
	private boolean isInvulnerable = false;

	public Player(int x, int y, int lifes, int attackValue, Game game){
		super(x, y, 1, lifes, attackValue, game);
		countBomb = 3;
		this.inventory = new Inventory(this, game);
	}
	
	public synchronized Bomb dropBomb(){
		/*
		 * permet au joueur de lacher une bombe si il en a encore
		 */
		if(this.countBomb > 0){
			this.countBomb = this.countBomb - 1;	//décrémentation du compteur de bombes
			Bomb bomb = new Bomb(posX, posY, game);	//création d'une bombe
			bomb.demisableAttach(this);
			new Thread(bomb).start();				//démarrage de son thread
			return bomb;
		}
		return null;
	}
	
	public void addLifes(int heal){
		/*
		 * ajoute un certain nombre de vie(s) au joueur
		 */
		this.lifes = this.lifes + heal;
	}
	
	public int getCountBomb(){
		return countBomb;
	}
	
	public synchronized void simpleAttack(int x, int y){
		/*
		 * fonction permettant une attaque au corps à corps
		 * prenant en paramètre la direction dans laquelle l'effectuer
		 */
		for(GameObject object : game.getGameObjects()){
			if (object instanceof Monster && object.isAtPosition(this.posX + x, this.posY + y)){	
				//pour tous les monstres de la liste vérifier si ils sont à la position de l'attaque
				((Monster) object).removeLifes(attackValue);	//si oui, lui enlever de la vie
				
			}
		}
	}
	
	public synchronized void distanceAttack(int x, int y){
		/*
		 * fonction permettant l'utilisation du laser
		 * prenant en paramètre la direction dans laquelle le projeter
		 */
		int nextX = this.posX + x;
		int nextY = this.posY + y;
		while (caseIsKillable(nextX, nextY)){	//si un laser peut être affiché sur cette case
			new Laser(nextX, nextY, game, x!=0);	//laser créé
			nextX += x;							//toujours le cas une case plus loin?
			nextY += y;
		}
	}
	
	public void pick(ArrayList<GameObject> objects){
		/*
		 * ramasser des objets sur le sol
		 */
		for (GameObject item : objects){
			if (item instanceof InventoryItem && item.isAtPosition(this.posX, this.posY)){
				inventory.addItem((InventoryItem) item);	//ajouter l'objet à l'inventaire si il peut l'être
			} else if (item instanceof Item && item.isAtPosition(this.posX, this.posY)){
				this.countBomb += 1;		//si l'objet ne peut pas aller dans l'inventaire, c'est une bombe
				((Item) item).drop();
			}
		}
	}
	
	public void dropItem(int selectedItem){
		inventory.dropItem(selectedItem);
	}
	
	public void useItem(int selectedItem){
		inventory.useItem(selectedItem);
	}
	
	public void setPosition(int x, int y){
		this.posX = x;
		this.posY = y;
	}
	
	public void invulnerable() {
		/*
		 * appelée si une potion d'invulnérabilité est en cours d'utilisation
		 */
		this.isInvulnerable  = !this.isInvulnerable;
	}
	
	@Override
	public void removeLifes(int hurt){
		if (!this.isInvulnerable){	//enlève des vies seulement si le joueur n'est pas invulnérable
			this.lifes = this.lifes - hurt;
			if(this.lifes == 0){
				game.gameOver();	//jeu terminé si le joueur n'a plus de vie
			}
		}
	}

	@Override
	public void demise(Demisable d, ArrayList<GameObject> loot) {
		// TODO Auto-generated method stub
		
	}
}
