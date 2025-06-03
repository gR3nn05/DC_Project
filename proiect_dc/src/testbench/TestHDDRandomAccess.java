package testbench;

import bench.hdd.HDDRandomAccess;

public class TestHDDRandomAccess {
    public static void main(String[] args) {
        HDDRandomAccess bench = new HDDRandomAccess();

        // Define buffer sizes in bytes
        int[] bufferSizes = {512, 4 * 1024, 64 * 1024, 1024 * 1024}; // 512B, 4KB, 64KB, 1MB
        long fileSize = 1L * 1024 * 1024 * 1024; // 1GB

        // Table headers
        System.out.printf("%-10s | %-25s | %-25s%n", "Buffer", "Read (IOPS | MB/s)", "Write (IOPS | MB/s)");
        System.out.println("--------------------------------------------------------------------------");

        for (int bufferSize : bufferSizes) {
            bench.initialize(fileSize);

            // --- Random Read - Fixed Time (IOPS)
            bench.run(new Object[]{"r", "ft", bufferSize});
            String readFt = bench.getResult();

            // --- Random Read - Fixed Size (MB/s)
            bench.run(new Object[]{"r", "fs", bufferSize});
            String readFs = bench.getResult();

            // --- Random Write - Fixed Time (IOPS)
            bench.run(new Object[]{"w", "ft", bufferSize});
            String writeFt = bench.getResult();

            // --- Random Write - Fixed Size (MB/s)
            bench.run(new Object[]{"w", "fs", bufferSize});
            String writeFs = bench.getResult();

            // Extract IOPS and MB/s using regex or simple parsing
            String readIOPS = extractIOPS(readFt);
            String readMBps = extractMBps(readFs);

            String writeIOPS = extractIOPS(writeFt);
            String writeMBps = extractMBps(writeFs);

            System.out.printf("%-10s | %-12s | %-10s | %-12s | %-10s%n",
                    bufferSize + "B", readIOPS, readMBps, writeIOPS, writeMBps);

            bench.clean();
        }
    }

    // Extracts number before "I/Os per second"
    private static String extractIOPS(String str) {
        String[] parts = str.split(" ");
        return parts.length > 0 ? parts[0] : "N/A";
    }

    // Extracts last part like "300MB/s"
    private static String extractMBps(String str) {
        if (str.contains("MB/s")) {
            return str.substring(str.lastIndexOf(' ') + 1);
        }
        return "N/A";
    }
}
