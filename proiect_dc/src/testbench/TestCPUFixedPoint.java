package testbench;

import logging.ConsoleLogger;
import logging.ILog;
import logging.TimeUnit;
import timing.ITimer;
import timing.Timer;
import bench.IBenchmark;
import bench.cpu.CPUFixedPoint;

public class TestCPUFixedPoint {

    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILog log = new ConsoleLogger();
        TimeUnit timeUnit = TimeUnit.Milli;

        IBenchmark bench = new CPUFixedPoint();
        final int size = 10000000; // 10 million operations

        // Initialize benchmark
        bench.initialize(size);
        log.write("Initialized with size: ", size);

        // Warm-up
        log.write("Warming up...");
        bench.warmUp();

        // Run benchmark with dummy parameter
        log.write("Starting benchmark...");
        timer.start();
        bench.run(new Object()); // Changed to use run(Object...)
        long time = timer.stop();

        // Calculate MOPS (approximate)
        // Total operations per iteration:
        // arithmeticTest: ~29 ops, branchingTest: ~12 ops, arrayTest: ~10 ops
        final double TOTAL_OPS_PER_ITERATION = 29 + 12 + 10;
        double mops = (TOTAL_OPS_PER_ITERATION * size) / (TimeUnit.toTimeUnit(time, TimeUnit.Sec) * 1e6);

        // Results
        log.writeTime("Finished in", time, timeUnit);
        log.write("MOPS: ", String.format("%.2f", mops));
        log.write("Result: ", bench.getResult());

        // Cleanup
        bench.clean();
        log.close();
    }
}