public class SerialCatDecodeChannel extends AbstractBufferChannel {

	private Channel outerDecodeChannel;
	private Channel innerDecodeChannel;

	public SerialCatDecodeChannel(BlockCode outerCode, BlockCode innerCode) {
		super();
		this.outerDecodeChannel = outerCode.getDecodeChannel();
		this.innerDecodeChannel = innerCode.getDecodeChannel();
	}

	@Override
	public void pushInput(int bit) {
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
