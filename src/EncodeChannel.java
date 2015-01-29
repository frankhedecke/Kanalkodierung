public class EncodeChannel extends IntegerBufferChannel {

	private BlockCode code;
	private int[] inputBuffer;
	private int inputPtr;

	public EncodeChannel(BlockCode code) {
		super();
		this.code = code;
		this.inputBuffer = new int[code.getL()];
		this.inputPtr = 0;
	}

	@Override
	public void pushInput(Integer bit) {
		this.inputBuffer[this.inputPtr] = bit;
		this.inputPtr++;

		if (this.inputPtr == this.inputBuffer.length) {
			this.inputPtr = 0;
			BinaryWord input = new BinaryWord(inputBuffer);
			BinaryWord output = code.encode(input);

			for (int i : output.toArray()) {
				super.buffer.add(i);
			}
		}
	}
}
