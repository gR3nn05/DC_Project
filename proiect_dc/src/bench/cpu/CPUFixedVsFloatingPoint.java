package bench.cpu;

import bench.IBenchmark;

public class CPUFixedVsFloatingPoint implements IBenchmark {

	private int result;
	private int size;

	@Override
	public void initialize(Object ... params) {
		this.size = (Integer)params[0];
	}

	@Override
	public void warmUp() {
		for (int i = 0; i < size; ++i) {
			// Fixed point warmup
			result = i / 256;
			// Floating point warmup
			result = (int)(i / 256.0);
		}
	}

	@Override
	@Deprecated
	public void run() {
	}

	@Override
	public void run(Object ...options) {
		result = 0;

		switch ((NumberRepresentation) options[0]) {
			case FLOATING:
				for (int i = 0; i < size; ++i)
					result += i / 256.0; // Floating point division
				break;
			case FIXED:
				for (int i = 0; i < size; ++i)
					result += i >> 8; // Fixed point division
				break;
			case FIXED_OPTIMIZED:
				for (int i = 0; i < size; ++i)
					result += i >> 8; // Optimized fixed point (bit shift instead of division)
				break;
			default:
				break;
		}
	}

	@Override
	public void cancel() {

	}

	@Override
	public void clean() {
	}

	@Override
	public String getResult() {
		return String.valueOf(result);
	}
}