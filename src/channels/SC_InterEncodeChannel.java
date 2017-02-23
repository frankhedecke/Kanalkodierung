package channels;

import main.BlockInterleaver;
import blockcode.BlockCode;

public class SC_InterEncodeChannel extends IntegerBufferChannel {

	private Channel<Integer> outerEncodeChannel;
	private Channel<Integer> innerEncodeChannel;
	private Channel<Integer> interleaver;

	public SC_InterEncodeChannel(BlockCode outerCode, BlockCode innerCode) {
		super();
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
		while (this.innerEncodeChannel.hasOutput()) {
			int out2 = this.innerEncodeChannel.getOutput();
			this.buffer.add(out2);
		}
	}
}