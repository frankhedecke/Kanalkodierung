package main;
public class SerialConcatenation {

	private BlockCode outerCode;
	private BlockCode innerCode;
	private int parameterL;
	private int parameterN;

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
		return new SerialCatDecodeChannel(this.outerCode, this.innerCode);
	}

	public Channel<Integer> getEncodeChannel() {
		return new SerialCatEncodeChannel(this.outerCode, this.innerCode);
	}
}
