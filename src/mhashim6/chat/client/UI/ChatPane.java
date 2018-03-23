package mhashim6.chat.client.UI;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ChatPane extends ScrollPane {

	private VBox bubblesBox;

	public ChatPane() {
		bubblesBox = new VBox();
		bubblesBox.setMinSize(410, 275);
		bubblesBox.setMaxWidth(410);
		bubblesBox.getStyleClass().add("bubbles-box");

		setHbarPolicy(ScrollBarPolicy.NEVER);
		setPrefHeight(275);
		setPrefWidth(410);
		vvalueProperty().bind(bubblesBox.heightProperty());
		setContent(bubblesBox);

	}

	public VBox getBubblesBox() {
		return bubblesBox;
	}

	public void addHostMessageBubble(String text) {
		BubblePane bubblePane = new BubblePane(text, Pos.CENTER_RIGHT);
		bubblePane.getBubble().getStyleClass().add("hostMessageBubble");
		addBubblePane(bubblePane);

	}

	public void addGuestMessageBubble(String name, String text) {
		BubblePane bubblePane = new BubblePane(name.toUpperCase() + "\n" + text, Pos.CENTER_LEFT);
		bubblePane.getBubble().getStyleClass().add("guestMessageBubble");
		addBubblePane(bubblePane);

	}

	public void addInfoMessageBubble(String text) {
		BubblePane bubblePane = new BubblePane(text, Pos.CENTER);
		bubblePane.getBubble().getStyleClass().add("errMessageBubble");
		addBubblePane(bubblePane);
	}

	private void addBubblePane(BubblePane bubblePane) {
		Platform.runLater(() -> {
			bubblesBox.getChildren().add(bubblePane);
		});
	}

}
