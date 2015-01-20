public class Main {

	public static void main(String[] args) {

		HammingCode_4 code = new HammingCode_4();

		RandomSource random = new RandomSource(2, 1337l);

		Channel encodeChannel = code.getEncodeChannel();
		Channel decodeChannel = code.getDecodeChannel();

		System.out.println("input data");

		for (int i = 0; i < 50; i++) {
			if (random.hasOutput()) {
				int bit = random.getOutput();
				System.out.print(bit);
				encodeChannel.pushInput(bit);
			}
		}

		System.out.println("\n\nencoded data");

		while (encodeChannel.hasOutput()) {
			int bit = encodeChannel.getOutput();
			System.out.print(bit);
			decodeChannel.pushInput(bit);
		}

		System.out.println("\n\ndecoded data");

		while (decodeChannel.hasOutput()) {
			int bit = decodeChannel.getOutput();
			System.out.print(bit);
		}
	}
}
