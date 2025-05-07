package bench.hdd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import logger.ConsoleLogger;
import logger.ILog;
import timer.Timer;
import timer.TimeConst;

public class FileWriter {

	private static final int MIN_BUFFER_SIZE = 1024; // 1 KB
	private static final int MAX_BUFFER_SIZE = 1024 * 1024 * 64; // 32 MB
	private static final long MIN_FILE_SIZE = 1024 * 1024; // 1 MB
	private static final long MAX_FILE_SIZE = 1024 * 1024 * 1024; // 1 GB
	private final Timer timer = new Timer();
	private final Random rand = new Random();
	private final ILog logger;
	private NumberFormat nf;

	public FileWriter() {
		this.logger = new ConsoleLogger();
		nf = new DecimalFormat("#.000");
	}

	/**
	 * Writes files on disk using a variable write buffer and fixed file size.
	 * 
	 * @param filePrefix
	 *            - Path and file name
	 * @param fileSuffix
	 *            - file extension
	 * @param fileSize
	 *            - size of the benchmark file to be written in the disk
	 * @throws IOException
	 */
	public void streamWriteFixedFileSize(String filePrefix, String fileSuffix, long fileSize, boolean clean)
			throws IOException {

		logger.write("Testing file write speed with fixed file size of", fileSize / (1024 * 1024), "MB");
		int currentBufferSize = MIN_BUFFER_SIZE;
		String fileName;
		int fileIndex = 0;
		long totalTime = 0;
		double totalMegaBytes = 0;
		var scores = new ArrayList<Double>();

		while (currentBufferSize <= MAX_BUFFER_SIZE) {
			fileName = filePrefix + fileIndex + "." + fileSuffix;

			timer.start();
			writeFile(fileName, currentBufferSize, fileSize, clean);
			var time = timer.stop();
			totalTime += time;
			double currentMegaBytes = (double) fileSize / (1024 * 1024);
			totalMegaBytes += currentMegaBytes;

			double currentRate = currentMegaBytes / (time / TimeConst.NANO_TO_SECONDS); // MB/sec
			scores.add(currentRate); // MB/sec

			logger.write("Pass", fileIndex + 1, "Buffer size:", currentBufferSize / 1024, "kB",
					"Time:", nf.format(time / TimeConst.NANO_TO_MILLISECONDS), "ms",
					"Rate:", nf.format(currentRate), "MB/sec");

			currentBufferSize *= 4;
			fileIndex++;
		}

		// calculate average score
//		double totalRate = calcTotalRate(scores, fileIndex);
		double totalRate = totalMegaBytes / (totalTime / TimeConst.NANO_TO_SECONDS);

		printStats(filePrefix, totalRate, totalMegaBytes, totalTime, -1);
	}

	/**
	 * Writes files on disk using a variable file size and fixed buffer size.
	 * 
	 * @param filePrefix
	 *            - Path and file name
	 * @param fileSuffix
	 *            - file extension
	 * @param bufferSize
	 *            - size of the benchmark file to be written in the disk
	 */
	public void streamWriteFixedBufferSize(String filePrefix, String fileSuffix, int bufferSize, boolean clean)
			throws IOException {

		logger.write("Testing file write speed with fixed buffer size of", bufferSize / 1024, "kB");
		long currentFileSize = MIN_FILE_SIZE;
		int fileIndex = 0;
		long totalTime = 0;
		double totalMegaBytes = 0;
		var scores = new ArrayList<Double>();

		while (currentFileSize <= MAX_FILE_SIZE) {
			String fileName = filePrefix + fileIndex + "." + fileSuffix;

			timer.start();
			writeFile(fileName, bufferSize, currentFileSize, clean);
			var time = timer.stop();
			totalTime += time;
			double currentMegaBytes = (double) currentFileSize / (1024 * 1024);
			totalMegaBytes += currentMegaBytes;
			double currentRate = currentMegaBytes / (time / TimeConst.NANO_TO_SECONDS); // MB/sec
			scores.add((double) currentFileSize / (time / TimeConst.NANO_TO_SECONDS) / (1024 * 1024)); // MB/sec


			logger.write("Pass", fileIndex + 1, "File size:", currentMegaBytes, "MB",
					"Time:", time / TimeConst.NANO_TO_MILLISECONDS, "ms", "Rate:", nf.format(currentRate), "MB/sec");

			currentFileSize *= 4;
			fileIndex++;
		}

		// calculate average score
//		double totalRate = calcTotalRate(scores, fileIndex);
		double totalRate = totalMegaBytes / (totalTime / TimeConst.NANO_TO_SECONDS);

		printStats(filePrefix, totalRate, totalMegaBytes, totalTime, bufferSize);
	}

	/**
	 * Writes a file with random binary content on the disk, using a given file
	 * path and buffer size.
	 */
	private void writeFile(String fileName, int bufferSize, long fileSize, boolean clean) throws IOException {

		File folderPath = new File(fileName.substring(0, fileName.lastIndexOf(File.separator)));

		// create folder path to benchmark output
		if (!folderPath.isDirectory())
			folderPath.mkdirs();

		final File file = new File(fileName);
		// create stream writer with given buffer size
		final FileOutputStream fileStream = new FileOutputStream(file);
		final BufferedOutputStream outputStream = new BufferedOutputStream(fileStream, bufferSize);

		byte[] buffer = new byte[bufferSize];
		int i = 0;
		long toWrite = fileSize / bufferSize;

		while (i < toWrite) {
			// generate random content to write
			rand.nextBytes(buffer);
			outputStream.write(buffer);
			i++;
		}

		outputStream.close();
		fileStream.close();
		if(clean)
			file.delete();
	}

	private double calcTotalRate(ArrayList<Double> scores, int passCount) {
		double totalRate = 0;
		for (int i = 0; i < passCount; i++) {
			totalRate += scores.get(i);
		}
		return totalRate / passCount;
	}

	private void printStats(String filePrefix, double totalRate,
							double totalMegaBytes, long totalTime, int bufferSize) {

		double seconds = (double) totalTime / TimeConst.NANO_TO_SECONDS;

		var loggerMessage = "Done writing ";
		loggerMessage += totalMegaBytes + " MB ";
		loggerMessage += "in " + nf.format(seconds) + " seconds ";
		loggerMessage += "(" + nf.format(totalRate) + " MB/sec) ";

		if (bufferSize > 0) {
			loggerMessage += "with a buffer size of " + bufferSize / 1024 + " kB";
		} else {
			loggerMessage += "with a variable buffer size";
		}

		logger.write(loggerMessage);
	}
}