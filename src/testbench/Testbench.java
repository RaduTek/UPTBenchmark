package testbench;

import bench.DemoBenchmark;
import logger.ConsoleLogger;
import logger.TimeUnit;
import timer.Timer;

public class Testbench {
    public static void main(String[] args) throws InterruptedException {
        Timer timer = new Timer();
        ConsoleLogger log = new ConsoleLogger();

        System.out.println("Initializing benchmark...");
        DemoBenchmark bench = new DemoBenchmark();
        bench.initialize(1_000_000);

        System.out.println("Starting timer...");
        timer.start();

        for (int i = 0; i < 5; i++) {
            System.out.println("Running benchmark iteration " + i + "...");
            bench.run();

            long elapsed = timer.pause();
            System.out.println("Paused timer after run " + i + ", elapsed: " + elapsed + " ns");
            log.write("Run " + i + " paused at:", elapsed, "ns");

            Thread.sleep(100); // simulate wait

            System.out.println("Resuming timer after pause...");
            timer.resume();
        }

        System.out.println("Stopping timer and calculating total time...");
        long total = timer.stop();

        log.writeTime("Total benchmark time", total, TimeUnit.MILLI);

        System.out.println("Closing logger...");
        log.close();

        System.out.println("Done.");
    }
}
