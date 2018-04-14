package me.magnum.il2mapintegrator.core;

/**
 * Error codes that can be returned the the integrator when processing a command.
 */
public final class ErrorCodes {
	/**
	 * Indicates that the command ran successfully.
	 */
	public static final int SUCCESS = 0;

	/**
	 * Indicates that the given message was invalid and could not be processed.
	 */
	public static final int ERR_INVALID_MESSAGE = 100;

	/**
	 * Indicates that the command is invalid. This generally means that the command may not be
	 * registered.
	 */
	public static final int ERR_INVALID_COMMAND = 101;

	/**
	 * Indicates that there was an error executing the command.
	 */
	public static final int ERR_COMMAND_ERROR = 102;

	private ErrorCodes() {}
}
