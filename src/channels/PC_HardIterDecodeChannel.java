package channels;

import blockcode.BlockCode;
import main.Modulator;

public class PC_HardIterDecodeChannel implements Channel<Integer> {

	private BlockCode code;
	private PC_IterDecodeChannel decodeChannel; 
	
	public PC_HardIterDecodeChannel(BlockCode code) {
		this.code = code;
		this.decodeChannel = new PC_IterDecodeChannel(this.code);
	}

	@Override
	public void pushInput(Integer bit) {
		this.decodeChannel.pushInput(Modulator.hardToSoft(bit));
	}

	@Override
	public boolean hasOutput() {
		return this.decodeChannel.hasOutput();
	}

	@Override
	public Integer getOutput() {
		return Modulator.softToHard(this.decodeChannel.getOutput());
	}
}
