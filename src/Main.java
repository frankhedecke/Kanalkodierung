
public class Main {

	public static void main(String[] args) {
		
		BinaryWord input = new BinaryWord("0011");
		
		String[] hamming7x4 = {"0001101", "0011001", "0101010", "1000011"};
		
		BinaryMatrix g_transposed = new BinaryMatrix(hamming7x4); 
		
		BinaryWord output = g_transposed.multiplyM(input);

		System.out.println(output);
	}
}
