package blockcode;

import channels.Channel;
import channels.DecodeChannel;
import channels.EncodeChannel;
import channels.EncodeChannelOrdered;
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

	@Override
	public Channel<Integer> createOrderedEncodeChannel() {
		return new EncodeChannelOrdered(this);
	}

	@Override
	public float getCodeRate() {
		return 1.0f * this.getL() / this.getN();
	}
}