package Model;

import java.util.ArrayList;

public abstract class Item extends GameObject implements Demisable{
	
	private ArrayList<DemisableObserver> observers = new ArrayList<DemisableObserver>();

	public Item(int x, int y, int color){
		super(x, y, color);
	}
	
	public boolean isObstacle(){
		return false;
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
