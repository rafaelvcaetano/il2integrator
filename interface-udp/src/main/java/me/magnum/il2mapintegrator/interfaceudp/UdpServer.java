package me.magnum.il2mapintegrator.interfaceudp;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.core.entities.CommandRequest;
import me.magnum.il2mapintegrator.core.entities.Response;

public class UdpServer extends Thread {
	private IL2Integrator il2Integrator;
	private int port;
	private Gson gson;
	private DatagramSocket socket;
	private AtomicBoolean running;

	public UdpServer(IL2Integrator il2Integrator, int port) {
		this.il2Integrator = il2Integrator;
		this.port = port;
		this.gson = new Gson();
		this.running = new AtomicBoolean(false);
	}

	@Override
	public void run() {
		this.running.set(true);
		try {
			this.socket = new DatagramSocket(this.port);

			byte[] cmdBuffer = new byte[1024];
			while (this.running.get()) {
				DatagramPacket packet = new DatagramPacket(cmdBuffer, cmdBuffer.length);
				try {
					this.socket.receive(packet);
				} catch (IOException e) {
					if (!this.running.get())
						return;

					Logger.e(e);
					continue;
				}

				String data = new String(cmdBuffer, 0, packet.getLength());
				Logger.d("Got data: " + data);

				CommandRequest request = this.gson.fromJson(data, CommandRequest.class);
				Response response = this.il2Integrator.processCommand(request);

				this.sendResponse(response, packet.getSocketAddress());
			}
		} catch (IOException e) {
			Logger.e(e);
			this.running.set(false);
			this.socket = null;
		}
	}

	private void sendResponse(Response response, SocketAddress outAddress) {
		String outData = this.gson.toJson(response);
		byte[] byteData = outData.getBytes();

		DatagramPacket outputPacket = new DatagramPacket(byteData, byteData.length);
		outputPacket.setSocketAddress(outAddress);

		try {
			Logger.d("Sending response: " + outData);
			this.socket.send(outputPacket);
		} catch (IOException e) {
			Logger.e(e);
		}
	}

	public void stopServer() {
		if (!this.running.get())
			return;

		this.running.set(false);
		if (this.socket == null)
			return;

		this.socket.close();
	}
}
