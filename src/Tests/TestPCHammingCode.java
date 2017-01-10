package Tests;

import channels.Channel;
import channels.ErrorSourceAWGN;
import channels.NoisyChannelAWGN;
import channels.RandomSource;
import blockcode.*;
import main.*;

public class TestPCHammingCode {

	public static void main(String[] args) {

		BlockCode code = new HammingCode_4();
		
		ParallelConcatenation pcat = new ParallelConcatenation(code);
		Channel<Integer> encoder = pcat.getEncodeChannel();
		Channel<Float> decoder = pcat.getSoftDecodeChannel();

		Channel<Integer> src = new RandomSource(2, 14337l);
		float snr_dB = 4.0f;
		ErrorSourceAWGN awgn = new ErrorSourceAWGN(pcat.getCodeRate(), snr_dB, 56784l);
		Channel<Float> channel = new NoisyChannelAWGN(awgn);

		// write introduction
		System.out.println("parallel concatenation of a (7,4,3) Hamming Code");
		System.out.println("code rate = " + pcat.getCodeRate());
		System.out.println("noisy channel with SNR " + snr_dB + " dB");
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
				float bit = Modulator.hardToSoft(encoder.getOutput());
				System.out.format("%+02.2f ", bit);
				channel.pushInput(bit);
			}

			System.out.print("\n decoder input  = ");
			while(channel.hasOutput()) {
				float bit = channel.getOutput();
				System.out.format("%+02.2f ", bit);
				decoder.pushInput(bit);
			}

			System.out.print("\n decoder output = ");
			while(decoder.hasOutput()) {
				int bit = Modulator.softToHard(decoder.getOutput());
				System.out.print(bit + " ");
			}

			System.out.println();
			System.out.println();
		}
	}
}
