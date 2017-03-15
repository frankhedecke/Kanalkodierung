package main;

import blockcode.AbstractBlockCode;
import blockcode.BlockCode;
import channels.BF_DecodeChannel;
import channels.Channel;
import channels.PC_DecodeChannelNoECC;
import channels.PC_HardIterDecodeChannel;
import channels.PC_IterDecodeChannel;
import channels.PC_EncodeChannel;
import channels.WBF_DecodeChannel;
import entity.BinaryMatrix;
import entity.BinaryWord;

public class ParallelConcatenation extends AbstractBlockCode {

	private BlockCode code;
	private int parameterK;
	private int parameterN;
	private int parameterL;

	public ParallelConcatenation(BlockCode code) {
		this.code = code;
		this.parameterK = 2 * code.getL() * code.getK();
		this.parameterL = code.getL() * code.getL();
		this.parameterN = code.getL() * code.getL() + this.parameterK;
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
	}

	private void constructH() {
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

		// horizontal steps
		for(int hor_step = 0; hor_step < this.code.getL(); ++hor_step) {
			for (int m = 0; m < this.code.getK(); ++m) {
				int m_start = 0;
				int m_offset = hor_step * this.code.getK() + m;
				int pos_m = m_start + m_offset + 1;
				for (int n = 0; n < this.code.getL(); ++n) {
					int value = C_src[m][n];
					int pos_n = hor_step * this.code.getL() + n + 1;
					this.controlMatrix.setElement(pos_m, pos_n, value);
				}
			}
		}

		// vertical steps
		for(int ver_step = 0; ver_step < this.code.getL(); ++ver_step) {
			for (int m = 0; m < this.code.getK(); ++m) {
				int m_start = this.code.getL() * this.code.getK();
				int m_offset = ver_step * this.code.getK() + m;
				int pos_m = m_start + m_offset + 1;
				for (int n = 0; n < this.code.getL(); ++n) {
					int value = C_src[m][n];
					int pos_n = ver_step + n * this.code.getL() + 1;
					this.controlMatrix.setElement(pos_m, pos_n, value);
				}
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

	public BinaryMatrix getGeneratorMatrix() {
		return this.generatorMatrix;
	}

	public BinaryMatrix getControlMatrix() {
		return this.controlMatrix;
	}

	@Override
	public int[] getBitOrder() {
		int[] positions = new int[this.parameterN];
		for (int i = 1; i <= this.parameterN; ++i)
			positions[i] = i;
		return positions;
	}

	public float getCodeRate() {
		return (float)this.parameterL / this.parameterN;
	}

	public Channel<Integer> getHardDecodeChannel() {
		return new PC_HardIterDecodeChannel(this.code);
	}

	public Channel<Integer> getDecodeChannelNoECC() {
		return new PC_DecodeChannelNoECC(this.code);
	}

	public Channel<Float> getSoftDecodeChannel() {
		return new PC_IterDecodeChannel(this.code);
	}

	public Channel<Integer> createBFDecodeChannel(int max_iter) {
		return new BF_DecodeChannel(this, max_iter);
	}

	public Channel<Float> createWBFDecodeChannel(int max_iter) {
		return new WBF_DecodeChannel(this, max_iter);
	}

	public Channel<Integer> createPCEncodeChannel() {
		return new PC_EncodeChannel(code);
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
}