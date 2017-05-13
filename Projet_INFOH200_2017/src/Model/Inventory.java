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
		/*
		 * ajoute un objet � l'inventaire si il y a encore de la place
		 */
		if (inventoryObjects.size() < size){
			inventoryObjects.add(item);
			item.setPosition(game.sizeMap + 2, inventoryObjects.size());	//colonne d�di�e � l'inventaire
			game.notifyView();
		} else {
			System.out.println("Inventaire plein");
		}
	}
	
	public void useItem(int position){
		/*
		 * utilise un objet � la position re�ue en param�tre si il y en a un
		 * sinon affiche "Aucun objet � la position  + position" dans la console
		 */
		if (inventoryObjects.size() >= position){
			InventoryItem item = inventoryObjects.get(position);
			if (item.isInstant()){
				
				item.use(player, inUseObjects);
				
				inventoryObjects.remove(position);	//enl�ve de l'inventaire
				
				int count = 1;
				for (InventoryItem object : inventoryObjects){	//repositionnement des objets dans l'inventaire
					object.setPosition(game.sizeMap + 2, count);
					count++;
				}
			} else if (inUseObjects.size() == 0){	//objet pas instantan� et aucun objet en cours d'utilisation
				inUseObjects.add(item);
				
				item.use(player, inUseObjects);
				
				inventoryObjects.remove(position);	//enl�ve de l'inventaire
				
				int count = 1;
				for (InventoryItem object : inventoryObjects){
					object.setPosition((game.sizeMap + 2), count);
					count++;
				}
			}
			
			game.notifyView();
		} else {
			System.out.println("Aucun objet � la position " + position);
		}
	}
	
	public void dropItem(int position){
		/*
		 * abandonne l'objet � la position re�ue en param�tre si il y en a un
		 * sinon affiche "Aucun objet � la position  + position" dans la console
		 */
		if (inventoryObjects.size() >= position){
			InventoryItem item = inventoryObjects.get(position);
			item.drop();
			inventoryObjects.remove(position);
			
			int count = 1;
			for (InventoryItem object : inventoryObjects){
				object.setPosition((game.sizeMap + 2), count);
				count++;
			}
			
			game.notifyView();
		} else {
			System.out.println("Aucun objet � la position " + position);
		}
	}
}
