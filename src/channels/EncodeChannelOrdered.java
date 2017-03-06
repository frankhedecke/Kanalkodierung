package channels;

import entity.BinaryWord;
import blockcode.BlockCode;

public class EncodeChannelOrdered extends IntegerInputChannel {

	private BlockCode code;

	public EncodeChannelOrdered(BlockCode code) {
		super(code.getL());
		this.code = code;
	}

	@Override
	public void process() {
		BinaryWord input = new BinaryWord(inputBuffer);
		BinaryWord output = code.encode(input);

		for (int i : code.getBitOrder()) {
			super.buffer.add(output.getElement(i));
		}
	}
}