package Model;

import java.util.ArrayList;

public interface DemisableObserver {
	void demise(Demisable d, ArrayList<GameObject> loot);
}
