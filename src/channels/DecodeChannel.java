package channels;

import entity.BinaryWord;
import blockcode.BlockCode;

public class DecodeChannel extends IntegerInputChannel {

	private BlockCode code;

	public DecodeChannel(BlockCode code) {
		super(code.getN());
		this.code = code;
	}

	@Override
	public void process() {
		BinaryWord input = new BinaryWord(inputBuffer);
		BinaryWord output = code.decode(input);

		for (int i : output.toArray()) {
			super.buffer.add(i);
		}
	}
}