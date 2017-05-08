package Model;

public class PushableBlock extends UnbreakableBlock{

	public PushableBlock(int x, int y) {
		super(x, y, 10);
	}
	
	public void move(int x, int y){
		posX = posX + x;
		posY = posY + y;
	}

}
