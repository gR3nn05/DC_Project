package testbench;

import bench.hdd.HDDRandomAccess;

public class TestHDDRandomAccess {
    public static void main(String[] args) {
        HDDRandomAccess bench = new HDDRandomAccess();

        // Create a 100MB test file for random access
        long fileSize = 100L * 1024 * 1024; // 100 MB
        int bufferSize = 4 * 1024; // 4 KB

        System.out.println("Initializing test file...");
        bench.initialize(fileSize);

        // -------------------------------
        // Random Read - Fixed Size
        System.out.println("Running: Read - Fixed Size");
        bench.run(new Object[]{"r", "fs", bufferSize});
        System.out.println(bench.getResult());

        // -------------------------------
        // Random Read - Fixed Time
        System.out.println("Running: Read - Fixed Time");
        bench.run(new Object[]{"r", "ft", bufferSize});
        System.out.println(bench.getResult());

        // -------------------------------
        // Random Write - Fixed Size
        System.out.println("Running: Write - Fixed Size");
        bench.run(new Object[]{"w", "fs", bufferSize});
        System.out.println(bench.getResult());

        // -------------------------------
        // Random Write - Fixed Time
        System.out.println("Running: Write - Fixed Time");
        bench.run(new Object[]{"w", "ft", bufferSize});
        System.out.println(bench.getResult());

        // -------------------------------
        // Cleanup
        bench.clean();
        System.out.println("Temporary test file deleted.");
    }
}
