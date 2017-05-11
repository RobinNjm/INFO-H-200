package Model;

public class PushableBlock extends UnbreakableBlock{

	public PushableBlock(int x, int y) {
		super(x, y, 10);
	}
	
	public boolean move(int x, int y, boolean caseIsFree){
		boolean obstacle = false;
		if (caseIsFree){
			posX = posX + x;
			posY = posY + y;
		}else{
			obstacle = true;
		}
		
		return obstacle;
	}

}
