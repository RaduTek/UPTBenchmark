package testbench;

import benchmark.cpu.CPUDigitsOfPi;
import timer.Timer;

public class TestCPUDigitsOfPi {
    public static void main(String[] args) {
        CPUDigitsOfPi bench = new CPUDigitsOfPi();
        Timer timer = new Timer();

        // Print CSV header for spreadsheet
        System.out.println("digits,ms");

        // Test for n = 50, 100, 500, 1000, 5000, 10000, ..., 100000
        int[] testDigits = {50, 100, 500, 1000, 5000, 10000, 20000, 50000, 100000};

        for (int digits : testDigits) {
            bench.initialize(digits);
            bench.warmup();

            timer.start();
            bench.run();
            long elapsed = timer.stop();

            // Output: digits, runtime in milliseconds
            System.out.println(digits + "," + (elapsed / 1_000_000.0));
        }
    }
}