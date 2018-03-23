package mhashim6.chat.client.UI;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import mhashim6.chat.client.Client;
import mhashim6.chat.client.ClientLauncher;
import mhashim6.chat.exceptions.AuthenticationFailureException;

public class LogInUI extends VBox {

	private Client client;

	private Scene scene;

	private HBox	      nameBox;
	private TextField     nameTextField;
	private PasswordField passwordTextField;
	private Label	      nameLabel;

	private Button connect;

	private String username;

	public LogInUI() {
		getStyleClass().add("login-ui");

		client = new Client();
		username = null;

		nameLabel = new Label("Name");
		nameTextField = new TextField();
		passwordTextField = new PasswordField();
		passwordTextField.setDisable(true);

		nameBox = new HBox(3);
		nameBox.getChildren().addAll(nameLabel, nameTextField);
		getChildren().add(nameBox);
		getChildren().add(passwordTextField);

		connect = new Button("Sign in");
		connect.setPrefWidth(70);

		connect.setOnAction(event -> {

			try {
				if (client.connect(nameTextField.getText())) {
					username = client.getUserName();
//					ClientLauncher.showChatScene(username);
				}
				else
					System.out.println("couldn't connect");
			}
			catch (AuthenticationFailureException e) {
				System.out.println("couldn't connect");

			}
		});

		getChildren().add(connect);
	}

	public Scene newLogInScene() {
		scene = new Scene(this);
		scene.getStylesheets().add("login-style.css");

		return scene;
	}

}
