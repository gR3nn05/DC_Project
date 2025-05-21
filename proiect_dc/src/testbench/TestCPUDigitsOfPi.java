package testbench;

import bench.cpu.CPUDigitsOfPi;
import bench.IBenchmark;

import java.io.FileWriter;
import java.io.IOException;

public class TestCPUDigitsOfPi {
    public static void main(String[] args) {
        IBenchmark bench = new CPUDigitsOfPi();
        int[] digitCounts = {50, 100, 500, 1000, 2000, 5000, 10000};
        int method = 3; // 1 = Leibniz, 2 = Magic, 3 = Math

        try (FileWriter writer = new FileWriter("pi_benchmark_results.csv")) {
            writer.write("Digits,TimeSeconds\n");

            for (int digits : digitCounts) {
                bench.initialize(digits);
                bench.warmup();

                long start = System.nanoTime();
                bench.run(method);
                long end = System.nanoTime();

                double timeSeconds = (end - start) / 1e9;
                writer.write(digits + "," + timeSeconds + "\n");
            }

            bench.clean();
            System.out.println("Benchmark complete. Results written to pi_benchmark_results.csv");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
