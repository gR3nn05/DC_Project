package bench.hdd;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

import timing.Timer;

public class FileWriter {

    private static final int MIN_BUFFER_SIZE = 1024;
    private static final int MAX_BUFFER_SIZE = 64 * 1024 * 1024;
    private static final long MIN_FILE_SIZE = 1024 * 1024;
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 1024;

    private Timer timer = new Timer();
    private Random rand = new Random();
    private double benchScore;

    public void streamWriteFixedFileSize(String prefix, String suffix, int minIndex, int maxIndex,
                                         long fileSize, boolean clean, HDDWriteSpeed benchmark) throws IOException {
        int bufferSize = MIN_BUFFER_SIZE;
        int index = minIndex;
        benchScore = 0;

        while (bufferSize <= MAX_BUFFER_SIZE && index <= maxIndex) {
            if (benchmark.isCancelled()) break;
            String fileName = prefix + index + suffix;
            writeFile(fileName, bufferSize, fileSize, clean);
            bufferSize *= 4;
            index++;
        }

        if (index > minIndex) benchScore /= (index - minIndex);
    }

    public void streamWriteFixedBufferSize(String prefix, String suffix, int minIndex, int maxIndex,
                                           int bufferSize, boolean clean, HDDWriteSpeed benchmark) throws IOException {
        long fileSize = MIN_FILE_SIZE;
        int index = minIndex;
        benchScore = 0;

        while (fileSize <= MAX_FILE_SIZE && index <= maxIndex) {
            if (benchmark.isCancelled()) break;
            String fileName = prefix + index + suffix;
            writeFile(fileName, bufferSize, fileSize, clean);
            fileSize *= 10;
            index++;
        }

        if (index > minIndex) benchScore /= (index - minIndex);
    }

    public void streamWriteFixedFileSizeSingle(String fileName, long fileSize, int bufferSize,
                                               boolean clean, HDDWriteSpeed benchmark) throws IOException {
        benchScore = 0;
        if (!benchmark.isCancelled()) {
            writeFile(fileName, bufferSize, fileSize, clean);
        }
    }

    public void streamWriteFixedBufferSizeSingle(String fileName, long fileSize, int bufferSize,
                                                 boolean clean, HDDWriteSpeed benchmark) throws IOException {
        benchScore = 0;
        if (!benchmark.isCancelled()) {
            writeFile(fileName, bufferSize, fileSize, clean);
        }
    }

    private void writeFile(String fileName, int bufferSize, long fileSize, boolean clean) throws IOException {
        File file = new File(fileName);
        file.getParentFile().mkdirs();

        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file), bufferSize)) {
            byte[] buffer = new byte[bufferSize];
            long chunks = fileSize / bufferSize;

            timer.start();
            for (long i = 0; i < chunks; i++) {
                rand.nextBytes(buffer);
                out.write(buffer);
            }
            out.flush();
        }

        printStats(fileName, fileSize, bufferSize);

        if (clean) file.delete();
    }

    private void printStats(String fileName, long totalBytes, int bufferSize) {
        long time = timer.stop();
        NumberFormat nf = new DecimalFormat("#.00");
        double seconds = time / 1_000_000_000.0;
        double mb = totalBytes / (1024.0 * 1024.0);
        double rate = mb / seconds;

        System.out.println("Wrote " + totalBytes + " bytes to " + fileName + " in "
                + nf.format(seconds) + "s (" + nf.format(rate) + " MB/sec), buffer=" + bufferSize / 1024 + "KB");

        benchScore += rate;
    }

    public double getBenchScore() {
        return benchScore;
    }
}
