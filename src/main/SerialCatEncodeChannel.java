package main;
public class SerialCatEncodeChannel extends IntegerBufferChannel {

	private Channel<Integer> outerEncodeChannel;
	private Channel<Integer> innerEncodeChannel;

	public SerialCatEncodeChannel(BlockCode outerCode, BlockCode innerCode) {
		super();
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
		while (this.innerEncodeChannel.hasOutput()) {
			int out2 = this.innerEncodeChannel.getOutput();
			this.buffer.add(out2);
		}
	}

}
