package testbench;

import bench.DemoBenchmarkSleep;
import bench.IBenchmark;
import logging.ConsoleLogger;
import logging.ILogger;
import logging.TimeUnit;
import timing.ITimer;
import timing.Timer;

public class TestDemoSleep {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger logger = new ConsoleLogger();
        IBenchmark benchmark = new DemoBenchmarkSleep();

        int sleepTimeMs = 500;
        benchmark.initialize(sleepTimeMs);

        timer.start();
        benchmark.run();
        long elapsedNano = timer.stop();

        logger.writeTime("Measured time:", elapsedNano, TimeUnit.Milli);

        long expectedNano = sleepTimeMs * 1_000_000L;
        double offset = 100.0 * (elapsedNano - expectedNano) / expectedNano;

        logger.write(String.format("Offset: %.2f%%", offset));

        System.out.println("\nTesting resume-pause sequence:");
        benchmark.initialize(sleepTimeMs);
        timer.start();

        for (int i = 0; i < 3; i++) {
            timer.resume();
            benchmark.run();
            long partial = timer.pause();
            logger.writeTime("Paused time (" + (i+1) + "):", partial, TimeUnit.Milli);
        }

        long totalTime = timer.stop();
        logger.writeTime("Total time:", totalTime, TimeUnit.Milli);

        logger.close();
    }
}