package Model;

public interface Damageable {
	void damageableAttach(DamageableObserver po);
	void damageableNotifyObserver();
}
