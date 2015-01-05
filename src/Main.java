public class Main {

	public static void main(String[] args) {
		
		HammingCode_4 code = new HammingCode_4();
		
		BinaryWord input = new BinaryWord("1011");
		BinaryWord channel = code.encode(input);
		BinaryWord output;
		
		System.out.println("encode: " + input + " -> " + channel);

		System.out.print("1-bit error: " + channel + " -> ");
		channel.toggleElement(3);
		System.out.println(channel);
		
		output = code.decode(channel);
		
		System.out.println("decode: " + channel + " -> " + output);
	}
}
