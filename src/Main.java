public class Main {

	public static void main(String[] args) {

		BinaryWord input = new BinaryWord("0011");

		System.out.println("distance: "
				+ input.hammingDistance(new BinaryWord("1011")));
		System.out.println();

		String[] hamming7x4 = { "0001101", "0011001", "0101010", "1000011" };

		BinaryMatrix g = new BinaryMatrix(hamming7x4);

		g.print();

		BinaryWord output = g.multiplyM(input);

		System.out.println(output);
		System.out.println();

		g.transpose();
		g.print();

		BinaryWord o2 = g.multiplyM(new BinaryWord("1101001"));
		System.out.println(o2);
	}
}
