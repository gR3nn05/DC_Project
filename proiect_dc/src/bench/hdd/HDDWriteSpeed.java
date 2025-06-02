package bench.hdd;

import java.io.File;
import java.io.IOException;

import bench.IBenchmark;

public class HDDWriteSpeed implements IBenchmark {

    private volatile boolean cancelled = false;
    private FileWriter lastWriter;

    @Override
    public void initialize(Object... params) {
        // Nothing specific to initialize
    }



    @Override
    public void run() {
        throw new UnsupportedOperationException(
                "Method not implemented. Use run(Object...) instead");
    }

    @Override
    public void run(Object... options) {
        if (options == null || options.length < 2) {
            throw new IllegalArgumentException("Expected two parameters: option (\"fs\" or \"fb\"), clean (Boolean)");
        }

        String option = (String) options[0];   // "fs" or "fb"
        Boolean clean = (Boolean) options[1];  // true or false

        // Use consistent prefix for test files
        String prefix = "C:\\000-bench\\write-";
        String suffix = ".dat";
        int minIndex = 0;
        int maxIndex = 8;

        // Default sizes
        long fileSize = 512L * 1024 * 1024;   // 512 MB
        int bufferSize = 4 * 1024;             // 4 KB

        cancelled = false;  // reset cancel flag before run
        lastWriter = new FileWriter();

        try {
            if ("fs".equals(option)) {
                lastWriter.streamWriteFixedFileSize(prefix, suffix, minIndex, maxIndex, fileSize, clean, this);
            } else if ("fb".equals(option)) {
                lastWriter.streamWriteFixedBufferSize(prefix, suffix, minIndex, maxIndex, bufferSize, clean, this);
            } else {
                throw new IllegalArgumentException("Invalid option: " + option);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clean() {
        File directory = new File("C:/000-bench");
        if (!directory.exists()) {
            System.out.println("No directory to clean: " + directory.getAbsolutePath());
            return;
        }

        File[] files = directory.listFiles((dir, name) ->
                name.startsWith("write-") && name.endsWith(".dat"));

        if (files != null) {
            for (File file : files) {
                if (file.delete()) {
                    System.out.println("Deleted file: " + file.getName());
                } else {
                    System.out.println("Failed to delete file: " + file.getName());
                }
            }
        }

        // Try deleting directory if empty
        if (directory.isDirectory() && directory.list().length == 0) {
            if (directory.delete()) {
                System.out.println("Deleted empty directory: " + directory.getAbsolutePath());
            }
        }
    }


    public String getResult() {
        if (lastWriter != null) {
            return String.format("%.2f MB/sec", lastWriter.getBenchScore());
        }
        return "No result available.";
    }

    @Override
    public void cancel() {
        cancelled = true;
        System.out.println("Benchmark cancel requested.");
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
