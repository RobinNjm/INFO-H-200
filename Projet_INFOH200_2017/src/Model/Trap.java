package Model;

public class Trap extends UnbreakableBlock{

	private int hurtValue;
	
	public Trap(int x, int y) {
		super(x, y, 9);
	}
	
	public synchronized void hurt(Player player){
		if (player.isAtPosition(this.posX, this.posY)){
			player.removeLifes(hurtValue);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
