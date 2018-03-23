package mhashim6.chat.server.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import mhashim6.Toolkit.Appender;
import mhashim6.chat.server.Server;

public class ServerUI extends VBox {

	private TextArea logArea;
	private GridPane btnsPane;
	private Button	 startBtn, stopBtn, usersBtn, clearBtn;

	private Server server;

	public ServerUI() {
		init();

		startBtn.setOnAction(event -> {
			if (server.startServer()) {
				startBtn.setDisable(true);
				stopBtn.setDisable(false);
				usersBtn.setDisable(false);
				logArea.appendText("--Server started successfully.\n");
			}
			else {
				logArea.appendText("--Error\n	couldn't start server\n");

			}
		});

		stopBtn.setOnAction(event -> {
			stopServer();

		});

		usersBtn.setOnAction(event -> {
			logArea.appendText("--Online users:\n" + server.getUsers());
		});

		clearBtn.setOnAction(event -> {
			logArea.setText("");
		});

	}

	private void init() {
		server = new Server(new Appender() {

			@Override
			public void appendText(String text) {
				logArea.appendText(text);
			}

			@Override
			public void appendErrText(String text) {
				logArea.appendText(text);

			}

		});

		server.setOnDisconnect(() -> {
			startBtn.setDisable(false);
			stopBtn.setDisable(true);
			usersBtn.setDisable(true);
			logArea.appendText("--Server stopped.\n");
		});

		setPadding(new Insets(15, 15, 15, 15));
		setSpacing(10);

		logArea = new TextArea();

		logArea.setPrefRowCount(20);
		logArea.setPrefWidth(270);
		logArea.setEditable(false);
		logArea.setWrapText(true);

		getChildren().add(logArea);
		btnsPane = new GridPane();
		btnsPane.setHgap(5);
		btnsPane.setVgap(8);
		btnsPane.setPadding(new Insets(5, 5, 5, 5));

		startBtn = new Button("Start");
		startBtn.setPrefWidth(100);

		stopBtn = new Button("Stop");
		stopBtn.setPrefWidth(100);
		stopBtn.setDisable(true);

		usersBtn = new Button("Online users");
		usersBtn.setPrefWidth(100);
		usersBtn.setDisable(true);

		clearBtn = new Button("Clear logs");
		clearBtn.setPrefWidth(100);

		btnsPane.add(startBtn, 1, 0);
		btnsPane.add(stopBtn, 1, 1);
		btnsPane.add(usersBtn, 43, 0);
		btnsPane.add(clearBtn, 43, 1);

		getChildren().add(btnsPane);

	}

	public void stopServer() {
		server.stopServer();
	}

}
