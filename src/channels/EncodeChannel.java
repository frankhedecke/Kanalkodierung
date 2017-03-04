package channels;

import entity.BinaryWord;
import blockcode.BlockCode;

public class EncodeChannel extends IntegerInputChannel {

	private BlockCode code;

	public EncodeChannel(BlockCode code) {
		super(code.getL());
		this.code = code;
	}

	@Override
	public void process() {
		BinaryWord input = new BinaryWord(inputBuffer);
		BinaryWord output = code.encode(input);

		for (int i : output.toArray()) {
			super.buffer.add(i);
		}
	}
}