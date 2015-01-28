public class DecodeChannel extends AbstractBufferChannel {

	private BlockCode code;
	private int[] inputBuffer;
	private int inputPtr;

	public DecodeChannel(BlockCode code) {
		super();
		this.code = code;
		this.inputBuffer = new int[code.getN()];
		this.inputPtr = 0;
	}

	@Override
	public void pushInput(int bit) {
		this.inputBuffer[this.inputPtr] = bit;
		this.inputPtr++;

		if (this.inputPtr == this.inputBuffer.length) {
			this.inputPtr = 0;
			BinaryWord input = new BinaryWord(inputBuffer);
			BinaryWord output = code.decode(input);

			for (int i : output.toArray()) {
				super.buffer.add(i);
			}
		}
	}
}