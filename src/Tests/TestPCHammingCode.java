package Tests;

import channels.Channel;
import channels.RandomSource;
import blockcode.*;
import main.*;

public class TestPCHammingCode {

	public static void main(String[] args) {

		BlockCode code = new HammingCode_4();
		
		ParallelConcatenation pcat = new ParallelConcatenation(code);
		
		Channel<Integer> src = new RandomSource(2, 14337l);

		Channel<Integer> encoder = pcat.getEncodeChannel();
		Channel<Integer> decoder = pcat.getHardDecodeChannel();

		// write introduction
		System.out.println("parallel concatenation of a (7,4,3) Hamming Code");
		System.out.println("no errors in the noisy channel");
		System.out.println();

		for (int repeat = 0; repeat < 10 ; ++repeat) {

//			decoder = pcat.getHardDecodeChannel();
			System.out.println("cycle = " + repeat);
			System.out.print(" encoder input  = ");
			for (int i = 0; i < 16; i++) {
				int bit = src.getOutput();
				System.out.print(bit + " ");
				encoder.pushInput(bit);
			}

			System.out.print("\n encoder output = ");
			while(encoder.hasOutput()) {
				int bit = encoder.getOutput();
				System.out.print(bit + " ");
				decoder.pushInput(bit);
			}

			System.out.print("\n decoder output = ");
			while(decoder.hasOutput()) {
				int bit = decoder.getOutput();
				System.out.print(bit + " ");
			}

			System.out.println();
			System.out.println();
		}
	}
}
