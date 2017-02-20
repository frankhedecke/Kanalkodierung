package main;

import channels.Channel;
import channels.SC_DecodeChannel;
import channels.SC_EncodeChannel;
import blockcode.BlockCode;

public class SerialConcatenation {

	private BlockCode outerCode;
	private BlockCode innerCode;
	private int parameterL;
	private int parameterN;

	public float getCodeRate() {
		return (float)this.parameterL / this.parameterN;
	}

	public SerialConcatenation(BlockCode outerCode, BlockCode innerCode) {
		this.outerCode = outerCode;
		this.innerCode = innerCode;

		this.parameterL = outerCode.getL() * innerCode.getL();
		this.parameterN = outerCode.getN() * innerCode.getN();
	}

	public int getN() {
		return this.parameterN;
	}

	public int getL() {
		return this.parameterL;
	}

	public Channel<Integer> getDecodeChannel() {
		return new SC_DecodeChannel(this.outerCode, this.innerCode);
	}

	public Channel<Integer> getEncodeChannel() {
		return new SC_EncodeChannel(this.outerCode, this.innerCode);
	}
}
