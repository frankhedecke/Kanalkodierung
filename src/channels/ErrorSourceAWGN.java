package channels;
import java.util.Random;

public class ErrorSourceAWGN extends AbstractSource<Float> {
	
	private float codeRate;
	private float snr_dB;
	private Random random;
	
	public ErrorSourceAWGN(float codeRate, float snr_dB, long seed) {
		
		// TODO check codeRate
		// TODO check noise_dB
		this.codeRate = codeRate;
		this.snr_dB = snr_dB;
		this.random = new Random(seed);
	}

	@Override
	public Float getOutput() {
		// rand_val is gaussian distributed with a standard derivation = 1.0 (and mean = 0.0)
		double random_value = this.random.nextGaussian();
		// adjust the standard derivation
		double error_value = random_value * Math.sqrt(this.getVariance());

		return (float) error_value;
	}

	public Double getVariance() {
		return (1.0 / (2.0 * codeRate * Math.pow(10, snr_dB / 10)));
	}
}
