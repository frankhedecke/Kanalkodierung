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
	
	//TODO do XOR with Integer and not String
	public BinaryWord add(BinaryWord suplement) {
		if(this.getDimension() != suplement.getDimension()) {
			// TODO
			System.err.println("MISSMATCH");

			System.err.println("You try to xor a " + this.getDimension()
					+ " bit word with a " + suplement.getDimension() + " bit word");
		}
		
		int x1 = Integer.parseInt(this.toString(), 2);
		int x2 = Integer.parseInt(suplement.toString(), 2);
		String sum = Integer.toBinaryString(x1 ^ x2); // sum may have no leading zeros
		
		while (sum.length() != this.getDimension()) {
			sum = "0" + sum;
		}
		
		return new BinaryWord(sum);
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
