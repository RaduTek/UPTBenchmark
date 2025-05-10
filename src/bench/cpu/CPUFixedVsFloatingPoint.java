package bench.cpu;

import bench.IBenchmark;

public class CPUFixedVsFloatingPoint implements IBenchmark {

	private int result;
	private int size;

	// Enum for number representation
	public enum NumberRepresentation {
		FIXED,
		FLOATING
	}

	@Override
	public void initialize(Object ... params) {
		this.size = (Integer)params[0];
	}

	@Override
	public void warmUp() {
		// Perform a simple operation for warmup for both representations
		int fixed = 0;
		float floating = 0.0f;
		for (int i = 0; i < size; ++i) {
			fixed += i * 2 - 3;      // fixed point emulation
			floating += i * 2.0f - 3.0f; // floating point
		}
		// prevent optimization
		result = fixed + (int)floating;
	}

	@Override
	@Deprecated
	public void run() {
		// Deprecated, do nothing or call the preferred run with default
	}

	@Override
	public void run(Object ...options) {
		result = 0;

		NumberRepresentation rep = NumberRepresentation.FIXED;
		if (options != null && options.length > 0 && options[0] instanceof NumberRepresentation) {
			rep = (NumberRepresentation) options[0];
		}

		switch (rep) {
			case FLOATING:
				float floatResult = 0.0f;
				for (int i = 0; i < size; ++i)
					floatResult += i * 2.0f - 3.0f;
				result = (int) floatResult; // store as int for compatibility
				break;
			case FIXED:
				int fixedResult = 0;
				for (int i = 0; i < size; ++i)
					fixedResult += i * 2 - 3;
				result = fixedResult;
				break;
			default:
				break;
		}

	}

	@Override
	public void cancel() {
		// No cancellation logic needed for this simple benchmark
	}

	@Override
	public String getName() {
		return "CPU Fixed vs Floating Point";
	}

	@Override
	public void clean() {
		// Nothing to clean
	}

	@Override
	public String getResult() {
		return String.valueOf(result);
	}
}