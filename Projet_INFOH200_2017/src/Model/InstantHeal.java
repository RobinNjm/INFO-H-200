package Model;

import java.util.ArrayList;

public class InstantHeal extends InventoryItem{
	
	private int healValue = 2;
	
	public InstantHeal(int posX, int posY, Game game){
		super(posX, posY, 3, game);
	}
	
	public void use(Player player, ArrayList<InventoryItem> inUseObjects){
		player.addLifes(healValue);
		drop();
	}

	@Override
	public boolean isInstant() {
		return true;
	}
}
