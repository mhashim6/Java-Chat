package mhashim6.chat.server;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mhashim6.chat.server.UI.ServerUI;

public class ServerLauncher extends Application {

	ServerUI serverUI;

	public static void main(String args[]) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		serverUI = new ServerUI();
		Scene scene = new Scene(serverUI);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setOnCloseRequest(event -> {
			serverUI.stopServer();
			stage.close();
		});
		stage.show();
	}

}
