package mhashim6.chat.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import mhashim6.chat.exceptions.AuthenticationFailureException;
import mhashim6.chat.toolkit.StreamOfOutput;
import mhashim6.chat.wrappers.factories.messages.ChatMessage;
import mhashim6.chat.wrappers.factories.messages.Message;
import mhashim6.chat.wrappers.factories.messages.MessageType;
import mhashim6.chat.wrappers.factories.messages.UtilityMessage;

public class Client {

	private String	      username;
	private final String  serverAddress = "localhost";
	private final int     serverPort    = 50000;
	private AtomicBoolean isConnected;
	private boolean	      authenticated;
	private final Object  authLock	    = new Object(); //lock

	private Socket		   socket;
	private ObjectInputStream  inputStream;
	private ObjectOutputStream outputStream;

	private StreamOfOutput<Message>	messagesStream;
	private List<Message>		messagesList = new ArrayList<>();
	private final Object		streamLock   = new Object();	 //lock
	// ============================================================

	public Client() {
		isConnected = new AtomicBoolean(false);
	}

	// ============================================================

	public boolean connect(String name) throws AuthenticationFailureException {
		if (!isConnected.get()) {

			try {
				socket = new Socket(serverAddress, serverPort);
				outputStream = new ObjectOutputStream(socket.getOutputStream());
				inputStream = new ObjectInputStream(socket.getInputStream());

				username = name;
				isConnected.set(true);

				listen();

				if (!authenticate(name)) {
					disconnect();
					throw new AuthenticationFailureException();
				}

				//initialize the message stream if it's not already initialized.
				if (messagesStream == null)
					messagesStream = new StreamOfOutput<Message>() {

						private List<Message> buffer   = messagesList;
						private int	      pointer  = 0;
						private boolean	      isClosed = false;

						@Override
						public Message read() throws IOException {
							if (isClosed)
								throw new IOException("Stream is closed");

							synchronized (streamLock) {
								if (buffer.size() == pointer) //no new messages
									try {
										streamLock.wait(); //wait for new messages.
									}
									catch (InterruptedException e) {
										throw new StreamCorruptedException();
									}
								//when notified:
								if (isClosed) //have we failed ?
									return null;

								return buffer.get(pointer++);
							}

						}
						// ============================================================

						@Override
						public synchronized void close() {
							if (!isClosed) {
								synchronized (streamLock) {
									streamLock.notifyAll();
								}
								isClosed = true;
							}
						}
						// ============================================================

						@Override
						public synchronized boolean isClosed() {
							return isClosed;
						}
						// ============================================================

						@Override
						public synchronized void reset() {
							if (!isClosed)
								close();
							this.pointer = 0;
							this.isClosed = false;
						}

					};

				else //then reset it.
					messagesStream.reset();
			}
			catch (IOException ex) {
				//				ex.printStackTrace();
			}

		}
		//inform the caller if we are connected or not.
		return isConnected.get();
	}
	// ============================================================

	/*
	 * to know if the server accepts us or not.
	 */
	private boolean authenticate(String name) {//TODO: passwords.
		try {
			synchronized (authLock) {
				outputStream.writeObject(new UtilityMessage(MessageType.REQ_CONNECT, name, null));
				if (!authenticated) {
					authLock.wait(); //until notified
				}
			}

		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InterruptedException e) {
		}
		return authenticated;

	}

	// ============================================================

	/*
	 * starts the IncomingReader thread.
	 */
	private void listen() {
		new IncomingReader().start();
	}
	// ============================================================

	/*
	 * the thread that reads all incoming instances of Message from the
	 * server.
	 */
	private class IncomingReader extends Thread {

		public IncomingReader() {
			super("IncomingReader");

		}

		@Override
		public void run() {
			Message msg;

			try {
				while (isConnected.get()) {
					msg = (Message) inputStream.readObject();
					handleMessages(msg);
				}
			}
			catch (Exception ex) { // user disconnected.
				signOut();
				//				ex.printStackTrace();
			}
		}
		// ============================================================

		private final void handleMessages(Message msg) {
			// to determine in which category this message is, then handle it to the responsible method.

			MessageType type = msg.getType();

			switch (type.getCategory()) {

			case ERR:
				handleErrorMessage(msg);
				break;

			case INFO:
				handleInfoMessages(msg);
				break;

			default:
				break;

			}

			//notify the stream we have a new message.
			synchronized (streamLock) {
				messagesList.add(msg);
				streamLock.notifyAll();
			}

		}
		// ============================================================

		private final void handleErrorMessage(Message msg) {

			MessageType type;
			type = msg.getType();

			switch (type) {

			case ERR_INVALID_NAME://the server rejected us.
				synchronized (authLock) {
					authLock.notifyAll();
				}
				break;

			case ERR_CONNECTION_FAILURE:
				//TODO
				break;

			default:
				return;

			}

		}
		// ============================================================

		private final void handleInfoMessages(Message msg) {

			String targetName = msg.getAdditionalText();

			switch (msg.getType()) {

			case INFO_ADD:
				if (username.equals(targetName)) { //the server accepted us.

					authenticated = true;
					synchronized (authLock) {
						authLock.notifyAll();
					}
				}
				break;

			case INFO_REMOVE:
				//TODO
				break;

			case INFO_USERS_DATA:
				//TODO
				break;

			default:
				return;

			}

		}
		// ============================================================

	}

	// ============================================================

	public final String getUserName() {
		return username;
	}
	// ============================================================

	public StreamOfOutput<Message> getMessageStream() {
		return messagesStream;
	}
	// ============================================================

	public ArrayList<Message> getMessagesList() { //call when finished
		return new ArrayList<>(messagesList);
	}
	// ============================================================

	private void getUsers() {//TODO

	}
	// ============================================================

	public void sendMsg(String message) throws Exception {
		try {
			synchronized (outputStream) {

				outputStream.writeObject(new ChatMessage(username, message));
			}
		}
		catch (Exception ex) {
			throw ex;
		}
	}
	// ============================================================

	public boolean isConnected() {
		return isConnected.get();
	}
	// ============================================================

	public final boolean signOut() {
		if (isConnected.get() == true) {

			disconnect();

			//			if (disconHandler != null)
			//				disconHandler.act();
		}
		return !isConnected.get();
	}
	// ============================================================

	private final void disconnect() {
		try {
			if (!socket.isClosed())
				socket.close();
			isConnected.set(false);

			messagesStream.close();

			authenticated = false;
			inputStream.close();
			outputStream.close();

		}
		catch (IOException e) {//nothing to do here
		}
	}
	// ============================================================

}
