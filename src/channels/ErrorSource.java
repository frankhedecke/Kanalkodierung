package channels;
import java.util.Random;

public class ErrorSource extends AbstractSource {

	private int errorRate;
	private Random random;

	public ErrorSource(int errorRate, long seed) {

		if (errorRate < 0) {
			System.err.println("errorRate have to be >= 0; will use "
					+ -errorRate + " instead");
			this.errorRate = -errorRate;
		} else {
			this.errorRate = errorRate;
		}

		this.random = new Random(seed);
	}

	@Override
	public Integer getOutput() {
		if (this.errorRate < 2) {
			return this.errorRate;
		}

		return random.nextInt(errorRate) == 1 ? 1 : 0;
	}

}
