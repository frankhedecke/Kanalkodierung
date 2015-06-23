package channels;
import java.util.*;

import main.BlockInterleaver;


import entity.BinaryWord;

import blockcode.BlockCode;

public class ParallelCatEncodeChannel extends IntegerBufferChannel {

	private BlockCode code;
	private BlockInterleaver interleaver;
	private List<Integer> inputBuffer;
	private List<Integer> redundancyBufferH;
	private List<Integer> redundancyBufferV;

	public ParallelCatEncodeChannel(BlockCode code) {
		this.code = code;
		this.interleaver = new BlockInterleaver(code.getL());
		this.inputBuffer = new LinkedList<Integer>();
		this.redundancyBufferH = new LinkedList<Integer>();
		this.redundancyBufferV = new LinkedList<Integer>();
	}

	@Override
	public void pushInput(Integer bit) {
		this.inputBuffer.add(bit);
		this.interleaver.pushInput(bit);

		// fill the redundancy buffers
		while (this.interleaver.hasOutput()) {
			int[] x_H = new int[4];
			int[] x_V = new int[4];
			for (int i = 0; i < 4; i++) {
				int bit_H = this.inputBuffer.remove(0);
				int bit_V = this.interleaver.getOutput();
				x_H[i] = bit_H;
				x_V[i] = bit_V;
				this.buffer.add(bit_H);
			}
			BinaryWord redundancy_H = this.code.getRedundancy(new BinaryWord(x_H));
			BinaryWord redundancy_V = this.code.getRedundancy(new BinaryWord(x_V));
			for (int i = 1; i < 4; i++) {
				this.redundancyBufferH.add(redundancy_H.getElement(i));
				this.redundancyBufferV.add(redundancy_V.getElement(i));
			}
		}
		
		// process the redundancy buffers
		if (this.redundancyBufferH.size() == 12) {
			while(! this.redundancyBufferH.isEmpty()) {
				this.buffer.add(this.redundancyBufferH.remove(0));
			}
			while(! this.redundancyBufferV.isEmpty()) {
				this.buffer.add(this.redundancyBufferV.remove(0));
			}
		}
	}
}