package main;

import channels.Channel;
import channels.ErrorSource;
import channels.NoisyChannel;
import channels.RandomSource;
import blockcode.HammingCode_4;

public class Main {

	// this is not the main program
	// this is just a test bed
	public static void main(String[] args) {

		HammingCode_4 code = new HammingCode_4();
		SerialConcatenation cat = new SerialConcatenation(code, code);
		RandomSource random = new RandomSource(2, 1337l);
		ErrorSource error = new ErrorSource(25, 1337l);

		Channel<Integer> encodeChannel = cat.createEncodeChannel();
		Channel<Integer> decodeChannel = cat.createDecodeChannel();
		Channel<Integer> noisyChannel = new NoisyChannel(error);

		System.out.print("input data :     ");
		for (int i = 0; i < 16; i++) {
			if (random.hasOutput()) {
				int bit = random.getOutput();
				System.out.print(bit);
				encodeChannel.pushInput(bit);
			}
		}

		System.out.print("\n\nencoded data :   ");
		while (encodeChannel.hasOutput()) {
			int bit = encodeChannel.getOutput();
			System.out.print(bit);
			noisyChannel.pushInput(bit);
		}

		System.out.print("\n\nwith noise :     ");
		while (noisyChannel.hasOutput()) {
			int bit = noisyChannel.getOutput();
			System.out.print(bit);
			decodeChannel.pushInput(bit);
		}

		System.out.print("\n\ndecoded data :   ");
		while (decodeChannel.hasOutput()) {
			int bit = decodeChannel.getOutput();
			System.out.print(bit);
		}
	}
}
