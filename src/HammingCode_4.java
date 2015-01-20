public class HammingCode_4 implements BlockCode {

	private BinaryMatrix generatorMatrix;
	private BinaryMatrix controlMatrix;

	// TODO control matrix
	public HammingCode_4() {
		this.generatorMatrix = new BinaryMatrix(7, 4);
		this.controlMatrix = new BinaryMatrix(3, 7);

		this.generatorMatrix.setRow(1, new BinaryWord("1101"));
		this.generatorMatrix.setRow(2, new BinaryWord("1011"));
		this.generatorMatrix.setRow(3, new BinaryWord("1000"));
		this.generatorMatrix.setRow(4, new BinaryWord("0111"));
		this.generatorMatrix.setRow(5, new BinaryWord("0100"));
		this.generatorMatrix.setRow(6, new BinaryWord("0010"));
		this.generatorMatrix.setRow(7, new BinaryWord("0001"));

		this.controlMatrix.setRow(1, new BinaryWord("1010101"));
		this.controlMatrix.setRow(2, new BinaryWord("0110011"));
		this.controlMatrix.setRow(3, new BinaryWord("0001111"));
	}

	@Override
	public BinaryWord decode(BinaryWord input) {
		BinaryWord s = this.controlMatrix.multiplyN(input);
		s.reverse();
		int syndrome = s.toDecimal();

		if (syndrome != 0) {
			// System.out.println("H(7,4,3): s = " + s);
			// System.out.println("H(7,4,3): syndrome = " + syndrome);
			// System.out.print("H(7,4,3): correct from " + input);
			input.toggleElement(syndrome);
			// System.out.println(" to " + input);
		}

		BinaryWord out = new BinaryWord(4);
		out.setElement(1, input.getElement(3));
		out.setElement(2, input.getElement(5));
		out.setElement(3, input.getElement(6));
		out.setElement(4, input.getElement(7));

		return out;
	}

	@Override
	public BinaryWord encode(BinaryWord input) {
		return this.generatorMatrix.multiplyN(input);
	}

	@Override
	public BinaryMatrix getGeneratorMatrix() {
		return this.generatorMatrix;
	}

	@Override
	public BinaryMatrix getControlMatrix() {
		return this.controlMatrix;
	}

	@Override
	public int getN() {
		return 7;
	}

	@Override
	public int getL() {
		return 4;
	}

	@Override
	public Channel getDecodeChannel() {
		return new AbstractDecodeChannel(this);
	}

	@Override
	public Channel getEncodeChannel() {
		return new AbstractEncodeChannel(this);
	}
}
