package main;

import channels.IntegerBufferChannel;

public class BlockInterleaver extends IntegerBufferChannel {

	private int[][] inputBuffer;
	private int ptrX;
	private int ptrY;
	private int depth;
	private int length;

	public BlockInterleaver(int depth, int length) {
		this.inputBuffer = new int[depth][length];
		this.depth = depth;
		this.length = length;
		this.ptrX = 0;
		this.ptrY = 0;
	}

	@Override
	public void pushInput(Integer bit) {
		this.inputBuffer[this.ptrX][this.ptrY] = bit;
		this.ptrY++;
		if (this.ptrY >= this.length) {
			this.ptrY = 0;
			this.ptrX++;
		}

		if (this.ptrX >= this.depth) {
			this.ptrX = 0;

			for (int i = 0; i < this.length; i++) {
				for (int j = 0; j < this.depth; j++) {
					this.buffer.add(this.inputBuffer[j][i]);
				}
			}
		}
	}
}
