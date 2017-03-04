package channels;

public abstract class IntegerInputChannel extends IntegerBufferChannel {

	protected int[] inputBuffer;
	protected int inputBufferSize;
	private int inputCursor;

	IntegerInputChannel(int inputBufferSize) {
		this.inputBufferSize = inputBufferSize;
		this.inputCursor = 0;
		this.inputBuffer = new int[inputBufferSize];
	}

	@Override
	public void pushInput(Integer bit) {
		this.inputBuffer[this.inputCursor] = bit;
		this.inputCursor++;

		if (this.inputCursor == this.inputBufferSize) {
			this.inputCursor = 0;
			process();
		} 
	}

	public abstract void process();
}