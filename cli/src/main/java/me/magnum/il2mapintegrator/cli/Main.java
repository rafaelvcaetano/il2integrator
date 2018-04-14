package me.magnum.il2mapintegrator.cli;

import me.magnum.il2mapintegrator.career.CareerService;
import me.magnum.il2mapintegrator.core.IL2IntegratorBuilder;
import me.magnum.il2mapintegrator.core.IL2Integrator;
import me.magnum.il2mapintegrator.core.Logger;
import me.magnum.il2mapintegrator.pwcg.PWCGService;

public class Main {
	public static void main(String[] args) {
		IL2Integrator il2Integrator = new IL2IntegratorBuilder()
				.addService(new CareerService())
				.addService(new PWCGService())
				.build();

		il2Integrator.start();

		DebugThread debugThread = new DebugThread(il2Integrator);
		debugThread.start();
		try {
			// Wait for the debug thread to exit
			debugThread.join();
		} catch (InterruptedException e) {
			Logger.e(e);
		}
		il2Integrator.stop();
	}
}
