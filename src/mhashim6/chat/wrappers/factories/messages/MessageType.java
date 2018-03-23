package mhashim6.chat.wrappers.factories.messages;

public enum MessageType {

	//regular message (clients)
	CHAT(MessageCategory.CHAT), 

	//info messages (server)
	INFO_ADD(MessageCategory.INFO),
	INFO_REMOVE(MessageCategory.INFO),
	INFO_USERS_DATA(MessageCategory.INFO),
	INFO_SERVER_SHUTDOWN(MessageCategory.INFO), 

	// requests (clients)
	REQ_CONNECT(MessageCategory.REQUEST),
	REQ_DISCONNECT(MessageCategory.REQUEST),
	REQ_GET_USERS_DATA(MessageCategory.REQUEST), 

	//error messages (server)
	ERR_INVALID_NAME(MessageCategory.ERR),
	ERR_CONNECTION_FAILURE(MessageCategory.ERR); 

	//===================================================
	MessageType(MessageCategory category) {

		this.category = category;
	}

	private MessageCategory category;

	public MessageCategory getCategory() {
		return category;
	}
}
