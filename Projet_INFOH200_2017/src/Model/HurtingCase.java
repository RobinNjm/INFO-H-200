package Model;

import java.util.ArrayList;

public class HurtingCase extends GameObject implements Runnable, Demisable, Damageable{
	
	private int duration;
	private ArrayList<DemisableObserver> observers = new ArrayList<DemisableObserver>();


	public HurtingCase(int x, int y, int duration) {
		super(x, y, 6);
		this.duration = duration;
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

	@Override
	public synchronized void run() {
			try {
				Thread.sleep(this.duration);
				this.demisableNotifyObserver();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
	}

	@Override
	public boolean isObstacle() {
		return false;
	}

	@Override
	public void damageableAttach(DamageableObserver po) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void damageableNotifyObserver() {
		// TODO Auto-generated method stub
		
	}


}
