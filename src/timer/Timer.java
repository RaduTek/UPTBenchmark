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
            totalElapsedTime = 0;  // Reset only on fresh start
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

    // Added this helper method to get elapsed time so far (without stopping)
    public long getElapsedTime() {
        if (running) {
            return totalElapsedTime + (System.nanoTime() - startTime);
        } else {
            return totalElapsedTime;
        }
    }

    //method to check if timer is running
    public boolean isRunning() {
        return running;
    }
}
