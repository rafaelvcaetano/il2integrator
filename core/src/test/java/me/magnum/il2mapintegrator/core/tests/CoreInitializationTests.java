package me.magnum.il2mapintegrator.core.tests;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IL2IntegratorBuilder;
import me.magnum.il2mapintegrator.core.tests.mock.BadDummyInterface;
import me.magnum.il2mapintegrator.core.tests.mock.DummyInterface;
import me.magnum.il2mapintegrator.core.tests.mock.DummyService;
import me.magnum.il2mapintegrator.core.tests.utils.CommandSender;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class CoreInitializationTests {
	@Test
	public void testNoInterfaces() {
		IL2Integrator integrator = new IL2IntegratorBuilder().build();
		assertTrue(integrator.start());
		try {
			CommandSender.sendCommand(integrator, "internal", "services");
		} catch (IllegalStateException e) {
			fail();
		}
	}

	@Test
	public void testNullInterface() {
		IL2IntegratorBuilder builder = new IL2IntegratorBuilder();
		try {
			builder.addInterface(null);
			fail();
		} catch (IllegalArgumentException ex) {
		}
		builder.addInterface(new DummyInterface());

		assertTrue(builder.build().start());
	}

	@Test
	public void testThrowsExceptionWhenNotStarted() {
		IL2Integrator integrator = new IL2IntegratorBuilder()
				.addInterface(new DummyInterface())
				.addService(new DummyService())
				.build();

		try {
			CommandSender.sendCommand(integrator, "dummy", "dummy");
			fail();
		} catch (IllegalStateException e) {
		}
	}

	@Test
	public void testAddInvalidInterface() {
		IL2Integrator integrator = new IL2IntegratorBuilder()
				.addInterface(new BadDummyInterface())
				.addService(new DummyService())
				.build();

		assertTrue(integrator.start());
		try {
			CommandSender.sendCommand(integrator, "dummy", "dummy");
		} catch (IllegalStateException e) {
			fail();
		}
	}

	@Test
	public void testAddSameService() {
		try {
			new IL2IntegratorBuilder()
					.addInterface(new BadDummyInterface())
					.addService(new DummyService())
					.addService(new DummyService())
					.build();
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testCustomLogger() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(out, true);

		IL2Integrator service = new IL2IntegratorBuilder()
				.addInterface(new DummyInterface())
				.setLoggerOutput(ps)
				.build();

		assertTrue(service.start());

		// This should fail because the dummy service was not added. As such, output will be forced
		// into the log
		CommandSender.sendCommand(service, "dummy", "dummy");
		String log = out.toString();

		assertTrue(log.length() > 0);
		service.stop();
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
