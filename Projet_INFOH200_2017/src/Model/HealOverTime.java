package Model;

import java.util.ArrayList;

public class HealOverTime extends InventoryItem implements Runnable {
	
	private ArrayList<InventoryItem> inUseObjects;
	private int healValue = 3;
	private Player player;
	private int interval = 5000;
	
	public HealOverTime(int posX, int posY, Game game){
		super(posX, posY, 4, game);
	}
	
	public synchronized void use(Player player, ArrayList<InventoryItem> inUseObjects){
		/*
		 * l'utilisation de cet item entraîne le gain d'une vie par le joueur
		 * suivi du commencement d'un thread
		 */
		this.inUseObjects = inUseObjects;
		this.player = player;
		player.addLifes(1);
		new Thread((Runnable) this).start();
	}
	
	public synchronized void run(){
		/*
		 * le thread lancé enclenche un timer après lequel le joueur gagne une vie
		 * cette boucle est effectuée jusqu'à ce que la "healValue" soit atteinte
		 */
		this.setPosition(game.sizeMap + 7, 1);
		for (int i = 1 ; i<healValue ; i++){
			game.startTimer(interval);
			player.addLifes(1);
		}
		inUseObjects.remove(this);
		this.drop();
	}

	@Override
	public boolean isInstant() {
		return false;
	}
}
