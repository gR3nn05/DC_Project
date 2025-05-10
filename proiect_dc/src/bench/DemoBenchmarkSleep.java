package bench;

public class DemoBenchmarkSleep implements IBenchmark {
    private long sleepTimeMs;
    private boolean running;

    @Override
    public void initialize(Object... params) {
        if (params.length < 1 || !(params[0] instanceof Integer))
            throw new IllegalArgumentException("Sleep time (ms) must be provided.");
        sleepTimeMs = (Integer) params[0];
        running = true;
    }

    @Override
    public void run() {
        try {
            if (running) Thread.sleep(sleepTimeMs);
        } catch (InterruptedException e) {
            System.err.println("Benchmark sleep interrupted!");
        }
    }

    @Override
    public void run(Object... params) {
        run();
    }

    @Override
    public void clean() {}
    @Override
    public void cancel() {
        running = false;
    }
}