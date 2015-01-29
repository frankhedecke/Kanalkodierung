public class BlockInterleaver extends AbstractBufferChannel {

	private int[][] inputBuffer;
	private int ptrX;
	private int ptrY;
	private int depth;

	public BlockInterleaver(int depth) {
		this.inputBuffer = new int[depth][depth];
		this.depth = depth;
		this.ptrX = 0;
		this.ptrY = 0;
	}

	@Override
	public void pushInput(Integer bit) {
		this.inputBuffer[this.ptrX][this.ptrY] = bit;
		this.ptrY++;
		if (this.ptrY >= this.depth) {
			this.ptrY = 0;
			this.ptrX++;
		}

		if (this.ptrX >= this.depth) {
			this.ptrX = 0;

			for (int i = 0; i < this.depth; i++) {
				for (int j = 0; j < this.depth; j++) {
					this.buffer.add(this.inputBuffer[j][i]);
				}
			}
		}
	}
}
