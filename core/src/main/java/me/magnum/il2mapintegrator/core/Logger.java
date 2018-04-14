package me.magnum.il2mapintegrator.core;

import java.io.PrintStream;

public class Logger {
	public static final int LEVEL_DEBUG = 0;
	public static final int LEVEL_WARN = 1;
	public static final int LEVEL_ERROR = 2;
	public static final int LEVEL_NONE = 3;

	/**
	 * The current log level.
	 */
	private static int logLevel = LEVEL_DEBUG;

	/**
	 * The {@link PrintStream} to which logs will be outputted. Defaults to the {@link System#out}.
	 */
	private static PrintStream output = new PrintStream(System.out);

	/**
	 * Sets the {@link PrintStream} to which logs will be outputted.
	 */
	protected static void setOutput(PrintStream outStream) {
		output = outStream;
	}

	public static void setLogLevel(int level) {
		logLevel = level;
	}

	public static void d(Object message) {
		if (logLevel > LEVEL_DEBUG)
			return;

		output.println("INFO: " + message);
	}

	public static void w(Object message) {
		if (logLevel > LEVEL_WARN)
			return;

		output.println("WARN: " + message);
	}

	public static void e(Object message) {
		if (logLevel > LEVEL_ERROR)
			return;

		if (message instanceof Exception) {
			Exception exception = (Exception) message;
			output.println("ERR: " + exception.getMessage());
			exception.printStackTrace(output);
		}
		else
			output.println("ERR: " + message);
	}
}
