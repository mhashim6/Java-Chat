package mhashim6.chat.wrappers.factories.messages;

import mhashim6.chat.wrappers.factories.Group;

public class ChatMessage extends UtilityMessage {

	private static final long serialVersionUID = -2965589616787823525L;
	
	private long		  targetID;

	public ChatMessage(String name, String text) {
		this(name, text, Group.PLAYGROUND_ID);
	}

	public ChatMessage(String name, String text, long targetID) {
		super(MessageType.CHAT, name, text);
		this.targetID = targetID;
	}

	public long getTargetID() {
		return targetID;
	}

}
