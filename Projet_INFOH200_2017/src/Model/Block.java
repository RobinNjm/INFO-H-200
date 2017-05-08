package Model;

public abstract class Block extends GameObject{

	public Block(int x, int y, int color) {
		super(x, y, color);
	}

	@Override
	public boolean isObstacle() {
		return true;
	}
	

}
