package Model;

import java.util.ArrayList;

public abstract class Item extends GameObject implements Demisable{
	
	private ArrayList<DemisableObserver> observers = new ArrayList<DemisableObserver>();
	protected Game game;
	
	public Item(int x, int y, int color, Game game){
		super(x, y, color);
		this.game = game;
	}
	
	public boolean isObstacle(){
		return false;
	}
	
	public synchronized void drop(){
		game.getGameObjects().remove(this);
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
