package combatgame.input;

import java.util.List;
import java.util.ArrayList;

/**
 * **HAPPY**
 */

public class Pool<T> {
	
	public interface PoolObjectFactory<T> {
		public T createObject();
	}
	
	private final List<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize;
	
	public Pool(PoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	
	public T newObject() {
		T object = null;
		if(freeObjects.isEmpty())
			return object = factory.createObject();
		else
			object = freeObjects.remove(0);
		
		return object;
	}
	
	public void free(T object) {
		if(freeObjects.size() < maxSize)
			freeObjects.add(object);
	}

}
