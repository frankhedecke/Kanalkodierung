package main;

import channels.Channel;
import channels.SC_DecodeChannel;
import channels.SC_EncodeChannel;
import channels.SC_InterDecodeChannel;
import channels.SC_InterEncodeChannel;
import channels.SC_IterDecodeChannel;
import channels.SC_IterEncodeChannel;
import entity.BinaryMatrix;
import entity.BinaryWord;
import blockcode.AbstractBlockCode;
import blockcode.BlockCode;

public class SerialConcatenation extends AbstractBlockCode {

	private BlockCode outerCode;
	private BlockCode innerCode;
	private BlockCode code;
	private int parameterK;
	private int parameterN;
	private int parameterL;
	public BinaryMatrix helper1;
	public BinaryMatrix helper2;
	public BinaryMatrix helper_p;

	public float getCodeRate() {
		return (float)this.parameterL / this.parameterN;
	}

	public SerialConcatenation(BlockCode outerCode, BlockCode innerCode) {
		this.outerCode = outerCode;
		this.innerCode = innerCode;
		this.code = outerCode; // TODO fix this
		this.parameterN = outerCode.getN() * innerCode.getN();
		this.parameterL = outerCode.getL() * innerCode.getL();
		this.parameterK = this.parameterN - this.parameterL;
		this.generatorMatrix = new BinaryMatrix(this.parameterN, this.parameterL);
		this.controlMatrix = new BinaryMatrix(this.parameterK, this.parameterN);
		constructG();
		constructH();
	}

	private void constructG() {
		// write identity matrix
		for (int i = 1; i <= this.parameterL; ++i)
			this.generatorMatrix.setElement(i, i, 1);
		// calculate C
		BinaryMatrix G = code.getGeneratorMatrix();
		int[] bitOrder = code.getBitOrder();
		int[][] C_src = new int[this.code.getK()][this.code.getL()];
		for (int m = 0; m < this.code.getK(); ++m) {
			int pos_m = bitOrder[this.code.getL() + m];
			for (int n = 0; n < this.code.getL(); ++n) {
				C_src[m][n] = G.getElement(pos_m, n + 1);
			}
		}

		// create helper matrix
		int helper_m = this.code.getK() * this.code.getK();
		int helper_n = this.code.getK() * this.code.getL();
		this.helper1 = new BinaryMatrix(helper_m, helper_n);

		// write helper matrix
		for(int hor_step = 0; hor_step < this.code.getK(); ++hor_step) {
			for (int m = 0; m < this.code.getK(); ++m) {
				int m_start = 0;
				int m_offset = hor_step * this.code.getK() + m;
				int pos_m = m_start + m_offset + 1;
				for (int n = 0; n < this.code.getL(); ++n) {
					int value = C_src[m][n];
					int pos_n = hor_step * this.code.getL() + n + 1;
					this.helper1.setElement(pos_m, pos_n, value);
				}
			}
		}
		// create helper2 matrix
		int helper_m2 = this.code.getK() * this.code.getL();
		int helper_n2 = this.code.getL() * this.code.getL();
		this.helper2 = new BinaryMatrix(helper_m2, helper_n2);

		// write helper2 matrix
		for(int hor_step = 0; hor_step < this.code.getL(); ++hor_step) {
			for (int m = 0; m < this.code.getK(); ++m) {
				int m_start = 0;
				int m_offset = hor_step * this.code.getK() + m;
				int pos_m = m_start + m_offset + 1;
				for (int n = 0; n < this.code.getL(); ++n) {
					int value = C_src[m][n];
					int pos_n = hor_step * this.code.getL() + n + 1;
					this.helper2.setElement(pos_m, pos_n, value);
				}
			}
		}

		// horizontal steps
		for(int hor_step = 0; hor_step < this.code.getL(); ++hor_step) {
			for (int m = 0; m < this.code.getK(); ++m) {
				int m_start = this.parameterL;
				int m_offset = hor_step * this.code.getK() + m;
				int pos_m = m_start + m_offset + 1;
				for (int n = 0; n < this.code.getL(); ++n) {
					int value = C_src[m][n];
					int pos_n = hor_step * this.code.getL() + n + 1;
					this.generatorMatrix.setElement(pos_m, pos_n, value);
				}
			}
		}

		// vertical steps
		for(int ver_step = 0; ver_step < this.code.getL(); ++ver_step) {
			for (int m = 0; m < this.code.getK(); ++m) {
				int m_start = this.parameterL + this.code.getL() * this.code.getK();
				int m_offset = ver_step * this.code.getK() + m;
				int pos_m = m_start + m_offset + 1;
				for (int n = 0; n < this.code.getL(); ++n) {
					int value = C_src[m][n];
					int pos_n = ver_step + n * this.code.getL() + 1;
					this.generatorMatrix.setElement(pos_m, pos_n, value);
				}
			}
		}

		// write redundancy of redundancy part
		this.helper_p = this.helper1.multiply(this.helper2);
		int row_offset = this.generatorMatrix.getDimensionM() - this.code.getK() * this.code.getK();
		for (int i = 1; i <= this.code.getK() * this.code.getK(); ++i) {
			BinaryWord row = this.helper_p.getRow(i);
			this.generatorMatrix.setRow(row_offset + i, row);
		}
	}

