package channels;

import blockcode.BlockCode;

public class SC_EncodeChannel implements Channel<Integer> {

	private Channel<Integer> outerEncodeChannel;
	private Channel<Integer> innerEncodeChannel;

	public SC_EncodeChannel(BlockCode outerCode, BlockCode innerCode) {
		this.outerEncodeChannel = outerCode.getEncodeChannel();
		this.innerEncodeChannel = innerCode.getEncodeChannel();
	}

	@Override
	public void pushInput(Integer bit) {
		this.outerEncodeChannel.pushInput(bit);

		while (this.outerEncodeChannel.hasOutput()) {
			int out1 = this.outerEncodeChannel.getOutput();
			this.innerEncodeChannel.pushInput(out1);
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