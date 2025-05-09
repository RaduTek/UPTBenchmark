package testbench;

import bench.DemoBenchmark;
import bench.hdd.HDDWriteSpeed;
import logger.ConsoleLogger;
import logger.TimeUnit;
import timer.Timer;

public class Testbench {
    private static void hddwrite(String[] args) {
        // Implementation for HDD write speed benchmark
        if (args.length < 2) {
            System.out.println("Please specify the path for the HDD write speed benchmark, and optionally the benchmark type (fs, fb)");
            return;
        }

        System.out.println("Running HDD write speed benchmark...");

        String targetPath = args[1];
        HDDWriteSpeed hwd = new HDDWriteSpeed(targetPath);
        hwd.warmUp();

        if (args.length >= 3) {
            // run only specified benchmark type
            hwd.run(args[2], true);
        } else {
            // run both
            hwd.run("fs", true); // fixed size, clean up after
            hwd.run("fb", true); // fixed buffer, clean up after
        }

        hwd.clean();
    }

    private static void demo(String[] args) {
        Timer timer = new Timer();
        ConsoleLogger log = new ConsoleLogger();
        DemoBenchmark bench = new DemoBenchmark();

        int opsPerIter = bench.getOpsPerIter();
        int workload = bench.getWorkload();

        System.out.println("Initializing benchmark...");
        bench.initialize(1_000_000);
        System.out.println("Starting timer...");
        timer.start();

        for (int i = 0; i < 5; i++) {
            System.out.println("Running benchmark iteration " + i + "...");
            bench.run();

            long elapsed = timer.pause();
            System.out.println("Paused timer after run " + i + ", elapsed: " + elapsed + " ns");
            log.write("Run " + i + " paused at: " + elapsed + " ns");

            try {
                Thread.sleep(100); // simulate wait
            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
            }

            System.out.println("Resuming timer after pause...");
            timer.resume();
        }

        System.out.println("Stopping timer and calculating total time...");
        long total = timer.stop();

        log.writeTime("Total benchmark time", total, TimeUnit.MILLI);

        HDDWriteSpeed hwd = new HDDWriteSpeed("~");

        hwd.run("fs", true); // fixed size, clean up after
        hwd.run("fb", true); // fixed buffer, clean up after

        System.out.println("Closing logger...");
        log.close();

        System.out.println("Done.");
        double seconds = total / 1e9;
        double mops = opsPerIter * workload / (seconds * 1e6);
        log.write("MOPS:" + mops);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify a benchmark! hddwrite, hddrandom, demo, cpu, memory");
            return;
        }
        String benchmark = args[0].toLowerCase();

        switch (benchmark) {
            case "demo":
                demo(args);
                break;
            case "hddwrite":
                hddwrite(args);
                break;
        }
    }
}
