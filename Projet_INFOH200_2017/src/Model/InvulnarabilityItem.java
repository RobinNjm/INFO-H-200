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
		new Thread((Runnable) this).start();	//lancement du thread quand on veut utiliser cet objet
	}

	@Override
	public boolean isInstant() {
		return false;		//objet non instantané
	}

	@Override
	public void run() {
		this.setPosition(game.sizeMap + 7, 1);	//déplacement de l'objet de l'inventaire vers "in use"
		player.invulnerable();					//appel de la fonction invulnérable dans player
		game.startTimer(duration);				//démarrage d'un timer qui s'affiche à l'écran
		player.invulnerable();				//nouvel appel de la fonction invulnérable dans player pour revenir à l'état initial
		inUseObjects.remove(this);			//suppression de l'objet dans la liste en cours d'utilisation
		this.drop();						//suppression de l'objet dans la liste d'objets globale
	}
}
