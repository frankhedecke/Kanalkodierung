package blockcode;

import main.Channel;
import main.DecodeChannel;
import main.EncodeChannel;
import entity.BinaryMatrix;

public abstract class AbstractBlockCode implements BlockCode{

	protected BinaryMatrix generatorMatrix;
	protected BinaryMatrix controlMatrix;

	@Override
	public BinaryMatrix getGeneratorMatrix() {
		return this.generatorMatrix;
	}

	@Override
	public BinaryMatrix getControlMatrix() {
		return this.controlMatrix;
	}

	@Override
	public Channel<Integer> getDecodeChannel() {
		return new DecodeChannel(this);
	}

	@Override
	public Channel<Integer> getEncodeChannel() {
		return new EncodeChannel(this);
	}
}
