package testbench;

import bench.DemoBenchmark;
import bench.IBenchmark;
import logging.ConsoleLogger;
import logging.FileLogger;
import logging.ILogger;
import logging.TimeUnit;
import timing.ITimer;
import timing.Timer;

public class TestDemoBench {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILogger consoleLogger = new ConsoleLogger();
        ILogger fileLogger = new FileLogger("results.txt");
        IBenchmark benchmark = new DemoBenchmark();

        int size = 10000;

        benchmark.initialize(size);
        timer.start();
        benchmark.run();
        long elapsed = timer.stop();

        consoleLogger.writeTime("Elapsed time:", elapsed, TimeUnit.Milli);
        fileLogger.writeTime("Elapsed time:", elapsed, TimeUnit.Milli);

        benchmark.clean();
        consoleLogger.close();
        fileLogger.close();
    }
}