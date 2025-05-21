package bench;

import java.util.ArrayList;

public class RecursivePrimeBenchmark implements IBenchmark {
    // Benchmark parameters and state
    private int unrollFactor = 1; // L - number of primes found per recursion step
    private long lastPrime = 2;
    private long primeCount = 0;
    private long runtime = 0;
    private double score = 0;
    private boolean cancelled = false;

    // For result reporting
    private String resultString = "";

    @Override
    public void initialize(Object... params) {
        // Accept an optional unroll factor parameter
        if (params != null && params.length > 0 && params[0] instanceof Integer) {
            unrollFactor = (Integer) params[0];
            if (unrollFactor < 1) unrollFactor = 1;
        } else {
            unrollFactor = 1;
        }
        lastPrime = 2;
        primeCount = 0;
        runtime = 0;
        score = 0;
        cancelled = false;
        resultString = "";
    }

    @Override
    public void run(Object... params) {
        // Allow run-time override of unroll factor
        if (params != null && params.length > 0 && params[0] instanceof Integer) {
            unrollFactor = (Integer) params[0];
            if (unrollFactor < 1) unrollFactor = 1;
        }
        run();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        cancelled = false;
        try {
            // Start recursive prime search from 2
            findPrimesRecursive(2, unrollFactor);
        } catch (StackOverflowError soe) {
            // Stack overflow occurred: print stats and compute score
            runtime = System.currentTimeMillis() - startTime;
            score = computeScore(runtime);
            resultString = getFormattedResult("StackOverflowError detected!");
        } catch (Exception e) {
            runtime = System.currentTimeMillis() - startTime;
            resultString = "Exception: " + e.getMessage();
        }
    }

    @Override
    public void clean() {
        // Reset all state
        lastPrime = 2;
        primeCount = 0;
        runtime = 0;
        score = 0;
        cancelled = false;
        resultString = "";
    }

    @Override
    public void cancel() {
        // Set cancelled flag; recursion checks this before each call
        cancelled = true;
    }

    @Override
    public String getName() {
        return "RecursivePrimeBenchmark";
    }

    @Override
    public void warmUp() {
        // Simple warm-up: run a small, shallow recursion to trigger JIT optimizations, etc.
        try {
            findPrimesWarmUp(2, 3); // 3 primes at each step, shallow stack
        } catch (StackOverflowError ignored) {
        }
    }

    @Override
    public String getResult() {
        if (resultString == null || resultString.isEmpty()) {
            // Not run yet, or no stack overflow occurred
            return getFormattedResult("Result not available. Did you run()?");
        }
        return resultString;
    }

    // ---- Benchmark logic ----

    /**
     * Recursive method to find L primes per recursion step, starting from 'current'.
     * Fills up the stack until StackOverflowError occurs or cancelled.
     */
    private void findPrimesRecursive(long current, int L) {
        if (cancelled) return;

        ArrayList<Long> foundPrimes = new ArrayList<>(L);
        long next = current + 1;
        int found = 0;

        // Loop unrolling: find L primes in this stack frame
        while (found < L) {
            if (isPrime(next)) {
                lastPrime = next;
                foundPrimes.add(next);
                found++;
                primeCount++;
            }
            next++;
        }

        // Recurse with the next candidate
        findPrimesRecursive(next - 1, L);
    }

    /**
     * Warm-up recursive method for JIT, with limited stack depth.
     */
    private void findPrimesWarmUp(long current, int L) {
        if (L <= 0) return;
        int found = 0;
        long next = current + 1;
        while (found < L) {
            if (isPrime(next)) found++;
            next++;
        }
        // Only recurse a small number of times
        if (L > 1)
            findPrimesWarmUp(next - 1, L - 1);
    }

    /**
     * Primality check (simple trial division, suitable for benchmarking).
     */
    private boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i * i <= n; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Composite benchmark score function.
     * You can adjust the weights/formula as needed.
     * Here: score = (primesFound * unrollFactor) / runtimeSeconds
     */
    private double computeScore(long runtimeMillis) {
        double runtimeSec = runtimeMillis / 1000.0;
        if (runtimeSec == 0) runtimeSec = 0.001; // Avoid division by zero
        return (primeCount * unrollFactor) / runtimeSec;
    }

    /**
     * Format the result as a string for reporting.
     */
    private String getFormattedResult(String status) {
        return String.format(
                "=== %s ===\n" +
                        "Last prime found: %d\n" +
                        "Total primes found: %d\n" +
                        "Elapsed time (ms): %d\n" +
                        "Unroll factor (L): %d\n" +
                        "Composite score: %.2f (formula: (primes * unroll) / seconds)\n",
                status, lastPrime, primeCount, runtime, unrollFactor, score
        );
    }
}