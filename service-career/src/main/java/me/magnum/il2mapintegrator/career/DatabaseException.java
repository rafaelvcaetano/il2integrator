package me.magnum.il2mapintegrator.career;

public class DatabaseException extends Exception {
	public DatabaseException(String message) {
		super(message);
	}

	public DatabaseException(Throwable cause) {
		super(cause);
	}
}
