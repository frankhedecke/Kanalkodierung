package main;
public class SerialCatDecodeChannel extends IntegerBufferChannel {

	private Channel<Integer> outerDecodeChannel;
	private Channel<Integer> innerDecodeChannel;

	public SerialCatDecodeChannel(BlockCode outerCode, BlockCode innerCode) {
		super();
		this.outerDecodeChannel = outerCode.getDecodeChannel();
		this.innerDecodeChannel = innerCode.getDecodeChannel();
	}

	@Override
	public void pushInput(Integer bit) {
		this.outerDecodeChannel.pushInput(bit);
		while (this.outerDecodeChannel.hasOutput()) {
			int out1 = this.outerDecodeChannel.getOutput();
			this.innerDecodeChannel.pushInput(out1);
		}
		while (this.innerDecodeChannel.hasOutput()) {
			int out2 = this.innerDecodeChannel.getOutput();
			this.buffer.add(out2);
		}
	}

}
