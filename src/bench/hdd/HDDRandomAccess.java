package bench.hdd;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

import timer.TimeConst;
import timer.Timer;
import bench.IBenchmark;

public class HDDRandomAccess implements IBenchmark {
    private final String targetPath;
    private String tempFilePath;
    private String result;

    public HDDRandomAccess(String targetPath) {
        this.targetPath = targetPath;
    }

    @Override
    public String getName() {
        return "HDD Random Access Benchmark";
    }

    @Override
    public void initialize(Object... params) {
        tempFilePath = targetPath + "/tmp/random-access.dat";
        File tempFile = new File(tempFilePath);
        RandomAccessFile rafFile;
        long fileSizeInBytes = (Long) params[0];

        // Create a temporary file with random content to be used for
        // reading and writing
        try {
            rafFile = new RandomAccessFile(tempFile, "rw");
            Random rand = new Random();
            int bufferSize = 4 * 1024; // 4KB
            long toWrite = fileSizeInBytes / bufferSize;
            byte[] buffer = new byte[bufferSize];
            long counter = 0;

            while (counter++ < toWrite) {
                rand.nextBytes(buffer);
                rafFile.write(buffer);
            }
            rafFile.close();
            tempFile.deleteOnExit();

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void warmUp() {
        // have a Mountain Dew or Red Bull
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Use run(Object[]) instead");
    }

    @Override
    public void run(Object ...options) {
        // ex. {"r", "fs", 4*1024}
        Object[] param = (Object[]) options;
        // used by the fixed size option
        final int steps = 25000; // number of I/O ops
        // used by the fixed time option
        final int runtime = 5000; // ms

        try {
            // read benchmark
            if (String.valueOf(param[0]).equalsIgnoreCase("r")) {
                // buffer size given as parameter
                int bufferSize = Integer.parseInt(String.valueOf(param[2]));

                // read a fixed size and measure time
                if (String.valueOf(param[1]).equalsIgnoreCase("fs")) {

                    long timeMs = new RandomAccess().randomReadFixedSize(tempFilePath,
                            bufferSize, steps);
                    result = steps + " random reads in " + timeMs + " ms ["
                            + (steps * bufferSize / 1024 / 1024) + " MB, "
                            + 1.0 * (steps * bufferSize / 1024 / 1024) / timeMs * 1000 + "MB/s]";
                }
                // read for a fixed time amount and measure time
                else if (String.valueOf(param[1]).equalsIgnoreCase("ft")) {

                    int ios = new RandomAccess().randomReadFixedTime(tempFilePath,
                            bufferSize, runtime);
                    result = ios + " I/Os per second ["
                            + (ios * bufferSize / 1024 / 1024) + " MB, "
                            + 1.0 * (ios * bufferSize / 1024 / 1024) / runtime * 1000 + "MB/s]";
                } else
                    throw new UnsupportedOperationException("Read option \""
                            + String.valueOf(param[1])
                            + "\" is not implemented");
            }
            // write benchmark
            else if (String.valueOf(param[0]).equalsIgnoreCase("w")) {
                // buffer size given as parameter
                int bufferSize = Integer.parseInt(String.valueOf(param[2]));

                // read a fixed size and measure time
                if (String.valueOf(param[1]).equalsIgnoreCase("fs")) {

                    long timeMs = new RandomAccess().randomWriteFixedSize(tempFilePath,
                            bufferSize, steps);
                    result = steps + " random writes in " + timeMs + " ms ["
                            + (steps * bufferSize / 1024 / 1024) + " MB, "
                            + 1.0 * (steps * bufferSize / 1024 / 1024) / timeMs * 1000 + "MB/s]";
                }
                // read for a fixed time amount and measure time
                else if (String.valueOf(param[1]).equalsIgnoreCase("ft")) {

                    int ios = new RandomAccess().randomWriteFixedTime(tempFilePath,
                            bufferSize, runtime);
                    result = ios + " I/Os per second ["
                            + (ios * bufferSize / 1024 / 1024) + " MB, "
                            + 1.0 * (ios * bufferSize / 1024 / 1024) / runtime * 1000 + "MB/s]";
                } else
                    throw new UnsupportedOperationException("Write option \""
                            + String.valueOf(param[1])
                            + "\" is not implemented");
            } else
                throw new UnsupportedOperationException("Benchmark option \""
                        + String.valueOf(param[0]) + "\" is not implemented");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clean() {
        // remove created file perhaps?
    }

    @Override
    public void cancel() {
        //
    }

    @Override
    public String getResult() {
        return String.valueOf(result);
    }

    class RandomAccess {
        private Random random;

        RandomAccess() {
            random = new Random();
        }

        /**
         * Reads data from random positions into a fixed size buffer from a
         * given file using RandomAccessFile
         *
         * @param filePath
         *            Full path to file on disk
         * @param bufferSize
         *            Size of byte buffer to read at each step
         * @param toRead
         *            Number of steps to repeat random read
         * @return Amount of time needed to finish given reads in milliseconds
         * @throws IOException
         */
        public long randomReadFixedSize(String filePath, int bufferSize, int toRead) throws IOException {
            return randomFixedSize(filePath, bufferSize, toRead, false);
        }

        /**
         * Writes data from random positions into a fixed size buffer from a
         * given file using RandomAccessFile
         *
         * @param filePath
         *            Full path to file on disk
         * @param bufferSize
         *            Size of byte buffer to read at each step
         * @param toWrite
         *            Number of steps to repeat random read
         * @return Amount of time needed to finish given reads in milliseconds
         * @throws IOException
         */
        public long randomWriteFixedSize(String filePath, int bufferSize, int toWrite) throws IOException {
            return randomFixedSize(filePath, bufferSize, toWrite, true);
        }

        private long randomFixedSize(String filePath, int bufferSize, int passes, boolean write) throws IOException {
            RandomAccessFile file = new RandomAccessFile(filePath, "r");
            long fileSize = file.length();
            file.close();

            if (bufferSize > fileSize) {
                bufferSize = (int) fileSize;
            }

            Timer timer = new Timer();
            timer.start();

            for (int i = 0; i < passes; i++) {
                // go to random position, avoiding EOF
                long position = Math.abs(random.nextLong()) % (fileSize - bufferSize + 1);

                if (write) {
                    writeToFile(filePath, (int) position, bufferSize);
                } else {
                    readFromFile(filePath, (int) position, bufferSize);
                }
            }

            file.close();
            return timer.stop() / (long) TimeConst.NANO_TO_MILLISECONDS; // Convert nanoseconds to milliseconds
        }

        /**
         * Reads data from random positions into a fixed size buffer from a
         * given file using RandomAccessFile for one second, or any other given
         * time.
         *
         * @param filePath
         *            Full path to file on disk
         * @param bufferSize
         *            Size of byte buffer to read at each step
         * @param millis
         *            Total time to read from file (in milliseconds)
         * @return Number of reads in the given amount of time
         * @throws IOException
         */
        public int randomReadFixedTime(String filePath, int bufferSize, int millis) throws IOException {
            return randomFixedTime(filePath, bufferSize, millis, false);
        }

        /**
         * Writes data from random positions into a fixed size buffer from a
         * given file using RandomAccessFile for one second, or any other given
         * time.
         *
         * @param filePath
         *            Full path to file on disk
         * @param bufferSize
         *            Size of byte buffer to read at each step
         * @param millis
         *            Total time to read from file (in milliseconds)
         * @return Number of reads in the given amount of time
         * @throws IOException
         */
        public int randomWriteFixedTime(String filePath, int bufferSize, int millis) throws IOException {
            return randomFixedTime(filePath, bufferSize, millis, true);
        }

        private int randomFixedTime(String filePath, int bufferSize, int millis, boolean write) throws IOException {
            RandomAccessFile file = new RandomAccessFile(filePath, "r");
            long fileSize = file.length();
            file.close();

            if (bufferSize > fileSize) {
                bufferSize = (int) fileSize;
            }

            int counter = 0;

            long startTime = System.nanoTime();
            long durationNanos = millis * (long) TimeConst.NANO_TO_MILLISECONDS;

            while ((System.nanoTime() - startTime) < durationNanos) {
                long position = Math.abs(random.nextLong()) % (fileSize - bufferSize + 1);

                if (write) {
                    writeToFile(filePath, (int) position, bufferSize);
                } else {
                    readFromFile(filePath, (int) position, bufferSize);
                }

                counter++;
            }

            file.close();
            return counter;
        }


        /**
         * Read data from a file at a specific position
         *
         * @param filePath
         *            Path to file
         * @param position
         *            Position in file
         * @param size
         *            Number of bytes to reads from the given position
         * @return Data that was read
         * @throws IOException
         */
        public byte[] readFromFile(String filePath, int position, int size)
                throws IOException {

            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            file.seek(position);
            byte[] bytes = new byte[size];
            file.read(bytes);
            file.close();
            return bytes;
        }

        /**
         * Write random data to a file at a specific position
         *
         * @param filePath
         *            Path to file
         * @param position
         *            Start position in file
         * @param size
         *            Size of random data to write
         * @throws IOException
         */
        public void writeToFile(String filePath, int position, int size)
                throws IOException {

            RandomAccessFile file = new RandomAccessFile(filePath, "rw");
            file.seek(position);
            byte[] buffer = new byte[size];
            random.nextBytes(buffer);
            file.write(buffer);
            file.close();
        }
    }

}
