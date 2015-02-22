package blockcode;

import main.Channel;
import main.DecodeChannel;
import main.EncodeChannel;
import entity.BinaryMatrix;
import entity.BinaryWord;

public class ParityCheckCode implements BlockCode {

	private int paraL;
	private int paraK;
	private int paraN;
	private BinaryMatrix generatorMatrix;
	private BinaryMatrix controlMatrix;

	public ParityCheckCode(int length) {
		assert (length > 0);

		this.paraL = length;
		this.paraK = 1;
		this.paraN = length + 1;

		this.generatorMatrix = new BinaryMatrix(this.paraN, this.paraL);
		this.controlMatrix = new BinaryMatrix(this.paraK, this.paraN);

		// fill the generator matrix
		for (int row = 1; row <= paraN; row++) {
			for (int col = 1; col <= paraL; col++) {
				this.generatorMatrix.setElement(row, col, 1);
			}
		}

		// fill the generator matrix
		for (int col = 1; col <= paraN; col++) {
			this.controlMatrix.setElement(1, col, 1);
		}
	}

	@Override
	public int[] getBitOrder() {
		// ParityCheckOrder : l1-...-ln-k1
		// Cat-Matrix bit order: l1-...-ln-k1

		int[] positions = new int[this.paraN];
		for (int i = 0; i < this.paraN; i++) {
			positions[i] = i+1;
		}
		return positions;
	}

	@Override
	public BinaryWord decode(BinaryWord input) {
		assert (input.getLength() == this.paraN) : "input word has not the correct length";

		BinaryWord output = new BinaryWord(input.getLength() - 1);

		for (int i = 1; i < input.getLength(); i++) {
			output.setElement(i, input.getElement(i));
		}

		return output;
	}

	@Override
	public BinaryWord encode(BinaryWord input) {
		assert (input.getLength() == this.paraL) : "input word has not the correct length";

		BinaryWord output = new BinaryWord(input.getLength() + 1);
		int parity = 0;

		for (int i = 1; i <= input.getLength(); i++) {
			int elem = input.getElement(i);
			output.setElement(i, elem);
			parity += elem;
		}

		parity %= 2;
		output.setElement(output.getLength(), parity);

		return output;
	}

	@Override
	public BinaryWord getRedundancy(BinaryWord input) {
		assert (input.getLength() == this.paraL) : "input word has not the correct length";

		BinaryWord encoded = this.encode(input);
		BinaryWord redundancy = new BinaryWord(1);
		redundancy.setElement(1, encoded.getElement(paraN));
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
		return this.paraL + 1;
	}

	@Override
	public int getL() {
		return this.paraL;
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
