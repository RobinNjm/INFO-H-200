package Model;

import java.util.ArrayList;

public class BreakableBlock extends Block implements Demisable, DamageableObserver{

	private ArrayList<DemisableObserver> observers = new ArrayList<DemisableObserver>();
	
	public BreakableBlock(int x, int y) {
		super(x, y, 7);
	}

	@Override
	public void damaged(Damageable e) {
		Bomb bomb = (Bomb) e;
		boolean distanceX = Math.abs(this.getPosX() - bomb.getPosX()) <= bomb.getRange();
		boolean distanceY = Math.abs(this.getPosY() - bomb.getPosY()) <= bomb.getRange();

		if(distanceX && distanceY){
			demisableNotifyObserver();	
		}
	}
	@Override
	public void demisableAttach(DemisableObserver po) {
		observers.add(po);		
	}


	@Override
	public void demisableNotifyObserver() {
		for (DemisableObserver o : observers) {
			o.demise(this, null);
		}	
	}

	
	
	

}
