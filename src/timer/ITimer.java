package timer;

public interface ITimer {
    /**
     * Starts the timer by saving the current time and resetting the total elapsed time.
     */
    void start();

    /**
     * Stops the timer and returns the total elapsed time since start was called.
     *
     * @return the total elapsed time in nanoseconds
     */
    long stop();

    /**
     * Resumes the timer by saving the current time without resetting the total elapsed time.
     */
    void resume();

    /**
     * Pauses the timer and returns the elapsed time since the last start or resume.
     *
     * @return the elapsed time in nanoseconds since the last start or resume
     */
    long pause();
}
