package Model;

public class Laser extends GameObject implements Runnable{
	
	private Game game;
	
	public Laser(int x, int y, Game game) {
		super(x, y, 11);
		this.game = game;
	}
	
	public void setPosition(int x, int y, boolean horizontal){
		if (!horizontal){
			color = 13;
		}
		this.posX = x;
		this.posY = y;
		game.addObject(this);
		game.notifyView();
		new Thread(this).start();
	}
	
	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(150);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		game.removeObject(this);
		game.notifyView();
	}

}
