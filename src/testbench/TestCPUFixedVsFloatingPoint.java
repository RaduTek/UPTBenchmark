package testbench;

import logger.ConsoleLogger;
import logger.ILog;
import logger.TimeUnit;
import timer.ITimer;
import timer.Timer;
import bench.IBenchmark;
import bench.cpu.CPUFixedVsFloatingPoint;
import bench.cpu.NumberRepresentation;

public class TestCPUFixedVsFloatingPoint {

	public static void main(String[] args) {
		ITimer timer = new Timer();
		ILog log = /* new FileLogger("bench.log"); */new ConsoleLogger();
		TimeUnit timeUnit = TimeUnit.MILLI;

		IBenchmark bench = new CPUFixedVsFloatingPoint();
		bench.initialize(10000000);
		bench.warmUp();

		timer.start();
		bench.run(NumberRepresentation.FIXED);
//		bench.run(NumberRepresentation.FLOATING);
		long time = timer.stop();
		log.writeTime("Finished in", time, timeUnit);
		log.write("Result is " + bench.getResult());

		bench.clean();
		log.close();
	}
}