package channels;

import blockcode.BlockCode;

public class SC_DecodeChannel extends IntegerBufferChannel {

	private Channel<Integer> outerDecodeChannel;
	private Channel<Integer> innerDecodeChannel;

	public SC_DecodeChannel(BlockCode outerCode, BlockCode innerCode) {
		super();
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
		while (this.outerDecodeChannel.hasOutput()) {
			int out2 = this.outerDecodeChannel.getOutput();
			this.buffer.add(out2);
		}
	}
}
