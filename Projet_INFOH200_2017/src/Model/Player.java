package Model;

import java.util.ArrayList;


public class Player extends Character implements DemisableObserver{
	
	protected int countBomb;
	public Inventory inventory;

	public Player(int x, int y, int lifes, int attackValue, Game game){
		super(x, y, 1, lifes, attackValue, game);
		countBomb = 3;
		this.inventory = new Inventory(this, game);
	}
	
	public synchronized Bomb dropBomb(){
		if(this.countBomb > 0){
			this.countBomb = this.countBomb - 1;
			Bomb bomb = new Bomb(posX, posY, game);
			bomb.demisableAttach(this);
			Thread thread = new Thread(bomb);
			thread.start();
			return bomb;
		}
		return null;
	}
	
	public void addLifes(int heal){
		this.lifes = this.lifes + heal;
	}
	
	public int getCountBomb(){
		return countBomb;
	}
	
	public void setCountBomb(int number){
		this.countBomb = number;
	}
	
	public synchronized void simpleAttack(int x, int y){
		for(GameObject object : game.getGameObjects()){
			if (object instanceof Monster){
				if (object.isAtPosition(this.posX + x, this.posY + y)){
					((Monster) object).removeLifes(attackValue);
				}
			}
		}
	}
	
	public void pick(ArrayList<GameObject> objects){
		for (GameObject item : objects){
			if (item instanceof InventoryItem && item.isAtPosition(this.posX, this.posY)){
				inventory.addItem((InventoryItem) item);
			} else if (item instanceof Item && item.isAtPosition(this.posX, this.posY)){
				this.countBomb += 1;
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

	@Override
	public void demise(Demisable d, ArrayList<GameObject> loot) {
		// TODO Auto-generated method stub
		
	}

}
