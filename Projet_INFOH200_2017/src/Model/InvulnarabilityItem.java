package Model;

import java.util.ArrayList;

public class InvulnarabilityItem extends InventoryItem implements Runnable{
	
	private int duration = 5000;
	private ArrayList<InventoryItem> inUseObjects;
	private Player player;

	
	public InvulnarabilityItem(int posX, int posY, Game game){
		super(posX, posY, 9, game);
	}
	
	public int getDuration(){
		return this.duration;
	}
	
	@Override
	public void use(Player player, ArrayList<InventoryItem> inUseObjects){
		this.inUseObjects = inUseObjects;
		this.player = player;
		new Thread((Runnable) this).start();
	}

	@Override
	public boolean isInstant() {
		return false;
	}

	@Override
	public void run() {
		this.setPosition(game.sizeMap + 7, 1);
		player.invulnerable();
		game.startTimer(duration);
		player.invulnerable();
		inUseObjects.remove(this);
		this.drop();
	}
}
