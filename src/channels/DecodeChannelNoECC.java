package channels;

import entity.BinaryWord;
import blockcode.HammingCode_4;

public class DecodeChannelNoECC extends IntegerBufferChannel {

	private HammingCode_4 code;
	private int[] inputBuffer;
	private int inputPtr;

	// TODO use BlockCode
	public DecodeChannelNoECC(HammingCode_4 code) {
		super();
		this.code = code;
		this.inputBuffer = new int[code.getN()];
		this.inputPtr = 0;
	}

	@Override
	public void pushInput(Integer bit) {
		this.inputBuffer[this.inputPtr] = bit;
		this.inputPtr++;

		if (this.inputPtr == this.inputBuffer.length) {
			this.inputPtr = 0;
			BinaryWord input = new BinaryWord(inputBuffer);
			BinaryWord output = code.decodeNoECC(input);

			for (int i : output.toArray()) {
				super.buffer.add(i);
			}
		}
	}
}