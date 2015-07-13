package channels;

public class NoisyChannel extends IntegerBufferChannel {

	ErrorSource errorSource;

	public NoisyChannel(ErrorSource errorSource) {
		super();
		this.errorSource = errorSource;
	}

	@Override
	public void pushInput(Integer bit) {
		int value = bit + this.errorSource.getOutput();
		super.buffer.add(value % 2);
	}
}
