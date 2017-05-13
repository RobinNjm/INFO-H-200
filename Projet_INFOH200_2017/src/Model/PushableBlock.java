package Model;

public class PushableBlock extends UnbreakableBlock{

	public PushableBlock(int x, int y) {
		super(x, y, 10);
	}
	
	public boolean move(int x, int y, boolean caseIsFree){
		/*
		 * fonction appelée par le joueur dans le cas où il entre en contact
		 * avec un de ces blocs poussables et renvoyant false si le bloc peut
		 * se déplacer et false si il est bloqué
		 */
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
