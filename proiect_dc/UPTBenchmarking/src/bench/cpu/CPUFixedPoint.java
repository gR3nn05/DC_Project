package bench.cpu;

import bench.IBenchmark;

public class CPUFixedPoint implements IBenchmark {

    private int size;
    private int[] num = {1, 2, 3, 4};
    private int[] a, b, c;
    private int dummy;
    private final int OPS_PER_ITER = 22;

    @Override
    public void initialize(Object... params) {
        this.size = (Integer) params[0];
        a = new int[size];
        b = new int[size];
        c = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = i % 10;
            b[i] = (i * 3) % size;
        }
    }

    @Override
    public void warmUp() {}

    @Override
    public void run() {
        dummy = 0;
        testIntegerArithmetic();
        testBranching();
        testArrayAccess();
    }

    @Override
    public void run(Object... options) {
        run();
    }

    private void testIntegerArithmetic() {
        int j = 1, k = 2, l = 3;
        for (int i = 0; i < size; ++i) {
            j = num[1] * (k - j) * (l - k);
            k = num[3] * k - (l - j) * k;
            l = (l - k) * (num[2] + j);
            dummy += j + k + l;
        }
    }

    private void testBranching() {
        int j = 0;
        for (int i = 0; i < size; ++i) {
            if (j == 1) j = num[2]; else j = num[3];
            if (j > 2) j = num[0]; else j = num[1];
            if (j < 1) j = num[1]; else j = num[0];
            dummy += j;
        }
    }

    private void testArrayAccess() {
        for (int i = 0; i < size; ++i) {
            int idx = b[i] % size;
            c[i] = a[idx];
        }
    }

    @Override
    public void cancel() {}

    @Override
    public void clean() {}

    @Override
    public String getResult() {
        return String.valueOf(dummy);
    }

    public int getOpsPerIteration() {
        return OPS_PER_ITER;
    }
}
