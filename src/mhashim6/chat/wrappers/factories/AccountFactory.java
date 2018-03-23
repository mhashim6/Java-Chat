package mhashim6.chat.wrappers.factories;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class AccountFactory extends AbstractFactory<Account> {

	private static AccountFactory instance;

	protected AccountFactory() {
		super();
	}

	public static synchronized AccountFactory instance() {
		if (instance == null)
			instance = new AccountFactory();
		return instance;
	}

	public Account createFrom(Socket socket) throws IOException {
		Account a = new Account(socket);
		add(a);
		return a;
	}

	@Override
	public Account getBy(Object o) {
		synchronized (instances) {
			if (o instanceof String)
				return getBy((String) o);
			else if (o instanceof Socket)
				return getBy((Socket) o);

			else if (o instanceof ObjectOutputStream)
				return getBy((ObjectOutputStream) o);

		}
		return null;
	}

	private Account getBy(String usernameKey) {
		Account temp = null;

		for (Account element : instances)
			if (element.getUsername().equals(usernameKey))
				temp = element;

		return temp;
	}

	private Account getBy(Socket socketKey) {
		Account temp = null;

		for (Account element : instances)
			if (element.getSocket().equals(socketKey))
				temp = element;

		return temp;

	}

	private Account getBy(ObjectOutputStream objectOutputStreamKey) {
		Account temp = null;

		for (Account element : instances)
			if (element.getSocketObjectOutputStream().equals(objectOutputStreamKey))
				temp = element;

		return temp;
	}

	@Override
	public void kill(Account account) {
		account.kill();
	}

}