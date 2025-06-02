package bench.cpu;

import bench.IBenchmark;

public class CPUFixedVsFloatingPoint implements IBenchmark {

	private double result;
	private int size;

	@Override
	public void initialize(Object... params) {
		this.size = (Integer) params[0];
	}

	@Override
	public void warmUp() {
		double dummy = 0;
		for (int i = 1; i < size; ++i) {
			dummy += i >> 8;       // fixed (int division via shift)
			dummy += i / 256.0;    // floating (double division)
		}
	}

	@Override
	@Deprecated
	public void run() {}

	@Override
	public void run(Object... options) {
		result = 0;

		switch ((NumberRepresentation) options[0]) {
			case FLOATING:
				for (int i = 1; i < size; ++i)
					result += i / 256.0;
				break;
			case FIXED:
				for (int i = 1; i < size; ++i)
					result += i >> 8;
				break;
		}
	}

	@Override
	public void cancel() {}

	@Override
	public void clean() {}

	@Override
	public String getResult() {
		return String.valueOf(result);
	}
}
