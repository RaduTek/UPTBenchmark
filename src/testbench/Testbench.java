package testbench;

import bench.DummyBenchmark;
import logger.ConsoleLogger;
import timer.Timer;

public class Testbench {
    public static void main(String[] args) {
        var timer = new Timer();
        var log = new ConsoleLogger();

        var bench = new DummyBenchmark();

        bench.initialize(10000);
        timer.start();
        bench.run();

        log.write("Benchmark finished in:", timer.stop(), "ns");

        log.close();
    }
}
