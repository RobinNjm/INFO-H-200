package Model;

import java.util.ArrayList;

public class TeleporterItem extends InventoryItem{
	
	public TeleporterItem(int x, int y, Game game){
		super(x, y, 5, game);
	}

	@Override
	public boolean isInstant() {
		return true;
	}

	@Override
	public void use(Player player, ArrayList<InventoryItem> inUseObjects) {
		/*fonction permettant d'utiliser le téléporteur, qui ramène le joueur à la 
		 * position initiale et recharge une nouvelle map aléatoire
		 */
		game.mapBuild(player);
		this.drop();
	}
}
