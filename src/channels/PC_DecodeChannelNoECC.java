package channels;

import blockcode.BlockCode;

public class PC_DecodeChannelNoECC extends IntegerInputChannel {

	private BlockCode code;

	public PC_DecodeChannelNoECC(BlockCode code) {
		super(code.getL() * code.getL() + 2 * code.getL() * (code.getN() - code.getL()));
		this.code = code;
	}

	@Override
	public void process() {
		for (int i = 0; i < code.getL() * code.getL(); ++i) {
			this.buffer.add(this.inputBuffer[i]);
		}
	}
}