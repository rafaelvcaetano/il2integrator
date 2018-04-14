package me.magnum.il2mapintegrator.gui;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import me.magnum.il2mapintegrator.career.CareerService;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.IL2IntegratorBuilder;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.interfacehttp.HttpInterface;
import me.magnum.il2mapintegrator.interfaceudp.UdpInterface;
import me.magnum.il2mapintegrator.pwcg.PWCGService;

public class Main {
	public static void main(String[] args) {
		IL2Integrator il2Integrator = new IL2IntegratorBuilder()
				.addInterface(new HttpInterface(8080))
				.addInterface(new UdpInterface(25005))
				.addService(new CareerService())
				.addService(new PWCGService())
				.setLoggerOutput(getLogStream())
				.build();

		Runtime.getRuntime().addShutdownHook(new Thread(il2Integrator::stop));

		new IL2IntegratorController(new CommandInterface(il2Integrator));
	}

	private static PrintStream getLogStream() {
		File file = new File("integrator-log.txt");
		if (file.isFile()) {
			file.delete();
		}

		try {
			if (file.createNewFile())
				return new PrintStream(file);
		} catch (IOException e) {
			Logger.e(e);
		}
		return System.out;
	}
}
