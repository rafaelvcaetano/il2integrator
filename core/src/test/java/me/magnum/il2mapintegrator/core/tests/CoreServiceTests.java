package me.magnum.il2mapintegrator.core.tests;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IL2IntegratorBuilder;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.core.entities.ServiceList;
import me.magnum.il2mapintegrator.core.tests.mock.BadDummyService;
import me.magnum.il2mapintegrator.core.tests.mock.DummyInterface;
import me.magnum.il2mapintegrator.core.tests.mock.DummyResponse;
import me.magnum.il2mapintegrator.core.tests.mock.DummyService;
import me.magnum.il2mapintegrator.core.tests.utils.CommandSender;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoreServiceTests {
	private static IL2Integrator il2Integrator;
	private static DummyService dummyService;
	private static DummyInterface dummyInterface;

	@BeforeClass
	public static void startService() {
		dummyService = new DummyService();
		dummyInterface = new DummyInterface();
		assertFalse(dummyService.isInitialized());
		assertFalse(dummyInterface.isStarted());

		il2Integrator = new IL2IntegratorBuilder()
				.addInterface(dummyInterface)
				.addService(dummyService)
				.addService(new BadDummyService())
				.build();

		assertTrue(il2Integrator.start());
		assertTrue(dummyService.isInitialized());
		assertTrue(dummyInterface.isStarted());
	}

	@Test
	public void testDummyServiceAvailable() {
		Response response =  CommandSender.sendCommand(il2Integrator, "internal", "services");
		assertNotNull(response);
		assertTrue(response instanceof ServiceList);

		ServiceList serviceList = (ServiceList) response;
		assertEquals(1, serviceList.services.size());
		assertEquals("dummy", serviceList.services.get(0).key);
		assertEquals("Dummy", serviceList.services.get(0).name);
		assertEquals(0, serviceList.services.get(0).versionCode);
		assertEquals("1.0", serviceList.services.get(0).version);
	}

	@Test
	public void testDummyCommand() {
		Response response = CommandSender.sendCommand(il2Integrator, "dummy", "dummy");
		assertTrue(response instanceof DummyResponse);
		DummyResponse dummyResponse = (DummyResponse) response;

		assertNotNull(response);
		assertEquals(ErrorCodes.SUCCESS, dummyResponse.result);
		assertEquals("Success", dummyResponse.message);
		assertEquals("dummy", dummyResponse.dummyData);
	}

	@Test
	public void testInvalidCommand() {
		Response response = CommandSender.sendCommand(il2Integrator, "dummy", "invalid");
		assertFalse(response instanceof DummyResponse);

		assertNotNull(response);
		assertEquals(ErrorCodes.ERR_INVALID_COMMAND, response.result);
		assertEquals("Invalid command", response.message);
	}

	@Test
	public void testBadCommand() {
		Response response = CommandSender.sendCommand(il2Integrator, "dummy", "bad");
		assertFalse(response instanceof DummyResponse);

		assertNotNull(response);
		assertEquals(ErrorCodes.ERR_COMMAND_ERROR, response.result);
		assertEquals("Command error", response.message);
	}

	@Test
	public void testNullRequest() {
		Response response = il2Integrator.processCommand(null);
		assertNotNull(response);
		assertEquals(ErrorCodes.ERR_INVALID_MESSAGE, response.result);
		assertEquals("Request is null", response.message);
	}

	@Test
	public void testNullService() {
		Response response = CommandSender.sendCommand(il2Integrator, null, "bad");
		assertNotNull(response);
		assertEquals(ErrorCodes.ERR_INVALID_MESSAGE, response.result);
		assertEquals("Invalid request parameters", response.message);
	}

	@Test
	public void testNullCommand() {
		Response response = CommandSender.sendCommand(il2Integrator, "dummy", null);
		assertNotNull(response);
		assertEquals(ErrorCodes.ERR_INVALID_MESSAGE, response.result);
		assertEquals("Invalid request parameters", response.message);
	}

	@Test
	public void testNullArgs() {
		Response response = CommandSender.sendCommand(il2Integrator, "dummy", "dummy", null);
		assertNotNull(response);
		assertEquals(ErrorCodes.SUCCESS, response.result);
		assertEquals("Success", response.message);
	}

	@AfterClass
	public static void stopService() {
		assertNotNull(il2Integrator);
		il2Integrator.stop();

		assertFalse(dummyService.isInitialized());
		assertFalse(dummyInterface.isStarted());
	}
}
