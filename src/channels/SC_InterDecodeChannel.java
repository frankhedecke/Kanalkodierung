package channels;

import blockcode.BlockCode;

public class SC_InterDecodeChannel implements Channel<Integer> {

	private Channel<Integer> outerDecodeChannel;
	private Channel<Integer> innerDecodeChannel;
	private Channel<Integer> deInterleaver;

	public SC_InterDecodeChannel(BlockCode outerCode, BlockCode innerCode) {
		this.innerDecodeChannel = innerCode.getDecodeChannel();
		this.outerDecodeChannel = outerCode.getDecodeChannel();
		int depth = innerCode.getL();
		int length = outerCode.getN();
		this.deInterleaver = new BlockInterleaver(depth, length);
	}

	@Override
	public void pushInput(Integer bit) {
		this.innerDecodeChannel.pushInput(bit);

		while (this.innerDecodeChannel.hasOutput()) {
			int out1 = this.innerDecodeChannel.getOutput();
			this.deInterleaver.pushInput(out1);
		}
		while (this.deInterleaver.hasOutput()) {
			int out = this.deInterleaver.getOutput();
			this.outerDecodeChannel.pushInput(out);
		}
	}

	@Override
	public boolean hasOutput() {
		return this.outerDecodeChannel.hasOutput();
	}

	@Override
	public Integer getOutput() {
		return this.outerDecodeChannel.getOutput();
	}
}