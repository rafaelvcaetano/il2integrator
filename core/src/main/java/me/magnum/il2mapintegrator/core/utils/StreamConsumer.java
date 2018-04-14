package me.magnum.il2mapintegrator.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import me.magnum.il2mapintegrator.core.Logger;

/**
 * Thread that consumes the given {@link InputStream} without outputting it. The thread runs until
 * {@code null} is returned.
 */
public class StreamConsumer extends Thread {
	private InputStream stream;

	public StreamConsumer(InputStream stream) {
		if (stream == null)
			throw new IllegalArgumentException("stream cannot be null");

		this.stream = stream;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.stream))){
			while (reader.readLine() != null);
			Logger.d("Stream consumption finished");
		} catch (IOException ex) {
			Logger.e(ex);
		}
	}
}
