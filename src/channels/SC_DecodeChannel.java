package channels;

import blockcode.BlockCode;

public class SC_DecodeChannel implements Channel<Integer> {

	private Channel<Integer> outerDecodeChannel;
	private Channel<Integer> innerDecodeChannel;

	public SC_DecodeChannel(BlockCode outerCode, BlockCode innerCode) {
		this.outerDecodeChannel = outerCode.getDecodeChannel();
		this.innerDecodeChannel = innerCode.getDecodeChannel();
	}

	@Override
	public void pushInput(Integer bit) {
		this.innerDecodeChannel.pushInput(bit);

		while (this.innerDecodeChannel.hasOutput()) {
			int out1 = this.innerDecodeChannel.getOutput();
			this.outerDecodeChannel.pushInput(out1);
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