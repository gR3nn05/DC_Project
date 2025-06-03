package testbench;

import bench.hdd.HDDWriteSpeed;

public class TestHDDWriteSpeed {
    public static void main(String[] args) {
        HDDWriteSpeed benchmark = new HDDWriteSpeed();

        // Fixed file size benchmark ('fs')
        long fileSize = 512L * 1024 * 1024; // 512MB
        int[] bufferSizes = {1024, 4096, 16 * 1024, 64 * 1024, 256 * 1024, 1024 * 1024, 4 * 1024 * 1024,
                16 * 1024 * 1024, 64 * 1024 * 1024};

        System.out.println("\n=== Fixed File Size Benchmark ===");
        for (int bufferSize : bufferSizes) {
            String fileName = "C:\\000-bench\\write-fs-" + bufferSize + ".dat";
            benchmark.runFixedFileSizeBenchmark(fileName, fileSize, bufferSize, true);
            System.out.println("Buffer size " + bufferSize / 1024 + "KB: " + benchmark.getResult());
        }

        // Fixed buffer size benchmark ('fb')
        int bufferSize = 2 * 1024; // 2KB
        long[] fileSizes = {1L * 1024 * 1024, 10L * 1024 * 1024, 100L * 1024 * 1024, 1024L * 1024 * 1024};

        System.out.println("\n=== Fixed Buffer Size Benchmark ===");
        for (long size : fileSizes) {
            String fileName = "C:\\000-bench\\write-fb-" + size + ".dat";
            benchmark.runFixedBufferSizeBenchmark(fileName, size, bufferSize, true);
            System.out.println("File size " + size / (1024 * 1024) + "MB: " + benchmark.getResult());
        }
    }
}
