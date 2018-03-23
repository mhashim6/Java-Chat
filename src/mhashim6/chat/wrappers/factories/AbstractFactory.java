package mhashim6.chat.wrappers.factories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mhashim6.chat.wrappers.ChatElementWrapper;

public abstract class AbstractFactory<T extends ChatElementWrapper> {
	protected List<T> instances;

	protected AbstractFactory() {
		instances = new ArrayList<>(); // TODO
	}

	public List<T> getInstances() {
		synchronized (instances) {
			return instances;
		}
	}

	public abstract T getBy(Object o);

	public T getBy(long ID) {
		T temp = null;
		synchronized (instances) {

			for (T element : instances)
				if (element.getID() == ID) temp = element;

			return temp;
		}
	}

	public boolean add(T instance) {
		synchronized (instances) {
			return instances.add(instance);

		}
	}

	public boolean has(T instance) {
		synchronized (instances) {
			return instances.contains(instance);

		}

	}

	public boolean remove(T instance) {
		synchronized (instances) {
			return instances.remove(instance);
		}
	}

	public void clear() {
		synchronized (instances) {
			if (!isEmpty()) for (Iterator<T> it = instances.iterator(); it.hasNext();) {
				kill(it.next());
			}
			instances.clear();
		}
	}

	public abstract void kill(T instance);

	public boolean isEmpty() {
		synchronized (instances) {
			return instances.isEmpty();
		}
	}
}
