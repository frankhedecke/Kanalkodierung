package channels;

import blockcode.BlockCode;
import main.Modulator;

/**
 * This channel wraps the PC_IterDecodeChannel and modulates its soft output to hard output
 *
 */
public class PC_HardIterDecodeChannel implements Channel<Integer> {
// change class name to to PC_Decoder_HardIter

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
