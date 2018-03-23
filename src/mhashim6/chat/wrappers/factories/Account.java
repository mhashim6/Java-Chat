package mhashim6.chat.wrappers.factories;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mhashim6.chat.wrappers.ChatElementWrapper;

public class Account implements ChatElementWrapper {

	private final long ID = System.currentTimeMillis();
	private String	   username;

	private Socket		   socket;
	private ObjectOutputStream outputStream;
	private ObjectInputStream  inputStream;
	private Set<Group>	   groups;
	// ============================================================

	protected Account(Socket socket) throws IOException {
		this.socket = socket;
		outputStream = new ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());
		groups = Collections.synchronizedSet(new HashSet<>());
	}
	// ============================================================

	public synchronized void setUsername(String name) {
		this.username = name;
	}
	// ============================================================

	public synchronized String getUsername() {
		return username;
	}
	// ============================================================

	@Override
	public synchronized long getID() {
		return ID;
	}
	// ============================================================

	public synchronized boolean addGroup(Group group) {
		return groups.add(group);
	}
	// ============================================================

	public synchronized boolean removeGroup(Group group) {
		return groups.remove(group);
	}
	// ============================================================

	public synchronized boolean inGroup(Group group) {
		return groups.contains(group);
	}
	// ============================================================

	public synchronized Set<Group> getGroups() {
		return groups;
	}
	// ============================================================

	public synchronized Socket getSocket() {
		return socket;
	}
	// ============================================================

	public synchronized ObjectOutputStream getSocketObjectOutputStream() {
		return outputStream;
	}
	// ============================================================

	public synchronized ObjectInputStream getSocketObjectInputStream() {
		return inputStream;
	}
	// ============================================================

	public synchronized void kill() {
		username = null;
		if (!socket.isClosed())
			try {
				socket.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
	}
	// ============================================================

}
