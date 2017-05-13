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
	private int numberOfPushableBlocks = 10;
	private int numberOfShortRangeMonsters = 0;
	private int numberOfLongRangeMonsters = 0;
	private int numberOfMonsters = numberOfLongRangeMonsters + numberOfShortRangeMonsters;
	private boolean whichAttack = true;
	private int monsterAttack = 0;
	private int monsterLifes = 0;
	private int numberOfTraps = 5;
	
	
	
	public Game(Window window){
		this.window = window;
		
		objects.add(new Player(1,1,5,1, this));	//joueur créé en (1, 1)
		window.map.attack = getAttack();
		
		mapBuild();
	}
	
	public void mapBuild(){
		/*
		 * instancie un certain nombre de GameObject qui seront affichés à l'écran
		 * appelée en chaque début de niveau
		 */
		
		Player player = (Player) objects.get(0);	//joueur sauvé de la liste
		this.objects.clear();						//liste d'objets remise à 0
		this.objects.add(player);					//joueur ajouté à la liste
		
		player.setPosition(1, 1);					//joueur déplacé en (1, 1) pour le début du niveau
		
		window.map.levelNumber += 1;				//niveau affiché à l'écran incrémenté
		
		this.objects.addAll(player.inventory.getInventoryObjects());//ajout des objets présents dans l'inventaire à la liste d'objets
		
		for(int i = 0; i < sizeMap; i++){	//placement des murs autour de la map
			objects.add(new Wall(i,0));
			objects.add(new Wall(0,i));
			objects.add(new Wall(i, sizeMap-1));
			objects.add(new Wall(sizeMap-1, i));
		}
				
		for(int i = 0; i < numberOfBreakableBlocks; i++){	//placement des blocs cassables aléatoirement
			ArrayList<Integer> list;
			list = generatePosition();
			BreakableBlock block = new BreakableBlock(list.get(0),list.get(1));
			block.demisableAttach(this);
			objects.add(block);
		}
		
		for(int i = 0; i < numberOfPushableBlocks; i++){	//placement des blocs poussables aléatoirement
			ArrayList<Integer> list;
			list = generatePosition();
			objects.add(new PushableBlock(list.get(0),list.get(1)));
		}
		
		for(int i = 0; i < numberOfTraps; i++){				//placement des pièges aléatoirement
			ArrayList<Integer> list;
			list = generatePosition();
			objects.add(new Trap(list.get(0),list.get(1), player));
		}
		
		this.numberOfShortRangeMonsters += 5;	//incrémentation de la vie et de l'attaque 
		this.monsterAttack += 1;				//des monstres de 1 ainsi que du nombre de monstres courte portée de 5
		this.monsterLifes += 1;
		
		for (int i = 0; i < numberOfShortRangeMonsters; i++){			//placement de ces ennemis aléatoirement
			ArrayList<Integer> list;
			list = restrictedGeneratePosition();
			ShortRangeMonster monster = new ShortRangeMonster(list.get(0),list.get(1), monsterLifes, monsterAttack, this);
			monster.demisableAttach(this);
			objects.add(monster);
		}
		
		this.numberOfLongRangeMonsters += 2;	//incrémentation du nombre de monstres longue portée de 2
		
		for (int i = 0; i < numberOfLongRangeMonsters; i++){			//placement de ces ennemis aléatoirement
			ArrayList<Integer> list;
			list = restrictedGeneratePosition();
			LongRangeMonster monster = new LongRangeMonster(list.get(0),list.get(1), monsterLifes, monsterAttack, this);
			monster.demisableAttach(this);
			objects.add(monster);
		}
		
		this.numberOfMonsters = this.numberOfLongRangeMonsters + this.numberOfShortRangeMonsters;
		window.setNumberOfMonsters(this.numberOfMonsters);
		
		window.update(this.getGameObjects());
		notifyView();
	}
	
	public synchronized void dropBomb(int playerNumber){
		/*
		 * appelée par le keyboard pour lacher une bombe
		 */
		Player player = ((Player) objects.get(playerNumber));
		
		Bomb bombDropped = player.dropBomb();	//demande au joueur de lacher une bombe
		if(bombDropped != null){				//dans le cas où c'est possible
			bombDropped.demisableAttach(this);
			for(GameObject object : objects){	//vérifie qui doit être prévenu de l'explosion
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
		/*
		 * appelée par le keyboard pour tenter de bouger le joueur
		 */
		Player player = ((Player) objects.get(playerNumber));
		player.move(x,y);
		notifyView();
	}
	
	
	protected synchronized void notifyView(){
		window.update(this.objects);	//actualise la vue de la map
	}

	public synchronized ArrayList<GameObject> getGameObjects(){
		return this.objects;
	}
	
	private ArrayList<Integer> generatePosition(){
		/*
		 * génère aléatoirement une position non occupée sur la map
		 */
		Random rand = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int x = 0;
		int y = 0;
		do{
			x = rand.nextInt(sizeMap-2) + 1;	//random x
			y = rand.nextInt(sizeMap-2) + 1;	//random y
		} while (!caseIsFree(x, y));			//case libre?
		list.add(x);
		list.add(y);
		return list;
	}
	
	private ArrayList<Integer> restrictedGeneratePosition(){
		/*
		 * génère aléatoirement une position non occupée à une distance
		 * de plus de 3 carrés verticalement et horizontalement
		 */
		Random rand = new Random();
		ArrayList<Integer> list = new ArrayList<Integer>();
		int x = 0;
		int y = 0;
		do{
			x = rand.nextInt(sizeMap-5) + 4;
			y = rand.nextInt(sizeMap-5) + 4;
		} while (!caseIsFree(x, y));
		list.add(x);
		list.add(y);
		return list;
	}
	
	public synchronized boolean caseIsFree(int x, int y){
		/*
		 * détermine si une case est occupée par un objet
		 */
		boolean obstacle = false;	//par défaut : pas d'obstacle
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
		/*
		 * crée l'objet téléporteur permettant de passer au niveau suivant 
		 * et le place sur une case non occupée aléatoire
		 */
		ArrayList<Integer> list;
		list = generatePosition();
		TeleporterItem chimichanga = new TeleporterItem(list.get(0), list.get(1), this);
		objects.add(chimichanga);
		notifyView();
	}
	
	public void monsterDestroyed(int posX, int posY){
		/*
		 * appelée dans le cas où la vie d'un monstre est réduite à 0,
		 * fait apparaître un loot et éventuellement l'objet téléporteur si il n'y a plus de monstre
		 */
		this.numberOfMonsters = this.numberOfMonsters - 1;
		window.setNumberOfMonsters(this.numberOfMonsters);
		if (this.numberOfMonsters == 0){
			loot(posX, posY);
			teleporterCreation();
		} else{
			loot(posX, posY);
		}
		
	}
	
	private synchronized void loot(int posX, int posY){
		/*
		 * détermine un loot aléatoire à faire apparaître à la mort d'un monstre
		 */
		Random rand = new Random();
		int count = rand.nextInt(10);
		if (count == 0 || count == 1){		//2 chances sur 10 d'avoir un InstantHeal
			InstantHeal heal = new InstantHeal(posX, posY, this);
			objects.add(heal);
		} else if (count == 2){				//1 chance sur 10 d'avoir un HealOverTime
			HealOverTime heal = new HealOverTime(posX, posY, this);
			objects.add(heal);
		} else if (count == 3){				//1 chance sur 10 d'avoir une bombe
			Bomb bomb = new Bomb(posX, posY, this);
			objects.add(bomb);
		} else if (count == 4){				//1 chance sur 10 d'avoir un InvulnerabilityItem
			InvulnarabilityItem invulnerable = new InvulnarabilityItem(posX, posY, this);
			objects.add(invulnerable);
		}
	}
	
	public int getNumberOfMonsters(){
		return (numberOfShortRangeMonsters + numberOfLongRangeMonsters);
	}
	
	public synchronized void attack(int x, int y, int playerNumber){
		/*
		 * demande au joueur d'attaquer soit avec le corps à corps soit avec un laser
		 */
		Player player = ((Player) objects.get(playerNumber));
		if (whichAttack){
			player.simpleAttack(x, y);
		} else {
			player.distanceAttack(x, y);
		}
		notifyView();
	}
	
	public synchronized void addObject(GameObject object){
		objects.add(object);
		notifyView();
	}
	
	public synchronized void removeObject(GameObject object){
		objects.remove(object);
		notifyView();
	}
	
	public void swapAttack(){
		/*
		 * change d'attaque passant de celle au corps à corsp à 
		 * celle à distance. (appelée par le keyboard)
		 */
		this.whichAttack = !this.whichAttack;
		window.map.attack = getAttack();
	}
	
	public void startTimer(int duration){
		window.map.startTimer(duration);
	}
	
	@Override
	public synchronized void demise(Demisable ps, ArrayList<GameObject> loot) {
		objects.remove(ps);
		if(loot != null){
			objects.addAll(loot);
		}
		notifyView();
	}

	public void gameOver(){
		window.gameOver();
	}
	
	public synchronized void pickItem(int playerNumber){
		//appelée par keyboard pour ramasser un objet et l'ajouter à l'inventaire
		Player player = ((Player) objects.get(playerNumber));
		player.pick(objects);
	}
	
	public synchronized void dropItem(int playerNumber, int selectedItem){
		//appelée par keyboard pour abandonner un objet de l'inventaire
		Player player = ((Player) objects.get(playerNumber));
		player.dropItem(selectedItem);
	}
	
	public synchronized void useItem(int playerNumber, int selectedItem){
		//appelée par keyboard pour utiliser un objet de l'inventaire
		Player player = ((Player) objects.get(playerNumber));
		player.useItem(selectedItem);
	}
	
	public String getAttack(){
		/*
		 * renvoie l'attaque active à afficher à l'écran
		 */
		String attack = "Fists";
		if (!whichAttack){
			attack = "Laser";
		}
		return attack;
	}
}