	private void constructH() {
		int end_m = this.parameterK;
		int end_n = this.parameterL;
		for (int m = 1; m <= end_m; ++m) {
			for (int n = 1; n <= end_n; ++n) {
				int value = this.generatorMatrix.getElement(this.parameterL + m, n);
				this.controlMatrix.setElement(m, n, value);
			}
		}
		// write identity matrix
		for (int i = this.parameterL + 1; i <= this.parameterN; ++i)
			this.controlMatrix.setElement(i - this.parameterL, i, 1);
	}

	@Override
	public int getK() {
		return this.parameterK;
	}

	@Override
	public int getN() {
		return this.parameterN;
	}

	@Override
	public int getL() {
		return this.parameterL;
	}

	public Channel<Integer> createEncodeChannel() {
		return new SC_EncodeChannel(this.outerCode, this.innerCode);
	}

	public Channel<Integer> createEncodeChannelWithInterleaver() {
		return new SC_InterEncodeChannel(this.outerCode, this.innerCode);
	}

	public Channel<Integer> createEncodeChannelOrdered() {
		return new SC_IterEncodeChannel(this.outerCode, this.innerCode);
	}

	public Channel<Integer> createDecodeChannel() {
		return new SC_DecodeChannel(this.outerCode, this.innerCode);
	}

	public Channel<Integer> createDecodeChannelWithInterleaver() {
		return new SC_InterDecodeChannel(this.outerCode, this.innerCode);
	}

	public Channel<Float> createIterativeDecodeChannel() {
		return new SC_IterDecodeChannel(this.outerCode, this.innerCode);
	}

	@Override
	public BinaryWord decode(BinaryWord input) {
		// no ECC just encoding
		if (input.getLength() != this.parameterN)
			return null;

		BinaryWord decoded = new BinaryWord(this.parameterL);
		for (int i = 1; i <= this.parameterL; ++i) {
			decoded.setElement(i, input.getElement(i));
		}

		return decoded;
	}

	@Override
	public BinaryWord encode(BinaryWord input) {
		if (input.getLength() != this.parameterL)
			return null;
		return this.generatorMatrix.multiplyN(input);
	}

	@Override
	public BinaryWord getRedundancy(BinaryWord input) {
		if (input.getLength() != this.parameterN)
			return null;

		BinaryWord redundancy = new BinaryWord(this.parameterK);
		for (int i = 1; i <= this.parameterK; ++i) {
			redundancy.setElement(i, input.getElement(this.parameterL + i));
		}

		return redundancy;
	}

	@Override
	public int[] getBitOrder() {
		int[] positions = new int[this.parameterN];
		for (int i = 1; i <= this.parameterN; ++i)
			positions[i - 1] = i;
		return positions;
	}
}