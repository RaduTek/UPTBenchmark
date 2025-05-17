package bench;

import java.util.Random;

public class DemoBenchmark implements IBenchmark {
    private int[] array;
    private boolean running;

    //@Override
    public String getName() {
        return "DemoBenchmark";
    }

    @Override
    public void initialize(Object... params) {
        if (params.length > 0 && params[0] instanceof Integer) {
            int size = (Integer) params[0];
            array = new int[size];
            Random rand = new Random();

            for (int i = 0; i < size; i++) {
                array[i] = rand.nextInt();
            }

            running = true;
        }
    }

    @Override
    public void run() {
        System.out.println("DemoBenchmark: run() started");
        if (array == null) return;

        for (int i = 1; i < array.length && running; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key && running) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;

            // Print progress every 10% (optional)
            if (i % (array.length / 10) == 0) {
                System.out.println("Progress: " + (i * 100 / array.length) + "%");
            }
        }
        System.out.println("DemoBenchmark: run() finished"); 
    }


    @Override
    public void run(Object... params) {
        run();
    }

    @Override
    public void clean() {
        array = null;
    }

    @Override
    public void cancel() {
        running = false;
    }

    @Override
    public void warmup() {
        // Run a short version of the benchmark to trigger JVM optimizations
        if (array == null) return;
        for (int i = 1; i < Math.min(array.length, 1000) && running; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key && running) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }
}
