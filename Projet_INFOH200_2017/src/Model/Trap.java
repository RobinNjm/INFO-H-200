package Model;

public class Trap extends UnbreakableBlock implements Runnable{
	
	/*Case qui fait perdre une vie au joueur � intervalle r�gulier
	 * (monstres ne peuvent pas aller sur cette case)
	 */
	
	private int hurtValue = 1;
	private int interval = 1000;
	private Player player;
	
	public Trap(int x, int y, Player player) {
		super(x, y, 14);
		this.player = player;
	}

	@Override
	public void run() {
		while(player.isAtPosition(this.posX, this.posY)){	//fonction en cours tant que le joueur est r�sent sur la case
			player.removeLifes(hurtValue);				//une fois le run enclench�, enl�ve une vie au joueur
			try {
				Thread.sleep(interval);					//d�lai avant de v�rifier si le joueur est toujours l�
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
