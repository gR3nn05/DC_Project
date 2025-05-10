package timing;

public class Timer implements ITimer {
    private long startTime;
    private long totalTime;
    private boolean running;

    @Override
    public void start() {
        startTime = System.nanoTime();
        totalTime = 0;
        running = true;
    }

    @Override
    public long stop() {
        if (running) {
            totalTime += System.nanoTime() - startTime;
            running = false;
        }
        return totalTime;
    }

    @Override
    public void resume() {
        if (!running) {
            startTime = System.nanoTime();
            running = true;
        }
    }

    @Override
    public long pause() {
        if (running) {
            long elapsed = System.nanoTime() - startTime;
            totalTime += elapsed;
            running = false;
            return elapsed;
        }
        return 0;
    }
}