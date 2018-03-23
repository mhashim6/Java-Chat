package mhashim6.chat.wrappers.factories;

import java.util.Set;
import java.util.stream.Collectors;

public class GroupFactory extends AbstractFactory<Group> {

	private static GroupFactory instance;

	protected GroupFactory() {
		super();
	}

	public static synchronized GroupFactory instance() {
		if (instance == null)
			instance = new GroupFactory();
		return instance;
	}

	public Group createFrom(Account admin, String name, boolean isPrivate) {
		Group g = new Group(admin, name, isPrivate);
		instances.add(g);
		return g;
	}

	public Group createFrom(Account admin, String name) {
		Group g = new Group(admin, name);
		instances.add(g);
		return g;
	}

	public Group createGroupOfTwo(Account admin, String name) {
		return createFrom(admin, name, 2, true);
	}

	public Group createFrom(Account admin, String name, int MaxNumOfUsers, boolean isPrivate) {
		Group g = new Group(admin, name, MaxNumOfUsers, isPrivate);
		instances.add(g);
		return g;
	}

	@Override
	public Group getBy(Object o) {
		if (o instanceof String)
			return getBy((String) o);
		return null;
	}

	public Set<Group> getBy(Account account) {
		synchronized (instances) {
			return instances.stream().filter(group -> group.has(account)).collect(Collectors.toSet());
		}
	}

	private Group getBy(String name) {
		Group temp = null;

		for (Group element : instances)
			if (element.getName().equals(name))
				temp = element;

		return temp;
	}

	@Override
	public void kill(Group instance) {
		instance.clear(); // kill

	}

}
