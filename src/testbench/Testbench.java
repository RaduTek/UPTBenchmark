package testbench;

import bench.DemoBenchmark;
import bench.RecursivePrimeBenchmark;
import bench.hdd.HDDWriteSpeed;
import bench.ram.VirtualMemoryBenchmark;
import logger.ConsoleLogger;
import logger.TimeUnit;
import timer.Timer;

public class Testbench {
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

    private static void recursivePrime(String[] args) {
        int unrollFactor = 1;
        if (args.length >= 2) {
            try {
                unrollFactor = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid unroll factor, using default (1).");
                unrollFactor = 1;
            }
        }
        RecursivePrimeBenchmark bench = new RecursivePrimeBenchmark();
        bench.initialize(unrollFactor);

        System.out.println("Warming up...");
        bench.warmUp();

        System.out.println("Running RecursivePrimeBenchmark with unroll factor " + unrollFactor + "...");
        bench.run();

        System.out.println(bench.getResult());
        bench.clean();
    }

    private static void virtualMemory(String[] args) {
        long fileSize = 2L * 1024 * 1024 * 1024; // default 2 GB
        int bufferSize = 4096; // default 4 KB

        if (args.length >= 2) {
            try {
                fileSize = Long.parseLong(args[1]) * 1024 * 1024; // convert MB to bytes
            } catch (NumberFormatException e) {
                System.out.println("Invalid file size, using default 2 GB.");
            }
            try {
                bufferSize = Integer.parseInt(args[2]); // buffer size in bytes
            } catch (NumberFormatException e) {
                System.out.println("Invalid buffer size, using default 4 KB.");
            }
        }

        System.out.println("Running VirtualMemoryBenchmark with file size " + (fileSize / (1024 * 1024)) + " MB and buffer size " + (bufferSize / 1024) + " KB...");
        var vmb = new VirtualMemoryBenchmark();

        vmb.run(fileSize, bufferSize);

        System.out.println(vmb.getResult());
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify a benchmark!\n" +
                    "Available benchmarks: cpufloat, hddwrite, hddrandom, recursiveprime, virtualmem, demo");
            return;
        }
        String benchmark = args[0].toLowerCase();

        switch (benchmark) {
            case "demo":
                demo(args);
                break;
            case "cpufloat":
                TestCPUFixedVsFloatingPoint.main(args);
                break;
            case "hddwrite":
                HDDBench.hddwrite(args);
                break;
            case "hddrandom":
                HDDBench.hddrandom(args);
                break;
            case "recursiveprime":
                recursivePrime(args);
                break;
        }
    }
}
