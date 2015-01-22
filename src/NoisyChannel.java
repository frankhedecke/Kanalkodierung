public class NoisyChannel extends AbstractBufferChannel {

	ErrorSource errors;

	public NoisyChannel(ErrorSource errors) {
		super();
		this.errors = errors;
	}

	@Override
	public void pushInput(int bit) {
		int blub = errors.getOutput() + bit;
		super.buffer.add(blub % 2);
	}
}
