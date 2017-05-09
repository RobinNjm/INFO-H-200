package Model;

import java.util.ArrayList;

public class Inventory {
	public ArrayList<InventoryItem> inventoryObjects = new ArrayList<InventoryItem>();
	public ArrayList<InventoryItem> inUseObjects = new ArrayList<InventoryItem>();
	private int size = 5;
	private Player player;
	private Game game;
	
	public Inventory(Player player, Game game){
		this.player = player;
		this.game = game;
	}
	
	public ArrayList<InventoryItem> getInventoryObjects(){
		return this.inventoryObjects;
	}
	
	public void addItem(InventoryItem item){
		if (inventoryObjects.size() < size){
			inventoryObjects.add(item);
			item.setPosition(32, inventoryObjects.size());	//ligne 32 dédiée à l'inventaire
			
			game.notifyView();
		} else {
			System.out.println("Inventaire plein");
		}
	}
	
	public void useItem(int position){
		if (inventoryObjects.size() >= position){
			InventoryItem item = inventoryObjects.get(position);
			if (item.isInstant()){
				
				item.use(player, inUseObjects);
				
				inventoryObjects.remove(position);
				
				int count = 1;
				for (InventoryItem object : inventoryObjects){
					object.setPosition(32, count);
					count++;
				}
			} else if (inUseObjects.size() == 0){
				inUseObjects.add(item);
				
				item.use(player, inUseObjects);
				
				inventoryObjects.remove(position);
				
				int count = 1;
				for (InventoryItem object : inventoryObjects){
					object.setPosition(32, count);
					count++;
				}
			}
			
			game.notifyView();
		} else {
			System.out.println("Aucun objet à la position " + position);
		}
	}
	
	public void dropItem(int position){
		if (inventoryObjects.size() >= position){
			InventoryItem item = inventoryObjects.get(position);
			item.drop();
			inventoryObjects.remove(position);
			
			int count = 1;
			for (InventoryItem object : inventoryObjects){
				object.setPosition(32, count);
				count++;
			}
			
			game.notifyView();
		} else {
			System.out.println("Aucun objet à la position " + position);
		}
	}
}
