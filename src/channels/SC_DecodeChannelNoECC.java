package channels;

import blockcode.HammingCode_4;

public class SC_DecodeChannelNoECC extends IntegerBufferChannel {

	private Channel<Integer> outerDecodeChannel;
	private Channel<Integer> innerDecodeChannel;

	// TODO create IFC SystematicCode
	public SC_DecodeChannelNoECC(HammingCode_4 outerCode, HammingCode_4 innerCode) {
		super();
		this.outerDecodeChannel = outerCode.getDecodeChannelNoECC();
		this.innerDecodeChannel = innerCode.getDecodeChannelNoECC();
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