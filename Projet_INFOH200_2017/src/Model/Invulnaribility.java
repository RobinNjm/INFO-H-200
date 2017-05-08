package Model;

import java.util.ArrayList;

public class Invulnaribility extends InventoryItem{
	
	private int duration = 10000;

	
	public Invulnaribility(int posX, int posY, Game game){
		super(posX, posY, 5, game);
	}
	
	public int getDuration(){
		return this.duration;
	}
	
	@Override
	public void use(Player player, ArrayList<InventoryItem> inUseObjects){
		//TODO je sais pas comment faire ça
	}

	@Override
	public boolean isInstant() {
		return false;
	}
}
