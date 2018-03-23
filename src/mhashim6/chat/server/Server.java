package mhashim6.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import mhashim6.Toolkit.Appender;
import mhashim6.chat.toolkit.ConnectionStateEvent;
import mhashim6.chat.toolkit.ConnectionStateSupport;
import mhashim6.chat.wrappers.factories.Account;
import mhashim6.chat.wrappers.factories.AccountFactory;
import mhashim6.chat.wrappers.factories.messages.ChatMessage;
import mhashim6.chat.wrappers.factories.messages.Message;
import mhashim6.chat.wrappers.factories.messages.MessageType;
import mhashim6.chat.wrappers.factories.messages.UtilityMessage;

public class Server implements ConnectionStateSupport {
	private List<Account> accounts;

	private ServerSocket	serverSocket;
	private ExecutorService	executor;

	private ConnectionStateEvent disconHandler;
	private Appender	     appenderObject;

	private AtomicBoolean isConnected;

	public Server(Appender outputObject) {
		executor = Executors.newCachedThreadPool();
		isConnected = new AtomicBoolean(false);
		this.appenderObject = outputObject;
		accounts = AccountFactory.instance().getInstances();

	}

	@Override
	public void setOnDisconnect(ConnectionStateEvent handler) {
		disconHandler = handler;
	}

	public boolean startServer() {
		if (!isConnected.get()) {
			try {
				serverSocket = new ServerSocket(50000);
				isConnected.set(true);
				new Thread(new NewAccountsReceptionest(), "New Accounts Receptionest").start();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return isConnected.get();

	}

	private class NewAccountsReceptionest implements Runnable {

		@Override
		public void run() {
			while (isConnected.get()) {
				try {
					Socket socket = serverSocket.accept();
					executor.execute(new AccountAuthentication(
							AccountFactory.instance().createFrom(socket)));
				}
				catch (IOException e) {//TODO
					e.printStackTrace();
				}
			}

		}

	}

	private class AccountAuthentication implements Runnable {

		private Account		  unauthorizedAccount;
		private ObjectInputStream currentInputStream;

		protected AccountAuthentication(Account unauthorizedAccount) {
			this.unauthorizedAccount = unauthorizedAccount;
			currentInputStream = unauthorizedAccount.getSocketObjectInputStream();
		}

		@Override
		public void run() {
			Message msg = null;
			try {
				if ((msg = ((Message) currentInputStream.readObject())) != null) {
					if (msg.getType() == MessageType.REQ_CONNECT) { //TODO
						String tempName = msg.getSenderName();
						//TODO password.

						if (getUsers().contains(tempName)) { //check if not valid
							tellThisUser(unauthorizedAccount,
									new UtilityMessage(MessageType.ERR_INVALID_NAME,
											null, "name already exists")); //tell him why
							AccountFactory.instance().remove(unauthorizedAccount); //reject this account
						}

						else { //the account is welcome.
							unauthorizedAccount.setUsername(tempName);
							greetUser(unauthorizedAccount);
							executor.execute(new AccountHandler(unauthorizedAccount));
							return; //stop.

						}
					}

					else { //TODO: unexpected message.
						AccountFactory.instance().remove(unauthorizedAccount); //reject this account

					}
				}
			}
			catch (Exception e) {//stop
				e.printStackTrace();
			}

		}

	}

	private class AccountHandler implements Runnable {

		private ObjectInputStream currentInputStream;
		private Account		  clientAccount;

		protected AccountHandler(Account account) {
			clientAccount = account;
			currentInputStream = clientAccount.getSocketObjectInputStream();

		}

		@Override
		public void run() {
			Message msg = null;

			try {
				while (isConnected.get()
						&& (msg = ((Message) currentInputStream.readObject())) != null) {

					appenderObject.appendText("--Received:\n" + msg + "\n"); //debug
					handleMessages(msg);
				}

			}
			catch (Exception ex) {
				farewell(clientAccount);
			}

		}

		private final void handleMessages(Message msg) {

			if (msg instanceof ChatMessage)
				handleChat((ChatMessage) msg);
			else
				handleRequests(msg);
		}

		private final void handleChat(ChatMessage msg) {

			if (msg.getAdditionalText().equals("ousers")) {
				tellThisUser(clientAccount,
						new UtilityMessage(MessageType.INFO_USERS_DATA, null, getUsers()));//TODO
			}
			else
				tellEveryone(((ChatMessage) msg));

		}

		private void handleRequests(Message msg) {
			MessageType type = msg.getType();
			String tempName = msg.getSenderName();

			switch (type) {

			case REQ_DISCONNECT:
				//TODO
				break;

			case REQ_GET_USERS_DATA:
				//TODO
				break;

			default:
				break;
			}
		}

	}

	private void greetUser(Account account) {
		tellEveryone(new UtilityMessage(MessageType.INFO_ADD, null, account.getUsername()));
	}

	private void tellEveryone(Message msg) {
		Iterator<Account> it = accounts.iterator();
		for (; it.hasNext();)
			tellThisUser(it.next(), msg);

	}

	private void tellThisUser(Account account, Message msg) {

		if (AccountFactory.instance().has(account))
			try {
				ObjectOutputStream temp = account.getSocketObjectOutputStream();
				temp.writeObject(msg);
				temp.flush();
				temp = null;
			}
			catch (IOException e) {
				appenderObject.appendText("--Error\n	telling " + account.getUsername() + "\n");
				//				e.printStackTrace();
			}
	}

	public String getUsers() {
		
		StringBuilder users = new StringBuilder();
		users.append("Online now :\n");
		accounts.forEach(account -> users.append(account.getUsername()).append("\n"));
		return users.toString();

	}

	private void farewell(Account account) {
		
		String tempName = account.getUsername();
		AccountFactory.instance().remove(account);
		AccountFactory.instance().kill(account);

		tellEveryone(new UtilityMessage(MessageType.INFO_REMOVE, null, tempName));
		appenderObject.appendText(tempName + " failed.\n");
	}

	public void stopServer() {

		if (isConnected.get()) {
			try {
				tellEveryone(new UtilityMessage(MessageType.INFO_SERVER_SHUTDOWN, null, null));
				isConnected.set(false);
				AccountFactory.instance().clear();

				if (serverSocket != null)
					if (!serverSocket.isClosed())
						serverSocket.close();

				if (disconHandler != null)
					disconHandler.act();

			}
			catch (IOException e) {
				appenderObject.appendText("--Error\n	in shuting down server\n");

			}

		}
	}

}
