package combatgame.input;

import java.util.List;
import java.util.ArrayList;

public class LazyPool<T> {
	
	public interface LazyPoolObjectFactory<T> {
		public T createObject();
	}
	
	private final List<T> freeObjects;
	private final LazyPoolObjectFactory<T> factory;
	private final int maxSize;
	
	public LazyPool(LazyPoolObjectFactory<T> factory, int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	
	public T newObject() {
		T object = null;
		if(freeObjects.size() != maxSize) {
			object = factory.createObject();
			freeObjects.add(object);
			return object;
		}
		else
			object = freeObjects.remove(0);
		
		return object;
	}
	
	public void free(T object) {
		if(freeObjects.size() < maxSize)
			freeObjects.add(object);
	}

}