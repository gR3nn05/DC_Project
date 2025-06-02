package testbench;

import bench.cpu.CPUPrimeGenerator;
import logging.ConsoleLogger;
import logging.ILog;
import timing.ITimer;
import timing.Timer;
import logging.TimeUnit;

public class TestPrimeGenerator {
    public static void main(String[] args) {
        ITimer timer = new Timer();
        ILog log = new ConsoleLogger();
        CPUPrimeGenerator bench = new CPUPrimeGenerator();

        // Initialize
        bench.initialize();
        log.write("Benchmark initialized");

        // Warm-up
        log.write("Starting warm-up...");
        bench.warmUp();
        log.write("Warm-up completed");

        // Run benchmark
        log.write("Starting benchmark...");
        timer.start();
        bench.run();
        long time = timer.stop();

        // Get results
        String result = bench.getResult();
        int lastPrime = bench.getLastPrime();
        boolean overflow = bench.hitOverflow();

        // Calculate composite score
        double score = calculateCompositeScore(lastPrime, time, overflow);

        // Output results
        log.write("=== Benchmark Results ===");
        log.write(result);
        log.write("Stack overflow occurred: " + overflow);
        log.writeTime("Total execution time: ", time, TimeUnit.Milli);
        log.write(String.format("Composite score: %.2f", score));
    }

    private static double calculateCompositeScore(int primesFound, long timeNs, boolean overflow) {
        final double runtimeSeconds = TimeUnit.toTimeUnit(timeNs, TimeUnit.Sec);

        // Base score (primes per second)
        double baseScore = primesFound / runtimeSeconds;

        // Penalty for stack overflow (50% reduction)
        double penaltyFactor = overflow ? 0.5 : 1.0;

        // Normalized score (logarithmic scale 0-100)
        return Math.log1p(baseScore) * 10 * penaltyFactor;
    }
}