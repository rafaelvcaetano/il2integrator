package me.magnum.il2mapintegrator.interfacehttp.tests;

import com.google.gson.Gson;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import me.magnum.il2mapintegrator.core.ErrorCodes;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IL2IntegratorBuilder;
import me.magnum.il2mapintegrator.core.entities.ServiceList;
import me.magnum.il2mapintegrator.interfacehttp.HttpInterface;
import me.magnum.il2mapintegrator.interfacehttp.tests.mock.DummyService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class HttpInterfaceTests {
	private static IL2Integrator integrator;
	private static Gson gson;

	@BeforeClass
	public static void createIntegrator() {
		gson = new Gson();
		integrator = new IL2IntegratorBuilder()
				.addInterface(new HttpInterface(8080))
				.addService(new DummyService())
				.build();

		assertTrue(integrator.start());
	}

	@Test
	public void testListServices() {
		URL url = null;
		try {
			url = new URL("http://localhost:8080/internal/services");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		}

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		String output = this.readConnectionOutput(connection);
		assertNotNull(output);
		ServiceList response = gson.fromJson(output, ServiceList.class);
		assertEquals(1, response.services.size());
		assertEquals("dummy", response.services.get(0).key);
		assertEquals(ErrorCodes.SUCCESS, response.result);
		assertEquals("Success", response.message);

		connection.disconnect();
	}

	@Test
	public void testEmptyRequest() {
		URL url = null;
		try {
			url = new URL("http://localhost:8080");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		}

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		String output = this.readConnectionOutput(connection);
		assertNotNull(output);
		ServiceList response = gson.fromJson(output, ServiceList.class);
		assertEquals(ErrorCodes.ERR_INVALID_MESSAGE, response.result);
		assertEquals("Invalid request parameters", response.message);

		connection.disconnect();
	}

	@Test
	public void testNoCommand() {
		URL url = null;
		try {
			url = new URL("http://localhost:8080/internal");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		}

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		String output = this.readConnectionOutput(connection);
		assertNotNull(output);
		ServiceList response = gson.fromJson(output, ServiceList.class);
		assertEquals(ErrorCodes.ERR_INVALID_MESSAGE, response.result);
		assertEquals("Invalid request parameters", response.message);

		connection.disconnect();
	}

	@Test
	public void testMultiArgs() {
		URL url = null;
		try {
			url = new URL("http://localhost:8080/dummy/multi?one=1&two=2&three=3");
		} catch (MalformedURLException e) {
			e.printStackTrace();
			fail();
		}

		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		connection.setConnectTimeout(5000);
		connection.setReadTimeout(5000);

		String output = this.readConnectionOutput(connection);
		assertNotNull(output);
		ServiceList response = gson.fromJson(output, ServiceList.class);
		assertEquals(ErrorCodes.SUCCESS, response.result);
		assertEquals("Success", response.message);

		connection.disconnect();
	}

	@AfterClass
	public static void closeIntegrator() {
		integrator.stop();
	}

	private String readConnectionOutput(HttpURLConnection connection) {
		try {
			InputStream inStream;
			if (connection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST)
				inStream = connection.getInputStream();
			else
				inStream = connection.getErrorStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

			StringBuilder stringBuffer = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}
			reader.close();

			return stringBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		return null;
	}
}
