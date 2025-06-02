package bench.cpu;

import bench.IBenchmark;

public class CPUFixedPoint implements IBenchmark {

    private int[] num = {0, 1, 2, 3};
    private int[] res;
    private int size;
    private int j, k, l;

    @Override
    public void initialize(Object... params) {
        this.size = (Integer) params[0];
        this.res = new int[size];
        // Initialize with safe values
        j = 1; k = 2; l = 3;  // Changed from 0,1,2 to prevent negative indices
    }

    @Override
    public void warmUp() {
        // Reduced warm-up size and added bounds checking
        arithmeticTest(100);
        branchingTest(100);
        arrayTest(100);
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Use run(Object...) instead");
    }

    @Override
    public void run(Object... options) {
        arithmeticTest(size);
        branchingTest(size);
        arrayTest(size);
    }

    private void arithmeticTest(int workload) {
        for (int i = 0; i < workload; i++) {
            j = num[1] * (k - j) * (1 - k);
            k = num[3] * k - (1 - j) * k;
            l = (1 - k) * (num[2] + j) % Integer.MAX_VALUE;

            // Safe array access
            int index1 = Math.abs(l - 2) % res.length;
            int index2 = Math.abs(k - 2) % res.length;

            res[index1] = j + k + l;
            res[index2] = j * k * l;
        }
    }

    private void branchingTest(int workload) {
        for (int i = 0; i < workload; i++) {
            if (j == 1) {
                j = num[2];
            } else {
                j = num[3];
            }
            if (j > 2) {
                j = num[0];
            } else {
                j = num[1];
            }
            if (j < 1) {
                j = num[1];
            } else {
                j = num[0];
            }
        }
    }

    private void arrayTest(int workload) {
        int[] a = new int[workload];
        int[] b = new int[workload];
        int[] c = new int[workload];

        // Initialize arrays with safe indices
        for (int i = 0; i < workload; i++) {
            a[i] = i % num.length;
            b[i] = (i + 1) % num.length;
        }

        for (int i = 0; i < workload; i++) {
            int safeIndex = b[i] % a.length;
            c[i] = a[safeIndex];

            // Safe swap
            int temp = a[i % a.length];
            a[i % a.length] = b[i % b.length];
            b[i % b.length] = temp;
        }
    }

    @Override
    public void cancel() {
        // No special cancellation needed
    }

    @Override
    public void clean() {
        res = null;
    }

    @Override
    public String getResult() {
        return String.format("Arithmetic: j=%d, k=%d, l=%d | Branching: j=%d | Array: c[0]=%d",
                j, k, l, j, (res != null && res.length > 0) ? res[0] : -1);
    }
}