package logger;

/**
 * Represents time units and provides conversion from nanoseconds.
 */
public enum TimeUnit {
    NANO,
    MICRO,
    MILLI,
    SEC;

    /**
     * Converts a time value from nanoseconds to the specified time unit.
     *
     * @param timeInNano the time in nanoseconds
     * @param unit the target time unit
     * @return the converted time as a double
     */
    public static double convert(long timeInNano, TimeUnit unit) {
        return switch (unit) {
            case NANO -> timeInNano;
            case MICRO -> timeInNano / 1_000.0;
            case MILLI -> timeInNano / 1_000_000.0;
            case SEC   -> timeInNano / 1_000_000_000.0;
        };
    }
}
