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
		this.inUseObjects = inUseObjects;
		player.addLifes(1);
		this.player = player;
		new Thread((Runnable) this).start();
	}
	
	public synchronized void run(){
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
