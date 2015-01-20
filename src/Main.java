public class Main {

	public static void main(String[] args) {

		HammingCode_4 code = new HammingCode_4();		

		RandomSource random = new RandomSource(2, 1337l);
		
		Channel codeChannel = code.getEncodeChannel();

		for (int i = 0; i < 48; i++) {
			if (random.hasOutput()) {
				int bit = random.getOutput();
				System.out.print(bit);
				codeChannel.pushInput(bit);
			}
		}
		
		System.out.println("\n");

		for (int i = 0; i < 120; i++) {
			if (codeChannel.hasOutput()) {
				int bit = codeChannel.getOutput();
				System.out.print(bit);
			} else {
				//System.out.print("X");
			}
		}
	}
}
