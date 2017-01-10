package main;

import blockcode.BlockCode;
import channels.Channel;
import channels.PC_DecodeChannelNoECC;
import channels.PC_HardIterDecodeChannel;
import channels.PC_IterDecodeChannel;
import channels.PC_EncodeChannel;

public class ParallelConcatenation {
	
	private BlockCode code;
	private int parameterN;
	private int parameterL;
	
	public ParallelConcatenation(BlockCode code) {
		this.code = code;
		this.parameterL = code.getL() * code.getL();
		this.parameterN = code.getL() * code.getL() + 2 * code.getL() * (code.getN() - code.getL());
	}

	public int getN() {
		return this.parameterN;
	}

	public int getL() {
		return this.parameterL;
	}

	public float getCodeRate() {
		return (float)this.parameterL / this.parameterN;
	}

	public Channel<Integer> getHardDecodeChannel() {
		return new PC_HardIterDecodeChannel(this.code);
	}
	
	public Channel<Integer> getDecodeChannelNoECC() {
		return new PC_DecodeChannelNoECC(this.code);
	}

	public Channel<Float> getSoftDecodeChannel() {
		return new PC_IterDecodeChannel(this.code);
	}
	
	public Channel<Integer> getEncodeChannel() {
		return new PC_EncodeChannel(code);
	}

}
