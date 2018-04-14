package me.magnum.il2mapintegrator.core.tests;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IL2IntegratorBuilder;
import me.magnum.il2mapintegrator.core.Logger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class LoggerTests {
	private static IL2Integrator integrator;
	private static ByteArrayOutputStream outputStream;

	@BeforeClass
	public static void setupIntegrator() {
		outputStream = new ByteArrayOutputStream();

		integrator = new IL2IntegratorBuilder()
				.setLoggerOutput(new PrintStream(outputStream, true))
				.build();
		assertTrue(integrator.start());
	}

	@Before
	public void clearLog() {
		outputStream.reset();
		Logger.setLogLevel(Logger.LEVEL_DEBUG);
	}

	@Test
	public void testDebugMessages() {
		Logger.d("Test");
		Logger.d("tseT");
		Logger.d(null);

		String logOut = outputStream.toString();
		assertTrue(logOut.length() > 0);
		assertTrue(logOut.contains("INFO: Test"));
		assertTrue(logOut.contains("INFO: tseT"));
	}

	@Test
	public void testWarnMessages() {
		Logger.w("Test");
		Logger.w("tseT");
		Logger.w(null);

		String logOut = outputStream.toString();
		assertTrue(logOut.length() > 0);
		assertTrue(logOut.contains("WARN: Test"));
		assertTrue(logOut.contains("WARN: tseT"));
	}

	@Test
	public void testErrorMessages() {
		Logger.e("Test");
		Logger.e("tseT");
		Logger.e(null);

		String logOut = outputStream.toString();
		assertTrue(logOut.length() > 0);
		assertTrue(logOut.contains("ERR: Test"));
		assertTrue(logOut.contains("ERR: tseT"));
	}

	@Test
	public void testExceptionMessage() {
		Logger.e(new Exception("Exception message"));
		Logger.e("tseT");

		String logOut = outputStream.toString();
		assertTrue(logOut.length() > 0);
		assertTrue(logOut.contains("ERR: Exception message"));
		assertTrue(logOut.contains("ERR: tseT"));
	}

	@Test
	public void testLogLevelDebug() {
		Logger.setLogLevel(Logger.LEVEL_DEBUG);

		Logger.d("Test DEBUG");
		Logger.w("Test WARN");
		Logger.e("Test ERROR");

		String logOut = outputStream.toString();
		assertTrue(logOut.length() > 0);
		assertTrue(logOut.contains("INFO: Test DEBUG"));
		assertTrue(logOut.contains("WARN: Test WARN"));
		assertTrue(logOut.contains("ERR: Test ERROR"));
	}

	@Test
	public void testLogLevelWarn() {
		Logger.setLogLevel(Logger.LEVEL_WARN);

		Logger.d("Test DEBUG");
		Logger.w("Test WARN");
		Logger.e("Test ERROR");

		String logOut = outputStream.toString();
		assertTrue(logOut.length() > 0);
		assertFalse(logOut.contains("INFO: Test DEBUG"));
		assertTrue(logOut.contains("WARN: Test WARN"));
		assertTrue(logOut.contains("ERR: Test ERROR"));
	}

	@Test
	public void testLogLevelError() {
		Logger.setLogLevel(Logger.LEVEL_ERROR);

		Logger.d("Test DEBUG");
		Logger.w("Test WARN");
		Logger.e("Test ERROR");

		String logOut = outputStream.toString();
		assertTrue(logOut.length() > 0);
		assertFalse(logOut.contains("INFO: Test DEBUG"));
		assertFalse(logOut.contains("WARN: Test WARN"));
		assertTrue(logOut.contains("ERR: Test ERROR"));
	}

	@Test
	public void testLogLevelNone() {
		Logger.setLogLevel(Logger.LEVEL_NONE);

		Logger.d("Test DEBUG");
		Logger.w("Test WARN");
		Logger.e("Test ERROR");

		String logOut = outputStream.toString();
		assertEquals(0, logOut.length());
		assertFalse(logOut.contains("INFO: Test DEBUG"));
		assertFalse(logOut.contains("WARN: Test WARN"));
		assertFalse(logOut.contains("ERR: Test ERROR"));
	}

	@AfterClass
	public static void stopIntegrator() {
		integrator.stop();
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
