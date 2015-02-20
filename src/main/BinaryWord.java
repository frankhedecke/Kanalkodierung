package main;
public class BinaryWord {

	private int[] content;

	/**
	 * Constructs an empty BinaryWord for a given length
	 * 
	 * @param length
	 *            of the BinaryWord
	 */
	public BinaryWord(int length) {
		content = new int[length];
	}

	// TODO documentation parameter
	/**
	 * Constructs a BinaryWord for a given array
	 * 
	 * @param input
	 */
	public BinaryWord(int[] input) {
		this.content = input;
	}

	// TODO documentation parameter
	/**
	 * Constructs a BinaryWord for a given string
	 * 
	 * @param input
	 */
	public BinaryWord(String input) {

		this(input.length());

		for (int i = 0; i < input.length(); i++) {
			content[i] = new Integer("" + input.charAt(i));
		}
	}

	/**
	 * Get an array representation
	 * 
	 * @return array representation of the BinaryWord
	 */
	public int[] toArray() {
		return content;
	}

	// TODO make a static addition method

	// TODO do XOR with Integer and not String
	/**
	 * Computes the sum (XOR) of this and a given BinaryWord
	 * 
	 * @param suplement
	 *            of the addition
	 * @return sum of this and the given word
	 */
	public BinaryWord add(BinaryWord suplement) {
		if (this.getLength() != suplement.getLength()) {
			// TODO
			System.err.println("MISSMATCH");

			System.err
					.println("You try to xor a " + this.getLength()
							+ " bit word with a " + suplement.getLength()
							+ " bit word");
		}

		int x1 = Integer.parseInt(this.toString(), 2);
		int x2 = Integer.parseInt(suplement.toString(), 2);
		String sum = Integer.toBinaryString(x1 ^ x2); // sum may have no leading
														// zeros

		while (sum.length() != this.getLength()) {
			sum = "0" + sum;
		}

		return new BinaryWord(sum);
	}

	/**
	 * Computes the Hamming distance between this and a given BinaryWord
	 * 
	 * @param word
	 *            to compare to
	 * @return Hamming distance between the compared words
	 */
	public int hammingDistance(BinaryWord word) {
		if (word.getLength() != this.getLength()) {
			System.err.println("can not comput the hamming distance between a "
					+ word.getLength() + " bit word and a " + this.getLength()
					+ " bit word");
			return -1;
		}

		int distance = 0;
		for (int i = 1; i <= this.getLength(); i++) {
			if (word.getElement(i) != this.getElement(i))
				distance++;
		}

		return distance;
	}
	
	/**
	 * reverse the BinaryWord
	 *   (001110) -> (011100)
	 */
	public void reverse() {
		
		int length = this.getLength();
		int[] newContent = new int[length];
		
		
		for (int i = 0; i < length; i++) {
			newContent[i] = content[length - i - 1];
		}
		
		this.content = newContent;
	}

	/**
	 * Checks if the BinaryWord is the NullWord
	 * 
	 * @return true if the BinaryWord is the NullWord - false otherwise
	 */
	public boolean isNullWord() {
		if (this.toDecimal() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the length of the BinaryWord
	 * 
	 * @return length of the BinaryWord
	 */
	public int getLength() {
		return this.content.length;
	}

	/**
	 * Get a specified element of the BinaryWord
	 * 
	 * @param number
	 *            to specify the element
	 * @return element of the BinaryWord
	 */
	public int getElement(int number) {
		return this.content[number - 1];
	}

	/**
	 * Overwrites a specified element with a new value
	 * 
	 * @param number
	 *            to specify element
	 * @param newValue
	 *            of the specified element
	 */
	public void setElement(int number, int newValue) {
		this.content[number - 1] = newValue;
	}

	/**
	 * Toggles the specified bit
	 * 
	 * @param number
	 *            of the bit
	 */
	public void toggleElement(int number) {
		if (this.content[number - 1] == 1) {
			this.content[number - 1] = 0;
		} else {
			this.content[number - 1] = 1;
		}
	}

	/**
	 * Converts to int (decimal)
	 * 
	 * @return decimal int representation of the BinaryWord
	 */
	public int toDecimal() {
		return Integer.parseInt(this.toString(), 2);
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
