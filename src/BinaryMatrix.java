public class BinaryMatrix {

	private int[][] content;

	public BinaryMatrix(int dimension_m, int dimension_n) {
		this.content = new int[dimension_m][dimension_n];
	}

	public BinaryMatrix(String[] rows) {
		this.content = new int[rows.length][];
		for (int i = 0; i < rows.length; i++) {
			BinaryWord word = new BinaryWord(rows[i]);
			this.content[i] = word.toArray();
		}
	}

	// TODO implement
	// TODO adding is limited to 32 bit
	public BinaryWord multiplyM(BinaryWord x) {
		if (x.getDimension() != this.getDimensionM()) {
			// TODO
			System.err.println("MISSMATCH");

			System.err.println("You try to multiply a " + x.getDimension()
					+ " bit word with a " + this.getDimensionM() + "X"
					+ this.getDimensionN() + " Matrix.");
		}

		// matrix X vector
		BinaryWord out = new BinaryWord(this.getDimensionN());
		for (int i = 1; i <= this.getDimensionM(); i++) {
			if (x.getElement(i) == 1) {
				out = out.add(this.getRow(i));
			}
		}
		return out;
	}

	/**
	 * transpose the m X n matrix in a n X m matrix
	 */
	public void transpose() {

		int[][] newMatrix = new int[this.getDimensionN()][this.getDimensionM()];
		for (int i = 0; i < this.getDimensionM(); i++) {
			for (int j = 0; j < this.getDimensionN(); j++) {
				newMatrix[j][i] = this.content[i][j];
			}
		}
		this.content = newMatrix;
	}

	public int getElement(int row, int column) {
		return this.content[row - 1][column - 1];
	}

	public void setElement(int row, int column, int value) {
		this.content[row - 1][column - 1] = value;
	}

	public BinaryWord getRow(int rowNumber) {
		return new BinaryWord(this.content[rowNumber - 1]);
	}

	public void setRow(int rowNumber, int[] row) {
		this.content[rowNumber - 1] = row;
	}

	public void setRow(int rowNumber, BinaryWord row) {
		this.content[rowNumber - 1] = row.toArray();
	}

	/**
	 * @return number of rows
	 */
	public int getDimensionM() {
		return content.length;
	}

	/**
	 * @return number of columns
	 */
	public int getDimensionN() {
		return content[0].length;
	}

	public void print() {
		for (int i = 0; i < this.content.length; i++) {
			for (int j = 0; j < this.content[i].length; j++) {
				System.out.print(this.content[i][j]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
