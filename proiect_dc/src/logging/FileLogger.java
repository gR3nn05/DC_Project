package logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileLogger implements ILogger {
    private BufferedWriter writer;

    public FileLogger(String filePath) {
        try {
            writer = new BufferedWriter(new FileWriter(filePath));
        } catch (IOException e) {
            System.err.println("Error opening file for writing: " + e.getMessage());
        }
    }

    @Override
    public void write(long value) {
        try {
            writer.write(Long.toString(value));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing long value: " + e.getMessage());
        }
    }

    @Override
    public void write(String message) {
        try {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing string: " + e.getMessage());
        }
    }

    @Override
    public void write(Object... values) {
        try {
            for (Object val : values) {
                writer.write(val.toString() + " ");
            }
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing multiple values: " + e.getMessage());
        }
    }

    @Override
    public void writeTime(String message, long time, TimeUnit unit) {
        try {
            writer.write(message + " " + unit.convertFromNano(time) + " " + unit.name());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing time: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing file: " + e.getMessage());
        }
    }
}