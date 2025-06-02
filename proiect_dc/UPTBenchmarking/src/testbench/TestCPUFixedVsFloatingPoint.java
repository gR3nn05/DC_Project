package testbench;

import bench.cpu.CPUFixedVsFloatingPoint;
import bench.cpu.NumberRepresentation;
import bench.IBenchmark;
import logging.ConsoleLogger;
import logging.ILog;
import logging.TimeUnit;
import timing.ITimer;
import timing.Timer;

public class TestCPUFixedVsFloatingPoint {
	public static void main(String[] args) {
		ITimer timer = new Timer();
		ILog log = new ConsoleLogger();
		TimeUnit timeUnit = TimeUnit.Milli;

		IBenchmark bench = new CPUFixedVsFloatingPoint();
		bench.initialize(10_000_000);
		bench.warmUp();

		// Fixed Point Test
		timer.start();
		bench.run(NumberRepresentation.FIXED);
		long timeFixed = timer.stop();
		log.writeTime("Fixed-point time:", timeFixed, timeUnit);
		log.write("Fixed result:", bench.getResult());

		// Floating Point Test
		bench.initialize(10_000_000);
		bench.warmUp();
		timer.start();
		bench.run(NumberRepresentation.FLOATING);
		long timeFloat = timer.stop();
		log.writeTime("Floating-point time:", timeFloat, timeUnit);
		log.write("Floating result:", bench.getResult());

		bench.clean();
		log.close();
	}
}
