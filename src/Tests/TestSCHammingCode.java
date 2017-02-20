package Tests;

import main.Modulator;
import main.SerialConcatenation;
import blockcode.BlockCode;
import blockcode.HammingCode_4;
import channels.Channel;
import channels.ErrorSourceAWGN;
import channels.NoisyChannelAWGN;
import channels.RandomSource;

public class TestSCHammingCode {

	public static void main(String[] args) {

		BlockCode code = new HammingCode_4();
		
		SerialConcatenation scat = new SerialConcatenation(code, code);
		Channel<Integer> encoder = scat.getEncodeChannel();
		Channel<Integer> decoder = scat.getDecodeChannel();

		Channel<Integer> src = new RandomSource(2, 14337l);
		float snr_dB = 4.0f;
		ErrorSourceAWGN awgn = new ErrorSourceAWGN(scat.getCodeRate(), snr_dB, 56784l);
		Channel<Float> channel = new NoisyChannelAWGN(awgn);

		// write introduction
		System.out.println("serial concatenation of a (7,4,3) Hamming Code");
		System.out.println("code rate = " + scat.getCodeRate());
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
				int bit_hard = encoder.getOutput();
				float bit_soft = Modulator.hardToSoft(bit_hard);
				//System.out.format("%+02.2f ", bit_soft);
				System.out.format("%d ", bit_hard);
				channel.pushInput(bit_soft);
			}

			System.out.print("\n decoder input  = ");
			while(channel.hasOutput()) {
				int bit = Modulator.softToHard(channel.getOutput());
				System.out.format("%d ", bit);
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
