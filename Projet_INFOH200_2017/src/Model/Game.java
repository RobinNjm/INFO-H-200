package Model;

import View.Map;
import java.util.ArrayList;
import java.util.Random;

import View.Window;

public class Game implements DemisableObserver{
	private ArrayList<GameObject> objects = new ArrayList<GameObject>();
	
	private Window window;
	protected int sizeMap = Map.getSizeMap();
	private int numberOfBreakableBlocks = 50;
	private int initNumberOfMonsters = 0;
	private int numberOfMonsters = initNumberOfMonsters;
	private boolean whichOne = true;
	private int monsterAttack = 0;
	private int monsterLifes = 0;
	private int levelNumber = 0;
	
	
	
	public Game(Window window){
		this.window = window;
		
		// Creating one Player at position (1,1)
		objects.add(new Player(1,1,5,1, this));
		
		mapBuild();
	}
	
	public void mapBuild(){
		
		Player player = (Player) objects.get(0);
		this.objects.clear();
		this.objects.add(player);
		
		player.setPosition(1, 1);
		
		this.levelNumber += 1;
		window.map.updateLevelNumber();
		
		if(this.levelNumber != 1){
			window.nextLevel(this.levelNumber);
		}
		
		this.objects.addAll(player.inventory.getInventoryObjects());
		
		for(int i = 0; i < sizeMap; i++){
			objects.add(new Wall(i,0));
			objects.add(new Wall(0,i));
			objects.add(new Wall(i, sizeMap-1));
			objects.add(new Wall(sizeMap-1, i));
		}
				
		for(int i = 0; i < numberOfBreakableBlocks; i++){
			ArrayList<Integer> list;
			list = generatePosition();
			BreakableBlock block = new BreakableBlock(list.get(0),list.get(1));
			block.demisableAttach(this);
			objects.add(block);
		}
		
		this.numberOfMonsters = this.initNumberOfMonsters + 5;
		this.initNumberOfMonsters += 5;
		this.monsterAttack += 1;
		this.monsterLifes += 1;
		window.setNumberOfMonsters(this.numberOfMonsters);
		
		for (int i = 0; i < numberOfMonsters; i++){
			ArrayList<Integer> list;
			list = restrictedGeneratePosition();
			Monster monster = new Monster(list.get(0),list.get(1), monsterLifes, monsterAttack, this);
			monster.demisableAttach(this);
			objects.add(monster);
		}
				
				
		window.update(this.getGameObjects());
		notifyView();
	}
	
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
	
	private ArrayList<Integer> generatePosition(){
		Random rand = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int x = 0;
		int y = 0;
		do{
			x = rand.nextInt(sizeMap-2) + 1;
			y = rand.nextInt(sizeMap-2) + 1;
		} while (!caseIsFree(x, y));
		list.add(x);
		list.add(y);
		return list;
	}
	
	private ArrayList<Integer> restrictedGeneratePosition(){
		Random rand = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int x = 0;
		int y = 0;
		do{
			x = rand.nextInt(sizeMap-7) + 1;
			y = rand.nextInt(sizeMap-7) + 1;
		} while (!caseIsFree(x, y));
		list.add(x + 5);
		list.add(y + 5);
		return list;
	}
	
	public synchronized boolean caseIsFree(int x, int y){
		boolean obstacle = false;
		for(GameObject object : objects){
			if(object.isAtPosition(x, y)){
				obstacle = object.isObstacle();
			}
			if(obstacle){
				break;
			}
		}
		return !obstacle;
	}
	
	public void teleporterCreation(){
		ArrayList<Integer> list;
		list = generatePosition();
		TeleporterItem chimichanga = new TeleporterItem(list.get(0), list.get(1), this);
		objects.add(chimichanga);
		notifyView();
	}
	
	public void monsterDestroyed(int posX, int posY){
		this.numberOfMonsters = this.numberOfMonsters - 1;
		window.setNumberOfMonsters(this.numberOfMonsters);
		if (this.numberOfMonsters == 0){
			teleporterCreation();
		} else{
			loot(posX, posY);
		}
		
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
		} else if (count == 3){
			Bomb bomb = new Bomb(posX, posY, this);
			objects.add(bomb);
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
	
	public void swapAttack(){
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
	
	public synchronized void pickItem(int playerNumber){
		Player player = ((Player) objects.get(playerNumber));
		player.pick(objects);
	}
	
	public synchronized void dropItem(int playerNumber, int selectedItem){
		Player player = ((Player) objects.get(playerNumber));
		player.dropItem(selectedItem);
	}
	
	public synchronized void useItem(int playerNumber, int selectedItem){
		Player player = ((Player) objects.get(playerNumber));
		player.useItem(selectedItem);
	}
}
