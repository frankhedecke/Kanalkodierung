package channels;


public class NoisyChannel extends IntegerBufferChannel {

	ErrorSource errors;

	public NoisyChannel(ErrorSource errors) {
		super();
		this.errors = errors;
	}

	@Override
	public void pushInput(Integer bit) {
		int blub = errors.getOutput() + bit;
		super.buffer.add(blub % 2);
	}
}
