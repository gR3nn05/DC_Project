package bench.cpu;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;
import bench.IBenchmark;

public class CPUDigitsOfPi implements IBenchmark {

    private int digits;
    private MathContext mc;
    private volatile boolean isCancelled = false;

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Integer) {
            this.digits = (Integer) params[0];
        } else {
            this.digits = 1000;
        }
        this.mc = new MathContext(digits + 5, RoundingMode.HALF_UP);
    }


    public void warmup() {
        computePiLeibniz(1000);
    }

    @Override
    public void run() {
        computePiLeibniz(digits);
    }

    @Override
    public void run(Object... options) {
        int method = (options.length > 0 && options[0] instanceof Integer) ? (Integer) options[0] : 1;
        switch (method) {
            case 1:
                computePiLeibniz(digits);
                break;
            case 2:
                computePiMagically(digits);
                break;
            case 3:
                computePiUsingMath(digits);
                break;
            default:
                throw new IllegalArgumentException("Unknown method: " + method);
        }
    }


    @Override
    public void clean() {
        digits = 0;
        mc = null;
        isCancelled = false;
    }

    @Override
    public void cancel() {

    }

    private BigDecimal computePiLeibniz(int terms) {
        BigDecimal pi = BigDecimal.ZERO;
        BigDecimal one = BigDecimal.ONE;
        for (int k = 0; k < terms; k++) {
            if (isCancelled) break;
            BigDecimal numerator = (k % 2 == 0) ? one : one.negate();
            BigDecimal denominator = new BigDecimal(2 * k + 1);
            pi = pi.add(numerator.divide(denominator, mc));
        }
        return pi.multiply(new BigDecimal(4));
    }

    private BigDecimal computePiMagically(int digits) {
        Random r = new Random();
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < digits; i++) {
            if (isCancelled) break;
            sum = sum.add(BigDecimal.valueOf(r.nextDouble()));
        }
        return sum.remainder(BigDecimal.ONE);
    }

    private BigDecimal computePiUsingMath(int digits) {
        if (isCancelled) return BigDecimal.ZERO;
        return arctan(5, digits).multiply(new BigDecimal(4))
                .subtract(arctan(239, digits)).multiply(new BigDecimal(4));
    }

    private BigDecimal arctan(int inverseX, int digits) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal x = BigDecimal.ONE.divide(new BigDecimal(inverseX), mc);
        BigDecimal xPower = x;
        for (int i = 0; i < digits; i++) {
            if (isCancelled) break;
            BigDecimal term = xPower.divide(new BigDecimal(2 * i + 1), mc);
            result = (i % 2 == 0) ? result.add(term) : result.subtract(term);
            xPower = xPower.multiply(x).multiply(x).setScale(mc.getPrecision(), RoundingMode.HALF_UP);
        }
        return result;
    }
}



