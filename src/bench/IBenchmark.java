package bench;

public interface IBenchmark {

    /**
     * Runs the benchmark.
     */
    void run();


    /**
     * Runs the benchmark with additional parameters.
     *
     * @param params additional parameters for the benchmark
     */
    void run(Object ...params);

    /**
     * Initializes the benchmark with the given parameters.
     *
     * @param params parameters to initialize the benchmark
     */
    void initialize(Object ...params);

    /**
     * Cleans up resources used by the benchmark.
     */
    void clean();

    /**
     * Cancels the benchmark if it is running.
     */
    void cancel();

    /**
     * Returns the name of the benchmark.
     *
     * @return the name of the benchmark
     */
    String getName();
}
