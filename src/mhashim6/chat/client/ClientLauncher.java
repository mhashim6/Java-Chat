package mhashim6.chat.client;

import javafx.application.Application;
import javafx.stage.Stage;
import mhashim6.chat.client.UI.ChatUI;

public class ClientLauncher extends Application {

	static Stage primaryStage;

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		primaryStage = stage;

		showChatScene();
		stage.setResizable(false);

		stage.show();
	}

	public static void showLogInScene() {
		//		logInUI = new LogInUI();
		//		Scene scene = new Scene(LogInUI);
		//		scene.getStylesheets().add("client-style.css");
		//		stage.setScene(value);
	}

	public static void showChatScene() {
		ChatUI chatUI = new ChatUI();

		primaryStage.setScene(chatUI.newChatScene());
		primaryStage.setOnCloseRequest(event -> {
			chatUI.signOut();
			primaryStage.close();
		});
	}

}
