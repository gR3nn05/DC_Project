package logging;

public class ConsoleLogger implements ILogger {
    @Override
    public void write(long value) {
        System.out.println(value);
    }

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public void write(Object... values) {
        for (Object val : values) {
            System.out.print(val + " ");
        }
        System.out.println();
    }

    @Override
    public void writeTime(String message, long time, TimeUnit unit) {
        System.out.println(message + " " + unit.convertFromNano(time) + " " + unit.name());
    }

    @Override
    public void close() {}
}