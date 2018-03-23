package mhashim6.chat.client.UI;

import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChatPane2 extends HBox {

	private ScrollBar scrollBar;
	private VBox	  bubblesBox;

	public ChatPane2() {
		// setSpacing(10);
		// setMinSize(410, 275);
		getStyleClass().add("chat-pane");

		bubblesBox = new VBox();
		bubblesBox.setMinSize(380, 275);
		bubblesBox.setMaxSize(380, 275);
		bubblesBox.getStyleClass().add("bubbles-box");

		scrollBar = new ScrollBar();
		scrollBar.setOrientation(Orientation.VERTICAL);
		scrollBar.setPrefHeight(bubblesBox.getHeight());

		// scrollBar.valueProperty().bind(get());

		scrollBar.valueProperty().addListener((ov, old_val, new_val) -> {
			bubblesBox.setLayoutY(bubblesBox.getLayoutY() - new_val.doubleValue());

		});

		getChildren().addAll(bubblesBox, scrollBar);

	}

	DoubleBinding get() {
		return scrollBar.valueProperty().divide(bubblesBox.heightProperty())
				.subtract(scrollBar.valueProperty());
	}

	public void addHostMessageBubble(String text) {
		BubblePane bubblePane = new BubblePane(text, Pos.CENTER_RIGHT);
		bubblePane.getBubble().getStyleClass().add("hostMessageBubble");
		bubblesBox.getChildren().add(bubblePane);

	}

	public void addGuestMessageBubble(String name, String text) {
		BubblePane bubblePane = new BubblePane(name + "\n" + text, Pos.CENTER_LEFT);
		bubblePane.getBubble().getStyleClass().add("guestMessageBubble");
		bubblesBox.getChildren().add(bubblePane);
	}

	public void addErrMessageBubble(String text) {
		BubblePane bubblePane = new BubblePane(text, Pos.CENTER);
		bubblePane.getBubble().getStyleClass().add("errMessageBubble");
		bubblesBox.getChildren().add(bubblePane);

	}

}
