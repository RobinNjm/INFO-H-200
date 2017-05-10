package Model;

import java.util.ArrayList;

public class TeleporterItem extends InventoryItem{
	
	public TeleporterItem(int x, int y, Game game){
		super(x, y, 5, game);
	}
	
	public void use(){
		game.mapBuild();
		this.drop();
	}

	@Override
	public void use(Player player, ArrayList<InventoryItem> inUseObjects) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInstant() {
		return true;
	}
}
