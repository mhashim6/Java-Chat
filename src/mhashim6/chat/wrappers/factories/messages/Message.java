package mhashim6.chat.wrappers.factories.messages;

import java.io.Serializable;

import mhashim6.chat.wrappers.ChatElementWrapper;

public interface Message extends Serializable, Comparable<Message>, ChatElementWrapper {

	MessageType getType();
	// ============================================================

	String getSenderName();
	// ============================================================

	String getAdditionalText();
	// ============================================================

	long getInitTime();
	// ============================================================

	@Override
	default int compareTo(Message otherMsg) {
		if (getInitTime() > otherMsg.getInitTime())
			return 1;
		else
			return 0;
	}
	// ============================================================

	default String messageInfoString() {
		return String.format("	Type: %s\n	Sender: %s\n	Additonal Text: %s\n", getType(),
				getSenderName(), getAdditionalText());
	}
	// ============================================================

}
