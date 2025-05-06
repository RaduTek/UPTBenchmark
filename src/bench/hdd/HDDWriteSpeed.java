package bench.hdd;

import java.io.IOException;

import bench.IBenchmark;

public class HDDWriteSpeed implements IBenchmark {
	private final String targetPath;

	public HDDWriteSpeed(String targetPath) {
		this.targetPath = targetPath;
	}

	@Override
	public String getName() {
		return "HDD Write Speed Benchmark";
	}

	@Override
	public void initialize(Object... params) {
	}

	@Override
	public void warmUp() {
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException("Method not implemented. Use run(Object) instead");
	}

	@Override
	public void run(Object... options) {
		FileWriter writer = new FileWriter();
		// either "fs" - fixed size, or "fb" - fixed buffer
		String option = (String) options[0];
		// true/false whether the written files should be deleted at the end
		Boolean clean = (Boolean) options[1];

		String prefix = targetPath + "/tmp/";
		String suffix = ".dat";
		long fileSize = 512 * 1024 * 1024; // 512 MB
		int bufferSize = 4 * 1024; // 4 KB
		
		try {
			if (option.equals("fs"))
				writer.streamWriteFixedFileSize(prefix, suffix, fileSize, clean);
			else if (option.equals("fb"))
				writer.streamWriteFixedBufferSize(prefix, suffix, bufferSize, clean);
			else
				throw new IllegalArgumentException("Argument "
						+ options[0].toString() + " is undefined");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void clean() {
	}

	@Override
	public void cancel() {
	}

	@Override
	public String getResult() {
		return null; // or MBps
	}
}
