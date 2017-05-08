package Model;

import java.util.ArrayList;

public class HealOverTime extends InventoryItem implements Runnable {
	
	private ArrayList<InventoryItem> inUseObjects;
	private int healValue = 3;
	private Player player;
	
	public HealOverTime(int posX, int posY, Game game){
		super(posX, posY, 4, game);
	}
	
	public void use(Player player, ArrayList<InventoryItem> inUseObjects){
		this.inUseObjects = inUseObjects;
		player.addLifes(1);
		this.player = player;
		new Thread((Runnable) this).start();
	}
	
	public void run(){
		this.setPosition(37, 1);
		for (int i = 1 ; i<healValue ; i++){
			try {
				Thread.sleep(5000);
				player.addLifes(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		inUseObjects.remove(this);
		this.drop();
	}

	@Override
	public boolean isInstant() {
		return false;
	}
}
