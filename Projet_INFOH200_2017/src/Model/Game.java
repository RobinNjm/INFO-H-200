package Model;

import View.Map;
import java.util.ArrayList;
import java.util.Random;

import View.Window;

public class Game implements DemisableObserver{
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	
	private Window window;
	private int sizeMap = Map.getSizeMap();
	private int numberOfBreakableBlocks = 50;
	private int numberOfMonsters = 5;
	private boolean whichOne = true;
	
	
	
	public Game(Window window){
		this.window = window;
		window.setNumberOfMonsters(this.numberOfMonsters);

		// Creating one Player at position (1,1)
		objects.add(new Player(1,1,5,1, this));
		
		// Map building 
		for(int i = 0; i < sizeMap; i++){
			objects.add(new Wall(i,0));
			objects.add(new Wall(0,i));
			objects.add(new Wall(i, sizeMap-1));
			objects.add(new Wall(sizeMap-1, i));
		}
		Random rand = new Random();
		for(int i = 0; i < numberOfBreakableBlocks; i++){
			int x = rand.nextInt(sizeMap-2) + 1;
			int y = rand.nextInt(sizeMap-2) + 1;
			BreakableBlock block = new BreakableBlock(x,y);
			block.demisableAttach(this);
			objects.add(block);
		}
		
		for (int i = 0; i < numberOfMonsters; i++){
			int x = rand.nextInt(sizeMap-2) + 1;
			int y = rand.nextInt(sizeMap-2) + 1;
			Monster monster = new Monster(x, y, 1, 1, this);
			monster.demisableAttach(this);
			objects.add(monster);
		}
		
		
		window.update(this.getGameObjects());
		notifyView();
	}
	
	public
	
	public synchronized void dropBomb(int playerNumber){
		Player player = ((Player) objects.get(playerNumber));
		
		Bomb bombDropped = player.dropBomb();
		if(bombDropped != null){
			bombDropped.demisableAttach(this);
			for(GameObject object : objects){
				if(object instanceof Damageable){
					((Bomb) object).damageableAttach(((DamageableObserver) bombDropped));
				}
				if(object instanceof DamageableObserver){
					bombDropped.damageableAttach(((DamageableObserver) object));
				}
			}
			objects.add(bombDropped);
			notifyView();
		}
	}
	
	public synchronized void movePlayer(int x, int y, int playerNumber){
		Player player = ((Player) objects.get(playerNumber));
		player.move(x,y);
		notifyView();
	}
	
	
	protected void notifyView(){
		window.update(this.objects);
	}

	public ArrayList<GameObject> getGameObjects(){
		return this.objects;
	}
	
	public void monsterDestroyed(int posX, int posY){
		this.numberOfMonsters = this.numberOfMonsters - 1;
		window.setNumberOfMonsters(this.numberOfMonsters);
		loot(posX, posY);
		
	}
	
	private synchronized void loot(int posX, int posY){
		Random rand = new Random();
		int count = rand.nextInt(5);
		if (count == 0 || count == 1){
			InstantHeal heal = new InstantHeal(posX, posY, this);
			objects.add(heal);
		} else if (count == 2){
			HealOverTime heal = new HealOverTime(posX, posY, this);
			objects.add(heal);
		}
	}
	
	public int getNumberOfMonsters(){
		return numberOfMonsters;
	}
	
	public synchronized void attack(int x, int y, int playerNumber){
		Player player = ((Player) objects.get(playerNumber));
		if (whichOne){
			player.simpleAttack(x, y);
			notifyView();
		} else {
			/*player.distanceAttack(x, y);
			notifyView();*/
		}
		notifyView();
		
	}
	
	public void swapAttack(int playerNumber){
		this.whichOne = !this.whichOne;
	}
	
	@Override
	public synchronized void demise(Demisable ps, ArrayList<GameObject> loot) {
		objects.remove(ps);
		if(loot != null){
			objects.addAll(loot);
		}
		notifyView();
	}

	public synchronized void unlimitedBombs(int playerNumber) {
		Player player = ((Player) objects.get(playerNumber));
		player.setCountBomb(10000);
	}

	public void gameOver() {
		window.gameOver();
	}
	
	public void pickItem(int playerNumber){
		Player player = ((Player) objects.get(playerNumber));
		player.pick(objects);
	}
	
	public synchronized void dropItem(int playerNumber, int selectedItem){
		Player player = ((Player) objects.get(playerNumber));
		player.dropItem(selectedItem);
	}
	
	public void useItem(int playerNumber, int selectedItem){
		Player player = ((Player) objects.get(playerNumber));
		player.useItem(selectedItem);
	}
}
