package testbench;

import bench.hdd.HDDWriteSpeed;

public class TestHDDWriteSpeed {

    public static void main(String[] args) {
        HDDWriteSpeed bench = new HDDWriteSpeed();

        System.out.println("Running fixed file size benchmark, clean = true");
        bench.run("fs", true);
        System.out.println("Result: " + bench.getResult());

        System.out.println("\nRunning fixed buffer size benchmark, clean = false");
        bench.run("fb", false);
        System.out.println("Result: " + bench.getResult());

        System.out.println("\nCleaning up generated files...");
        bench.clean();
    }
}
