package mhashim6.chat.toolkit;

import java.io.IOException;

public interface StreamOfOutput<T> {

	T read() throws IOException;
	// ============================================================

	void close();
	// ============================================================

	boolean isClosed();
	// ============================================================

	void reset();
	// ============================================================

}
