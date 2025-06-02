package bench.hdd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import timing.Timer;

public class FileWriter {

    private static final int MIN_BUFFER_SIZE = 1024 * 1;         // 1 KB
    private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 32; // 32 MB
    private static final long MIN_FILE_SIZE = 1024 * 1024 * 1;   // 1 MB
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 512; // 512 MB

    private Timer timer = new Timer();
    private Random rand = new Random();
    private double benchScore;

    public void streamWriteFixedFileSize(String filePrefix, String fileSuffix,
                                         int minIndex, int maxIndex, long fileSize, boolean clean,
                                         HDDWriteSpeed benchmark)
            throws IOException {

        System.out.println("Stream write benchmark with fixed file size");
        int currentBufferSize = MIN_BUFFER_SIZE;
        int fileIndex = minIndex;
        benchScore = 0;

        while (currentBufferSize <= MAX_BUFFER_SIZE && fileIndex <= maxIndex) {
            if (benchmark.isCancelled()) {
                System.out.println("Benchmark cancelled. Stopping.");
                break;
            }

            String fileName = filePrefix + fileIndex + fileSuffix;
            writeFile(fileName, currentBufferSize, fileSize, clean);

            currentBufferSize *= 2;
            fileIndex++;
        }

        int filesWritten = fileIndex - minIndex;
        if (filesWritten > 0) {
            benchScore /= filesWritten;
        }

        String partition = extractPartitionName(filePrefix);
        System.out.println("File write score on partition " + partition + ": "
                + String.format("%.2f", benchScore) + " MB/sec");
    }

    public void streamWriteFixedBufferSize(String filePrefix, String fileSuffix,
                                           int minIndex, int maxIndex, int bufferSize, boolean clean,
                                           HDDWriteSpeed benchmark)
            throws IOException {

        System.out.println("Stream write benchmark with fixed buffer size");
        long currentFileSize = MIN_FILE_SIZE;
        int fileIndex = minIndex;
        benchScore = 0;

        while (currentFileSize <= MAX_FILE_SIZE && fileIndex <= maxIndex) {
            if (benchmark.isCancelled()) {
                System.out.println("Benchmark cancelled. Stopping.");
                break;
            }

            String fileName = filePrefix + fileIndex + fileSuffix;
            writeFile(fileName, bufferSize, currentFileSize, clean);

            currentFileSize *= 2;
            fileIndex++;
        }

        int filesWritten = fileIndex - minIndex;
        if (filesWritten > 0) {
            benchScore /= filesWritten;
        }

        String partition = extractPartitionName(filePrefix);
        System.out.println("File write score on partition " + partition + ": "
                + String.format("%.2f", benchScore) + " MB/sec");
    }

    private void writeFile(String fileName, int bufferSize,
                           long fileSize, boolean clean) throws IOException {

        File file = new File(fileName);
        File parent = file.getParentFile();
        if (parent != null) {
            parent.mkdirs();  // ensure directory structure exists
        }

        try (BufferedOutputStream outputStream =
                     new BufferedOutputStream(new FileOutputStream(file), bufferSize)) {

            byte[] buffer = new byte[bufferSize];
            long toWrite = fileSize / bufferSize;

            timer.start();
            for (long i = 0; i < toWrite; i++) {
                rand.nextBytes(buffer);
                outputStream.write(buffer);
            }
            outputStream.flush();
        }

        printStats(fileName, fileSize, bufferSize);

        if (clean) {
            if (file.delete()) {
                System.out.println("Deleted file immediately: " + fileName);
            } else {
                System.out.println("Failed to delete immediately, scheduling delete on exit: " + fileName);
                file.deleteOnExit();
            }
        }
    }

    private void printStats(String fileName, long totalBytes, int bufferSize) {
        long time = timer.stop();

        NumberFormat nf = new DecimalFormat("#.00");
        double seconds = time / 1_000_000_000.0;
        double megabytes = totalBytes / (1024.0 * 1024.0);
        double rate = megabytes / seconds;

        System.out.println("Done writing " + totalBytes + " bytes to file: "
                + fileName + " in " + nf.format(seconds) + " s ("
                + nf.format(rate) + " MB/sec) with buffer size of "
                + bufferSize / 1024 + " KB");

        benchScore += rate;
    }

    public double getBenchScore() {
        return benchScore;
    }

    private String extractPartitionName(String prefix) {
        if (prefix.contains(":\\")) {
            return prefix.substring(0, prefix.indexOf(":\\") + 1); // e.g. "D:"
        } else {
            return "/";
        }
    }
}
