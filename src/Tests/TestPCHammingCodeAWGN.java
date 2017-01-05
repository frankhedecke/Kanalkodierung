package Tests;

import blockcode.BlockCode;
import blockcode.HammingCode_4a;
import channels.Channel;
import channels.ErrorSourceAWGN;
import channels.NoisyChannelAWGN;
import channels.RandomSource;
import main.Modulator;
import main.ParallelConcatenation;

public class TestPCHammingCodeAWGN {

	public static void main(String[] args) {

		BlockCode code = new HammingCode_4a();
		ParallelConcatenation pcat = new ParallelConcatenation(code);
		
		Channel<Integer> src = new RandomSource(2, 823649l);
		Channel<Integer> encoder = pcat.getEncodeChannel();
		Channel<Float> decoder = pcat.getSoftDecodeChannel();

		float codeRate = 0.5f;
		float snr_dB = 4.0f;

		ErrorSourceAWGN errorSource = new ErrorSourceAWGN(codeRate, snr_dB, 1337);
		Channel<Float> noisyChannel = new NoisyChannelAWGN(errorSource);

		System.out.println("RauschVarianz = " + errorSource.getVariance());

		System.out.print("  input = ");
		for (int i = 0; i < 16; i++) {
			int bit = src.getOutput();
			System.out.print(bit + " ");
			encoder.pushInput(bit);
		}
		
		System.out.print("\n without noise = ");
		while(encoder.hasOutput()) {
			float bit = Modulator.hardToSoft(encoder.getOutput());
			System.out.format("%2.3f ", bit);
			noisyChannel.pushInput(bit);
		}
		
		System.out.print("\n with noise    = ");
		while(noisyChannel.hasOutput()) {
			float bit = noisyChannel.getOutput();
			System.out.format("%2.3f ", bit);
			decoder.pushInput(bit);
		}

		//TODO Output or output order is somehow broken

		System.out.print("\n output = ");
		while(decoder.hasOutput()) {
			int bit = Modulator.softToHard(decoder.getOutput());
			System.out.print(bit + " ");
		}
	}
}