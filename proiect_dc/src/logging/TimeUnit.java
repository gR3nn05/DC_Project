
package logging;


public enum TimeUnit {
    Nano(1),
    Micro(1_000),
    Milli(1_000_000),
    Sec(1_000_000_000);

    private final long multiplier;

    TimeUnit(long multiplier) {
        this.multiplier = multiplier;
    }

    public long convertFromNano(long nanos) {
        return nanos / (multiplier / 1);
    }
}
