package me.magnum.il2mapintegrator.interfaceudp.tests;

import com.google.gson.Gson;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.SocketException;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IL2IntegratorBuilder;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.entities.CommandRequest;
import me.magnum.il2mapintegrator.core.entities.Response;
import me.magnum.il2mapintegrator.core.entities.ServiceList;
import me.magnum.il2mapintegrator.interfaceudp.UdpInterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UdpInterfaceTests {
	private static IL2Integrator il2Integrator;
	private static Gson gson;
	private static DatagramSocket udpSocket;

	@BeforeClass
	public static void createService() {
		il2Integrator = new IL2IntegratorBuilder()
				.addInterface(new UdpInterface(12345))
				.addService(new DummyService())
				.build();
		assertTrue(il2Integrator.start());
		gson = new Gson();

		try {
			udpSocket = new DatagramSocket();
			udpSocket.setSoTimeout(10000);
		} catch (SocketException e) {
			Logger.e(e);
		}
	}

	@Test
	public void testListServices() {
		CommandRequest request = new CommandRequest();
		request.service = "internal";
		request.command = "services";

		try {
			ServiceList response = this.sendAndWait(request, ServiceList.class);
			assertNotEquals(null, response);
			assertEquals(ErrorCodes.SUCCESS, response.result);
			assertEquals("Success", response.message);
			assertEquals(1, response.services.size());
			assertEquals("dummy", response.services.get(0).key);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testInvalidCommand() {
		CommandRequest invalidRequest = new CommandRequest();
		invalidRequest.service = "internal";
		invalidRequest.command = "invalid";

		try {
			Response response = this.sendAndWait(invalidRequest, Response.class);
			assertNotEquals(null, response);
			assertEquals(ErrorCodes.ERR_INVALID_COMMAND, response.result);
			assertEquals("Invalid command", response.message);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@Test
	public void testErrorCommand() {
		CommandRequest errorRequest = new CommandRequest();
		errorRequest.service = "dummy";
		errorRequest.command = "null";

		try {
			Response response = this.sendAndWait(errorRequest, Response.class);
			assertNotEquals(null, response);
			assertEquals(ErrorCodes.ERR_COMMAND_ERROR, response.result);
			assertEquals("Command error", response.message);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	@AfterClass
	public static void cleanupService() {
		udpSocket.close();
		il2Integrator.stop();
	}

	private <T> T sendAndWait(CommandRequest request, Class<T> tClass) throws IOException  {
		String out = gson.toJson(request);
		byte[] outData = out.getBytes();

		DatagramPacket outPacket = new DatagramPacket(outData, outData.length, Inet4Address.getLocalHost(), 12345);
		udpSocket.send(outPacket);

		byte[] inData = new byte[256];
		DatagramPacket inPacket = new DatagramPacket(inData, inData.length);
		udpSocket.receive(inPacket);

		String in = new String(inPacket.getData(), 0, inPacket.getLength());
		return gson.fromJson(in, tClass);
	}
}
