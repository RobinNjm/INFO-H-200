package Model;

public class Trap extends UnbreakableBlock implements Runnable{

	private int hurtValue = 1;
	private int interval = 1000;
	private Player player;
	
	public Trap(int x, int y, Player player) {
		super(x, y, 14);
		this.player = player;
	}

	@Override
	public void run() {
		do{
			player.removeLifes(hurtValue);
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}while(player.isAtPosition(this.posX, this.posY));
	}
}
