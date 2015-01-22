public class Main {

	public static void main(String[] args) {

		HammingCode_4 code = new HammingCode_4();

		RandomSource random = new RandomSource(2, 1337l);

		ErrorSource error = new ErrorSource(25, 1337l);

		Channel encode1Channel = code.getEncodeChannel();
		Channel encode2Channel = code.getEncodeChannel();
		Channel noisyChannel = new NoisyChannel(error);
		Channel decode1Channel = code.getDecodeChannel();
		Channel decode2Channel = code.getDecodeChannel();

		System.out.print("input data :     ");
		for (int i = 0; i < 40; i++) {
			if (random.hasOutput()) {
				int bit = random.getOutput();
				System.out.print(bit);
				encode1Channel.pushInput(bit);
			}
		}

		System.out.print("\n\nencoded data 1 : ");
		while (encode1Channel.hasOutput()) {
			int bit = encode1Channel.getOutput();
			System.out.print(bit);
			encode2Channel.pushInput(bit);
		}

		System.out.print("\n\nencoded data 2 : ");
		while (encode2Channel.hasOutput()) {
			int bit = encode2Channel.getOutput();
			System.out.print(bit);
			noisyChannel.pushInput(bit);
		}

		System.out.print("\n\nwith noise :     ");
		while (noisyChannel.hasOutput()) {
			int bit = noisyChannel.getOutput();
			System.out.print(bit);
			decode1Channel.pushInput(bit);
		}

		System.out.print("\n\ndecoded data 1 : ");
		while (decode1Channel.hasOutput()) {
			int bit = decode1Channel.getOutput();
			System.out.print(bit);
			decode2Channel.pushInput(bit);
		}

		System.out.print("\n\ndecoded data 2 : ");
		while (decode2Channel.hasOutput()) {
			int bit = decode2Channel.getOutput();
			System.out.print(bit);
		}
	}
}
