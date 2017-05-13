package Model;

public class Laser extends GameObject implements Runnable{
	
	private Game game;
	
	public Laser(int x, int y, Game game, boolean horizontal) {
		super(x, y, 11);
		this.game = game;
		
		if (!horizontal){	//change d'image si le laser est lanc� verticalement
			color = 13;
		}
		
		game.addObject(this);	//objet ajout� � la liste
		game.notifyView();		//demande d'actualisation de la map par l'interm�diaire de game
		new Thread(this).start();	//d�marrage du thread
	}
	
	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(100);	//temps d'affichage du laser
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		game.removeObject(this);	//objet supprim� de la liste
		game.notifyView();			//demande d'actualisation de la map par l'interm�diaire de game
	}

}
