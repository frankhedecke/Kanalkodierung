package channels;

public class ErrorSourceAWGN extends AbstractSource<Float> {
	
	private float coteRate;
	private float noise_dB;
	
	public ErrorSourceAWGN(float codeRate, float noise_dB) {
		
		// TODO check codeRate
		// TODO check noise_dB
		this.coteRate = codeRate;
		this.noise_dB = noise_dB;
	}

	@Override
	public Float getOutput() {
		// TODO DELETE, just a stub for testing
		return 0.2f;
	}
}
