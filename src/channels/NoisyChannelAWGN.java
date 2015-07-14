package channels;

public class NoisyChannelAWGN extends FloatBufferChannel {

	ErrorSourceAWGN errorSource;

	public NoisyChannelAWGN(ErrorSourceAWGN errorSource) {
		super();
		this.errorSource = errorSource;
	}

	@Override
	public void pushInput(Float bit) {
		float value = bit + this.errorSource.getOutput();
		super.buffer.add(value);
	}
}
