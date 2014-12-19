public class BinaryWord {

	private int[] content;

	public BinaryWord(int dimension) {

		content = new int[dimension];
	}

	public int[] toArray() {
		return content;
	}

	public BinaryWord(int[] input) {
		this.content = input;
	}

	public BinaryWord(String input) {

		this(input.length());

		for (int i = 0; i < input.length(); i++) {
			content[i] = new Integer("" + input.charAt(i));
		}
	}

	// TODO do XOR with Integer and not String
	public BinaryWord add(BinaryWord suplement) {
		if (this.getDimension() != suplement.getDimension()) {
			// TODO
			System.err.println("MISSMATCH");

			System.err.println("You try to xor a " + this.getDimension()
					+ " bit word with a " + suplement.getDimension()
					+ " bit word");
		}

		int x1 = Integer.parseInt(this.toString(), 2);
		int x2 = Integer.parseInt(suplement.toString(), 2);
		String sum = Integer.toBinaryString(x1 ^ x2); // sum may have no leading
														// zeros

		while (sum.length() != this.getDimension()) {
			sum = "0" + sum;
		}

		return new BinaryWord(sum);
	}

	/**
	 * Compute the Hamming distance between this and a given binary word 
	 * @param Binary word to compare to
	 * @return Hamming distance between the compared words
	 */
	public int hammingDistance(BinaryWord x) {
		if (x.getDimension() != this.getDimension()) {
			System.err.println("can not comput the hamming distance between a "
					+ x.getDimension() + " bit word and a "
					+ this.getDimension() + " bit word");
			return -1;
		}
	
		int distance = 0;
		for (int i = 1; i <= this.getDimension(); i++) {
			if (x.getElement(i) != this.getElement(i))
				distance++;
		}

		return distance;
	}

	public int getDimension() {
		return this.content.length;
	}

	public int getElement(int number) {
		return this.content[number - 1];
	}

	public void setElement(int number, int value) {
		this.content[number - 1] = value;
	}

	@Override
	public String toString() {

		String representation = "";

		for (int i = 0; i < this.content.length; i++) {
			representation += this.content[i];
		}

		return representation;
	}

}
