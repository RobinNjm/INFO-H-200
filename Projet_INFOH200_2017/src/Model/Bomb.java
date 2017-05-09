package Model;

import java.util.ArrayList;

public class Bomb extends Item implements Runnable, Damageable, DamageableObserver{
	
	public int duration = 1000;
	public int bombRange = 1;
	boolean detonated = false;
	protected ArrayList<DemisableObserver> demisableObservers = new ArrayList<DemisableObserver>();
	private ArrayList<DamageableObserver> damageableObservers = new ArrayList<DamageableObserver>();
	
	
	public Bomb (int x, int y){
		super(x, y, 6);
	}
	
	public int getRange(){
		return this.bombRange;
	}
	
	@Override
	public synchronized void run() {
		int count = 0;
		while(!this.detonated  && count < this.duration/10.0){
			try {
				Thread.sleep(10);
				count = count + 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.demisableNotifyObserver();
		this.damageableNotifyObserver();
	}


	@Override
	public void demisableAttach(DemisableObserver po) {
		demisableObservers.add(po);				
	}

	@Override
	public synchronized void demisableNotifyObserver() {
		ArrayList<GameObject> loot = new ArrayList<GameObject>();
		int range = this.getRange();
		int x = this.getPosX();
		int y = this.getPosY();
		for(int i = x-range; i <= x+range; i++){
			for(int j = y-range; j <= y+range; j++){
				HurtingCase cas = new HurtingCase(i,j,250);
				Thread thread = new Thread(cas);
				thread.start();
				for(DemisableObserver observer : demisableObservers){
					cas.demisableAttach(observer);
				}
				loot.add(cas);
			}
		}
		
		//System.out.println(loot.size());
		
		
		for (DemisableObserver o : this.demisableObservers) {
			o.demise(this, loot);
		}		
	}

	public void explodableAttach(DamageableObserver eo) {
		damageableObservers.add(eo);
		
	}

	@Override
	public void damaged(Damageable e) {
		Bomb bomb = (Bomb) e;
		boolean distanceX = Math.abs(this.getPosX() - bomb.getPosX()) <= bomb.getRange() && this.getPosY() == bomb.getPosY();
		boolean distanceY = Math.abs(this.getPosY() - bomb.getPosY()) <= bomb.getRange() && this.getPosX() == bomb.getPosX();
		if(distanceX || distanceY){
			this.detonated = true;
			this.demisableNotifyObserver();		
		}		
	}

	@Override
	public void damageableNotifyObserver() {
		for (DamageableObserver o : damageableObservers) {
			o.damaged(this);
		}		
	}

	@Override
	public void damageableAttach(DamageableObserver po) {
		damageableObservers.add(po);
	}


}
