package bench;

import java.util.Random;

public class DemoBenchmark implements IBenchmark {
    private int[] array;
    private boolean running;

    @Override
    public void initialize(Object... params) {
        if (params.length < 1 || !(params[0] instanceof Integer))
            throw new IllegalArgumentException("Array size must be provided as an Integer.");

        int size = (Integer) params[0];
        array = new int[size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size);
        }

        running = true;
    }

    @Override
    public void run() {
        if (array == null)
            throw new IllegalStateException("Benchmark not initialized.");

        int n = array.length;
        for (int i = 0; i < n - 1 && running; i++) {
            for (int j = 0; j < n - i - 1 && running; j++) {
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    @Override
    public void run(Object... params) {
        run();
    }

    @Override
    public void clean() {
        array = null;
    }

    @Override
    public void cancel() {
        running = false;
    }
}