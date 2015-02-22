package entity;


public class BinaryMatrix {

	private int[][] content;

	/**
	 * Constructs an empty M x N BinaryMatrix
	 * 
	 * @param dimensionM
	 *            of the matrix
	 * @param dimensionN
	 *            of the matrix
	 */
	public BinaryMatrix(int dimensionM, int dimensionN) {
		this.content = new int[dimensionM][dimensionN];
	}

	/**
	 * Constructs a BinaryMatrix for a given set of rows
	 * 
	 * @param rows
	 *            of the new BinaryMatrix
	 */
	public BinaryMatrix(String[] rows) {
		this.content = new int[rows.length][];
		for (int i = 0; i < rows.length; i++) {
			BinaryWord word = new BinaryWord(rows[i]);
			this.content[i] = word.toArray();
		}
	}

	/**
	 * Multiplies a M x N BinaryMatrix with a M-bit BinaryWord
	 * 
	 * @param x
	 *            a M-bit BinaryWord
	 * @return product (N-bit BinaryWord) from the multiplication
	 */
	public BinaryWord multiplyM(BinaryWord x) {
		if (x.getLength() != this.getDimensionM()) {
			System.err.println("MISSMATCH");

			System.err.println("You try to multiply a " + x.getLength()
					+ " bit word with a " + this.getDimensionM() + "X"
					+ this.getDimensionN() + " Matrix.");
		}

		BinaryWord out = new BinaryWord(this.getDimensionN());
		for (int i = 1; i <= this.getDimensionM(); i++) {
			if (x.getElement(i) == 1) {
				out = out.add(this.getRow(i));
			}
		}
		return out;
	}
	
	/**
	 * Multiplies a M x N BinaryMatrix with a M-bit BinaryWord
	 * 
	 * @param x
	 *            a N-bit BinaryWord
	 * @return product (M-bit BinaryWord) from the multiplication
	 */
	public BinaryWord multiplyN(BinaryWord x) {
		if (x.getLength() != this.getDimensionN()) {
			System.err.println("MISSMATCH");

			System.err.println("You try to multiply a " + x.getLength()
					+ " bit word with a " + this.getDimensionN() + "X"
					+ this.getDimensionN() + " Matrix.");
		}
		
		this.transpose();

		BinaryWord out = new BinaryWord(this.getDimensionN());
		for (int i = 1; i <= this.getDimensionM(); i++) {
			if (x.getElement(i) == 1) {
				out = out.add(this.getRow(i));
			}
		}
		
		this.transpose();
		return out;
	}

	/**
	 * Transposes the M x N BinaryMatrix into a N x M BinaryMatrix
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

	/**
	 * Get the element at the position (row, column)
	 * 
	 * @param row
	 *            of the specified element
	 * @param column
	 *            of the specified element
	 * @return element at the position (row, column)
	 */
	public int getElement(int row, int column) {
		return this.content[row - 1][column - 1];
	}

	/**
	 * Overwrites a specified element with a new value
	 * 
	 * @param row
	 *            to specify the element
	 * @param column
	 *            to specify the element
	 * @param newValue
	 *            of the specified element
	 */
	public void setElement(int row, int column, int newValue) {
		this.content[row - 1][column - 1] = newValue;
	}

	/**
	 * Returns a specified row
	 * 
	 * @param rowNumber
	 *            to specify the row
	 * @return the specified row of the BinaryMatrix
	 */
	public BinaryWord getRow(int rowNumber) {
		return new BinaryWord(this.content[rowNumber - 1]);
	}

	/**
	 * Overwrites a specified row
	 * 
	 * @param rowNumber
	 *            to specify the row
	 * @param newRow
	 *            of the BinaryMatrix
	 */
	public void setRow(int rowNumber, BinaryWord newRow) {
		this.content[rowNumber - 1] = newRow.toArray();
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

	/**
	 * Prints the matrix to the console
	 */
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