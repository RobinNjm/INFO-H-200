package Model;

import java.util.ArrayList;

public abstract class InventoryItem extends Item{
	
	public InventoryItem(int x, int y, int color, Game game) {
		super(x, y, color, game);
	}
	
	public void setPosition(int x, int y){
		this.posX = x;
		this.posY = y;
	}
	
	public abstract void use(Player player, ArrayList<InventoryItem> inUseObjects);
	
	public abstract boolean isInstant();
}
