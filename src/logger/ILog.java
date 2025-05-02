package logger;

public interface ILog {
    /**
     * Writes the given long value to the log.
     *
     * @param value the long value to write
     */
    void write(long value);

    /**
     * Writes the given string to the log.
     *
     * @param value the string to write
     */
    void write(String value);

    /**
     * Writes all provided values to the log, separated by spaces.
     *
     * @param values the values to write
     */
    void write(Object... values);

    /**
     * Closes the log and releases any resources if necessary.
     */
    void close();

    /**
     * Writes a formatted message followed by a converted time value in the specified time unit.
     *
     * @param message the message to print before the time (e.g., "Execution finished in")
     * @param time the time duration to convert and write, in nanoseconds
     * @param unit the time unit to convert to (e.g., MICRO, MILLI, SEC)
     */
    void writeTime(String msg, long time, TimeUnit unit);
}
