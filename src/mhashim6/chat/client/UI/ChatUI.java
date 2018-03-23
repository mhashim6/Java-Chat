package mhashim6.chat.client.UI;

import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mhashim6.chat.client.Client;
import mhashim6.chat.exceptions.AuthenticationFailureException;
import mhashim6.chat.toolkit.StreamOfOutput;
import mhashim6.chat.wrappers.factories.messages.ChatMessage;
import mhashim6.chat.wrappers.factories.messages.Message;
import mhashim6.chat.wrappers.factories.messages.MessageCategory;

public class ChatUI extends VBox {

	private Client client;

	private Scene scene;

	private HBox configPanel;

	private VBox	  nameBox;
	private TextField nameTextField;
	private Label	  nameLabel;

	private VBox   btnsBox;
	private Button connect;
	private Button disconnect;

	private ChatPane chatPane;

	private TextField msgBox;

	private String	username;
	private boolean	isOn;
	// ============================================================

	public ChatUI() {

		getStyleClass().add("chat-ui");

		// ============================================================

		client = new Client();
		username = null;

		configPanel = new HBox();
		configPanel.setSpacing(200);
		configPanel.setAlignment(Pos.CENTER_RIGHT);

		// ============================================================

		btnsBox = new VBox(3);

		connect = new Button("Connect");
		connect.setPrefWidth(70);
		disconnect = new Button("Disonnect");
		disconnect.setPrefWidth(70);
		disconnect.setDisable(true);

		btnsBox.getChildren().addAll(connect, disconnect);

		// ============================================================

		nameBox = new VBox(3);
		nameBox.setAlignment(Pos.CENTER_RIGHT);

		nameLabel = new Label("اسم النجم");
		nameTextField = new TextField();

		nameBox.getChildren().addAll(nameLabel, nameTextField);

		configPanel.getChildren().addAll(btnsBox, nameBox);

		getChildren().addAll(configPanel, new Separator());

		// ============================================================

		chatPane = new ChatPane();

		getChildren().add(chatPane);
		getChildren().add(new Separator());

		// ============================================================

		msgBox = new TextField();
		msgBox.setPrefColumnCount(30);
		msgBox.setDisable(true);
		msgBox.setOnAction(event -> {
			try {
				client.sendMsg(msgBox.getText());
				msgBox.setText("");
			}
			catch (Exception e) {
				e.printStackTrace();
				chatPane.addInfoMessageBubble("failed to send the message.");

			}
			finally {
				msgBox.requestFocus();

			}
		});

		getChildren().add(msgBox);
		// ============================================================

		connect.setOnAction(event -> {

			try {
				if (client.connect(nameTextField.getText())) {
					username = client.getUserName();

					connect.setDisable(true);
					disconnect.setDisable(false);

					nameTextField.setDisable(true);
					msgBox.setDisable(false);
					msgBox.requestFocus();
					isOn = true;
					monitor();
				}

				else {
					chatPane.addInfoMessageBubble("Couldn't connect, try again!");
				}
			}
			catch (AuthenticationFailureException e) {
				chatPane.addInfoMessageBubble(e.toString());

			}

		});
		// ============================================================

		disconnect.setOnAction(event ->

		{
			signOut();

		});

	}
	// ============================================================

	public Scene newChatScene() {
		scene = new Scene(this);
		scene.getStylesheets().add("client-style.css");

		return scene;
	}

	private void monitor() {
		StreamOfOutput<Message> stream = client.getMessageStream();

		new Thread(() -> {
			Message msg;
			try {
				while ((msg = stream.read()) != null) {
					handleMessage(msg);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			signOut();

		}, "View's monitor").start();
	}
	// ============================================================

	private final void handleMessage(Message msg) {

		MessageCategory category = msg.getType().getCategory();

		switch (category) {

		case CHAT:
			handleChat((ChatMessage) msg);

			break;

		case ERR://TODO

			break;

		case INFO:
			handleInfoMessages(msg);
			break;

		default:
			return;

		}

	}
	// ============================================================

	private final void handleChat(ChatMessage msg) {
		String tempName;
		tempName = msg.getSenderName();

		if (username.equals(tempName))
			chatPane.addHostMessageBubble(msg.getAdditionalText());
		else
			chatPane.addGuestMessageBubble(tempName, msg.getAdditionalText());

	}
	// ============================================================

	private final void handleInfoMessages(Message msg) {

		String tempName = msg.getAdditionalText();

		switch (msg.getType()) {

		case INFO_ADD:
			if (username.equals(tempName)) {

				chatPane.addInfoMessageBubble("Connected.");

			}
			else
				chatPane.addInfoMessageBubble(tempName + " connected.");

			break;

		case INFO_REMOVE:
			chatPane.addInfoMessageBubble(tempName + " is now offilne.");
			break;

		default:
			return;

		}

	}
	// ============================================================

	public void signOut() {
		if (isOn) {
			client.signOut();
			isOn = false;
			connect.setDisable(false);
			disconnect.setDisable(true);

			nameTextField.setDisable(false);
			msgBox.setDisable(true);
			chatPane.addInfoMessageBubble("Disconnected.");
		}
	}
	// ============================================================

}
