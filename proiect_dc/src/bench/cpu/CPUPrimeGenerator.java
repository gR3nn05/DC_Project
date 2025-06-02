package bench.cpu;

import bench.IBenchmark;
import java.util.ArrayList;
import java.util.List;

public class CPUPrimeGenerator implements IBenchmark {
    private List<Integer> primes = new ArrayList<>();
    private int current = 2;
    private int lastPrime = 0;
    private boolean overflow = false;

    @Override
    public void initialize(Object... params) {
        primes.clear();
        current = 2;
        overflow = false;
    }

    @Override
    public void warmUp() {
        // Safe warm-up without deep recursion
        try {
            for (int i = 0; i < 1000; i++) {
                if (isPrime(i)) {
                    primes.add(i);
                    lastPrime = i;
                }
            }
        } catch (StackOverflowError ignored) {}
    }

    private void findPrimesRecursive(int n) {
        try {
            if (isPrime(n)) {
                primes.add(n);
                lastPrime = n;
            }
            findPrimesRecursive(n + 1);
        } catch (StackOverflowError e) {
            overflow = true;
        }
    }

    private boolean isPrime(int num) {
        if (num <= 1) return false;
        for (int p : primes) {
            if (p * p > num) break;
            if (num % p == 0) return false;
        }
        return true;
    }

    @Override
    public void run() {
        findPrimesRecursive(current);
    }

    @Override
    public void run(Object... options) {
        run();
    }

    @Override
    public String getResult() {
        return String.format("Last prime found: %d", lastPrime);
    }

    public int getLastPrime() {
        return lastPrime;
    }

    public boolean hitOverflow() {
        return overflow;
    }

    @Override
    public void clean() {
        primes = null;
    }

    @Override
    public void cancel() {
        // Not implemented for this benchmark
    }
}