package blockcode;

import entity.BinaryMatrix;
import entity.BinaryWord;
import main.Channel;
import main.DecodeChannel;
import main.EncodeChannel;

public class HammingCode_4 implements BlockCode {

	private BinaryMatrix generatorMatrix;
	private BinaryMatrix controlMatrix;

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
			input.toggleElement(syndrome);
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
		// bit order : k1-k2-l1-k3-l2-l3-l4
		return this.generatorMatrix.multiplyN(input);
	}
	
	@Override
	public BinaryWord getRedundancy(BinaryWord input) {
		BinaryWord encoded = this.encode(input);

		BinaryWord redundancy = new BinaryWord(3);
		redundancy.setElement(1, encoded.getElement(1));
		redundancy.setElement(2, encoded.getElement(2));
		redundancy.setElement(3, encoded.getElement(4));
		return redundancy;
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
	public Channel<Integer> getDecodeChannel() {
		return new DecodeChannel(this);
	}

	@Override
	public Channel<Integer> getEncodeChannel() {
		return new EncodeChannel(this);
	}
}
