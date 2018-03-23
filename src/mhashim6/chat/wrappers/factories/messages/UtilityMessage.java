package mhashim6.chat.wrappers.factories.messages;

public class UtilityMessage implements Message {

	private static final long serialVersionUID = 2169851977468240694L;
	private final long	  ID		   = System.currentTimeMillis();

	private MessageType infoType;
	private String	    name;
	private String	    additionalText;

	public UtilityMessage(MessageType infoType, String senderName, String additionalText) {
		this.name = senderName;
		this.infoType = infoType;
		this.additionalText = additionalText;
	}

	@Override
	public long getID() {
		return ID;
	}

	@Override
	public MessageType getType() {
		return infoType;
	}

	@Override
	public String getSenderName() {
		return name;
	}

	@Override
	public String getAdditionalText() {
		return additionalText;
	}

	@Override
	public String toString() {
		return messageInfoString();
	}

	@Override
	public long getInitTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
