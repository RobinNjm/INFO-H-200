package Model;

import java.util.ArrayList;

public class Bomb extends Item implements Runnable, Damageable, DamageableObserver{
	
	public int duration = 1000;
	public int bombRange = 1;
	private boolean detonated = false;
	protected ArrayList<DemisableObserver> demisableObservers = new ArrayList<DemisableObserver>();
	private ArrayList<DamageableObserver> damageableObservers = new ArrayList<DamageableObserver>();
	
	
	public Bomb (int x, int y, Game game){
		super(x, y, 6, game);
	}
	
	public int getRange(){
		return this.bombRange;
	}
	
	@Override
	public void run() {
		int count = 0;
		while(!this.detonated  && count < this.duration/10.0){
			//continue tant que le temps de détonation n'est pas atteint et tant que la détonation a eu lieu
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
		demisableObservers.add(po);		//ajoute un observateur		
	}

	@Override
	public synchronized void demisableNotifyObserver() {
		ArrayList<GameObject> loot = new ArrayList<GameObject>();
		int range = this.getRange();
		int x = this.getPosX();
		int y = this.getPosY();
		for(int i = x-range; i <= x+range; i++){
			for(int j = y-range; j <= y+range; j++){	//créé des explosions dans une zone carré autour de la position de la bombe
				Explosion explosion = new Explosion(i,j,250);
				new Thread(explosion).start();		//démarre le thread des explosions
				for(DemisableObserver observer : demisableObservers){
					explosion.demisableAttach(observer);
				}
				loot.add(explosion);
			}
		}		
		
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
