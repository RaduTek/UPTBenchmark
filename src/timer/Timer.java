package timer;

public class Timer implements ITimer {
    private long startTime;
    private long totalElapsedTime;
    private boolean running;

    @Override
    public void start() {
        if (!running) {
            startTime = System.nanoTime();
            running = true;
            totalElapsedTime = 0;
        }
    }

    @Override
    public long stop() {
        if (running) {
            totalElapsedTime += System.nanoTime() - startTime;
            running = false;
        }
        return totalElapsedTime;
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
            totalElapsedTime += elapsed;
            running = false;
            return elapsed;
        }
        return 0;
    }
}
