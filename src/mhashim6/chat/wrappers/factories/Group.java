package mhashim6.chat.wrappers.factories;

import java.util.ArrayList;
import java.util.List;

import mhashim6.chat.wrappers.ChatElementWrapper;

public class Group implements ChatElementWrapper {

	public final static long PLAYGROUND_ID = -777;
	private final long	 ID	       = System.currentTimeMillis();
	private String		 name;

	private List<Account> accounts;

	private boolean		 isPrivate;
	private int		 MaxNumOfUsers;
	private static final int UNLIMITED_USERS = 70; // it's, for an 8 years old.

	protected Group(Account admin, String name) {
		this(admin, name, UNLIMITED_USERS, false);

	}

	protected Group(Account admin, String name, boolean isPrivate) {
		this(admin, name, UNLIMITED_USERS, isPrivate);
	}

	protected Group(Account admin, String name, int MaxNumOfUsers, boolean isPrivate) {
		this.name = name;
		this.isPrivate = isPrivate;
		this.MaxNumOfUsers = MaxNumOfUsers;

		accounts = new ArrayList<>(MaxNumOfUsers);
		accounts.add(admin);

	}

	public synchronized Account getAdmin() {
		return accounts.get(0);
	}

	public synchronized boolean has(Account account) {
		return accounts.contains(account);

	}

	public synchronized boolean addAccount(Account account) {
		if (!(MaxNumOfUsers >= UNLIMITED_USERS))
			if (!hasCapacity())
				return false;
		return accounts.add(account);

	}

	public synchronized void setName(String name) {
		this.name = name;
	}

	public synchronized String getName() {
		return name;
	}

	@Override
	public long getID() {
		return ID;
	}

	public synchronized boolean remove(Account account) {
		return accounts.remove(account);
	}

	public synchronized ArrayList<Account> getAccounts() {
		return (ArrayList<Account>) accounts;
	}

	public int getMaxNumOfUsers() {
		return MaxNumOfUsers;
	}

	public synchronized boolean hasCapacity() {
		return (accounts.size() < MaxNumOfUsers);

	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public synchronized void clear() {
		accounts.clear();
		accounts = null;
	}

}
