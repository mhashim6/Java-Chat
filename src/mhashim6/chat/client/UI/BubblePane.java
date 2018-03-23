package mhashim6.chat.client.UI;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BubblePane extends VBox {

	private Bubble bubble;

	public BubblePane(String text, Pos value) {
		getStyleClass().add("bubble-pane");
		setAlignment(value);
		bubble = new Bubble(text);

		getChildren().add(bubble);

	}

	public Bubble getBubble() {
		return bubble;
	}

	public static class Bubble extends Label {

		public Bubble(String text) {
			super();
			setBubbleText(text);
			getStyleClass().add("bubble");
			requestFocus();
		}

		public void setBubbleText(String text) {

			// TODO
			super.setText(text);
		}

	}
}
