package bench.ram;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.util.Random;

import timer.TimeConst;
import timer.Timer;
import bench.IBenchmark;

/**
 * Maps a large file into RAM triggering the virtual memory mechanism. Performs
 * reads and writes to the respective file.<br>
 * The access speeds depend on the file size: if the file can fit the available
 * RAM, then we are measuring RAM speeds.<br>
 * Conversely, we are measuring the access speed of virtual memory, implying a
 * mixture of RAM and HDD access speeds (i.e., lower speeds).
 */
public class VirtualMemoryBenchmark implements IBenchmark {

	private String result = "";
	private NumberFormat nf;
	private Random random;
	private MemoryMapper core;

	public VirtualMemoryBenchmark() {
		nf = new DecimalFormat("#.00"); // format for speed in MB/s
		random = new Random();
	}

	@Override
	public void initialize(Object... params) {
		/* not today */
	}

	@Override
	public void warmUp() {
		/* summer is coming anyway */
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Use run(Object[]) instead");
	}

	@Override
	public void run(Object... options) {
		// expected: {fileSize, bufferSize}	
		Object[] params = (Object[]) options;
		long fileSize = Long.parseLong(params[0].toString()); // e.g. 2-16GB
		int bufferSize = Integer.parseInt(params[1].toString()); // e.g. 4+KB

		try {
			core = new MemoryMapper("./", fileSize); // change path as needed
			byte[] buffer = new byte[bufferSize];

			Timer timer = new Timer();

			// write to VM
			timer.start();
			timer.pause();
			for (long i = 0; i < fileSize; i += bufferSize) {
				// 1. fill buffer with random content
				random.nextBytes(buffer);
				// 2. write to memory mapper
				timer.resume();
				core.put(i, buffer);
				timer.pause();
			}
			long fileSizeMegaBytes = (long) (fileSize / (1024.0 * 1024.0)); // convert to MB
			double speed = fileSizeMegaBytes / (timer.stop() / TimeConst.NANO_TO_SECONDS); /* 3. fileSize/time [MB/s] */

			result = "\nWrote " + fileSizeMegaBytes
					+ " MB to virtual memory at " + nf.format(speed) + " MB/s";

			// read from VM
			timer.start(); // reset timer
			timer.pause();
			for (long i = 0; i < fileSize; i += bufferSize) {
				// 5. get from memory mapper
				timer.resume();
				core.get(i, bufferSize);
				timer.pause();
			}
			speed = fileSizeMegaBytes / (timer.stop() / TimeConst.NANO_TO_SECONDS); /* 6. MB/s */

			// append to previous 'result' string
			result += "\nRead " + fileSizeMegaBytes
					+ " MB from virtual memory at " + nf.format(speed) + " MB/s";
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (core != null)
				core.purge();
		}
	}

	@Override
	public void clean() {
		if (core != null)
			core.purge();
	}

	@Override
	public void cancel() {

	}

	@Override
	public String getName() {
		return "Virtual Memory Benchmark";
	}

	@Override
	public String getResult() {
		return result;
	}

}
