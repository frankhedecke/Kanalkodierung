package main;

import channels.Channel;
import channels.PC_HardIterDecodeChannel;
import channels.PC_EncodeChannel;
import blockcode.BlockCode;

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

	public Channel<Integer> getDecodeChannel() {
		return new PC_HardIterDecodeChannel(this.code);
	}
	
	public Channel<Integer> getEncodeChannel() {
		return new PC_EncodeChannel(code);
	}

}
