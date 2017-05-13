package Model;

public class PushableBlock extends UnbreakableBlock{

	public PushableBlock(int x, int y) {
		super(x, y, 10);
	}
	
	public boolean move(int x, int y, boolean caseIsFree){
		/*
		 * fonction appel�e par le joueur dans le cas o� il entre en contact
		 * avec un de ces blocs poussables et renvoyant false si le bloc peut
		 * se d�placer et false si il est bloqu�
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
