package testbench;

import bench.hdd.HDDWriteSpeed;
import bench.hdd.HDDRandomAccess;

class HDDBench {
    protected static void hddwrite(String[] args) {
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

    protected static void hddrandom(String[] args) {
        // Implementation for HDD random access benchmark
        if (args.length < 2) {
            System.out.println("""
                    Please specify the path for the HDD write speed benchmark, and optionally:\s
                     - the benchmark type (fs, ft)
                     - the buffer size (default 4 kB)
                     - read or write or both (r, w, rw)""");
            return;
        }

        System.out.println("Running HDD random access benchmark...");

        String targetPath = args[1];
        HDDRandomAccess hra = new HDDRandomAccess(targetPath);

        long fileSize = 256 * 1024 * 1024; // 256 MB

        hra.initialize(fileSize);
        hra.warmUp();

        // Parse optional arguments
        String benchmarkType = args.length >= 3 ? args[2] : "fs";
        int bufferSize = 4096; // default 4 kB
        if (args.length >= 4) {
            try {
                bufferSize = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid buffer size, using default 4 kB.");
            }
        }
        String mode = args.length >= 5 ? args[4].toLowerCase() : "rw";

        // Run benchmarks based on mode
        if (mode.contains("r")) {
            System.out.println("Running random read...");
            hra.run("r", benchmarkType, bufferSize, 1000);
            System.out.println(hra.getResult());
        }
        if (mode.contains("w")) {
            System.out.println("Running random write...");
            hra.run("w", benchmarkType, bufferSize, 1000);
            System.out.println(hra.getResult());
        }

        hra.clean();
    }

}
