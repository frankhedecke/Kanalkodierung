package channels;

import blockcode.BlockCode;

public class SC_InterEncodeChannel implements Channel<Integer> {

	private Channel<Integer> outerEncodeChannel;
	private Channel<Integer> innerEncodeChannel;
	private Channel<Integer> interleaver;

	public SC_InterEncodeChannel(BlockCode outerCode, BlockCode innerCode) {
		this.outerEncodeChannel = outerCode.getEncodeChannel();
		this.innerEncodeChannel = innerCode.getEncodeChannel();
		int depth = outerCode.getN();
		int length = innerCode.getL();
		this.interleaver = new BlockInterleaver(depth, length);
	}

	@Override
	public void pushInput(Integer bit) {
		this.outerEncodeChannel.pushInput(bit);

		while (this.outerEncodeChannel.hasOutput()) {
			int out1 = this.outerEncodeChannel.getOutput();
			this.interleaver.pushInput(out1);
		}
		while (this.interleaver.hasOutput()) {
			int out = this.interleaver.getOutput();
			this.innerEncodeChannel.pushInput(out);
		}
	}

	@Override
	public boolean hasOutput() {
		return this.innerEncodeChannel.hasOutput();
	}

	@Override
	public Integer getOutput() {
		return this.innerEncodeChannel.getOutput();
	}
}