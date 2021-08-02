package today.Miscible.utils.timeUtils;


public class TimeHelper {
    public long lastMs;
    private long prevMS = 0L;

    public TimeHelper() {
        this.lastMs = 0L;
    }

    public boolean isDelayComplete(long delay) {
        if (System.currentTimeMillis() - this.lastMs > delay) {
            return true;
        }
        return false;
    }

    public long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public void setLastMs(int i) {
        this.lastMs = System.currentTimeMillis() + (long) i;
    }

    public boolean hasReached(double d) {
        return this.getCurrentMS() - this.lastMs >= d;
    }

    public boolean hasReached(float timeLeft) {
        return (float) (this.getCurrentMS() - this.lastMs) >= timeLeft;
    }

    public long getTime() {
        return System.nanoTime() / 1000000L;
    }

    public static double getIncremental(double val, double inc) {
        double one = 1.0D / inc;
        return (double) Math.round(val * one) / one;
    }

    public boolean delay(double nextDelay) {
        return System.currentTimeMillis() - lastMs >= nextDelay;
    }
}

