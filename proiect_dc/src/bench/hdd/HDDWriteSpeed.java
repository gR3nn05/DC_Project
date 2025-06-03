package bench.hdd;

import java.io.File;
import java.io.IOException;

import bench.IBenchmark;

public class HDDWriteSpeed implements IBenchmark {

    private volatile boolean cancelled = false;
    private FileWriter lastWriter;

    @Override
    public void initialize(Object... params) {
        // No initialization needed
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Use run(Object...) instead");
    }

    @Override
    public void run(Object... options) {
        if (options == null || options.length < 2) {
            throw new IllegalArgumentException("Expected two parameters: option (\"fs\" or \"fb\"), clean (Boolean)");
        }

        String option = (String) options[0];
        boolean clean = (Boolean) options[1];

        String prefix = "C:\\000-bench\\write-";
        String suffix = ".dat";
        int minIndex = 0;
        int maxIndex = 8;

        long fileSize = 512L * 1024 * 1024;  // 512 MB
        int bufferSize = 4 * 1024;           // 4 KB

        cancelled = false;
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

    public void runFixedFileSizeBenchmark(String fileName, long fileSize, int bufferSize, boolean clean) {
        cancelled = false;
        lastWriter = new FileWriter();
        try {
            lastWriter.streamWriteFixedFileSizeSingle(fileName, fileSize, bufferSize, clean, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runFixedBufferSizeBenchmark(String fileName, long fileSize, int bufferSize, boolean clean) {
        cancelled = false;
        lastWriter = new FileWriter();
        try {
            lastWriter.streamWriteFixedBufferSizeSingle(fileName, fileSize, bufferSize, clean, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clean() {
        File directory = new File("C:/000-bench");
        if (!directory.exists()) return;

        File[] files = directory.listFiles((dir, name) -> name.startsWith("write-") && name.endsWith(".dat"));
        if (files != null) {
            for (File file : files) file.delete();
        }

        if (directory.list().length == 0) directory.delete();
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
