package bench;

public class DummyBenchmark implements IBenchmark {
    @Override
    public String getName() {
        return "DummyBenchmark";
    }

    private int iterations;

    @Override
    public void initialize(Object ...params) {
        if (params != null && params.length > 0 && params[0] instanceof Integer) {
            this.iterations = (Integer) params[0];
        } else {
            this.iterations = 0;
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            // Simulate some work
            double result = Math.sqrt(i);
        }
    }

    @Override
    public void run(Object ...params) {
        run();
    }

    @Override
    public void clean() {
        // Cleanup if necessary
    }

    @Override
    public void cancel() {
        // Cancel logic if necessary
    }
}
