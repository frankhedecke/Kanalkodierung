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

	public Channel getDecodeChannel() {
		return new SerialCatDecodeChannel(this.outerCode, this.innerCode);
	}

	public Channel getEncodeChannel() {
		return new SerialCatEncodeChannel(this.outerCode, this.innerCode);
	}
}
