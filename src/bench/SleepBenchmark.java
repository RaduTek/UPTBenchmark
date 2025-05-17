package bench;

public class SleepBenchmark implements IBenchmark {
    private long sleepTime;
    private boolean running;

    @Override
    public String getName() {
        return "SleepBenchmark";
    }

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Number) {
            sleepTime = ((Number) params[0]).longValue();
        } else {
            sleepTime = 1000; // Default to 1 second
        }
        running = true;
    }

    @Override
    public void run() {
        if (!running) return;

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            // Optionally log interruption
        }
    }

    @Override
    public void run(Object... params) {
        run();
    }

    @Override
    public void clean() {
        // Nothing to clean
    }

    @Override
    public void cancel() {
        running = false;
    }

    @Override
    public void warmup() {
        // No warmup needed for sleep benchmark
    }
}
