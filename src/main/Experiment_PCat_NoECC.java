package main;

import blockcode.BlockCode;
import blockcode.HammingCode_4;
import channels.Channel;
import channels.CompareChannel;
import channels.ErrorSourceAWGN;
import channels.NoisyChannelAWGN;
import channels.RandomSource;

public class Experiment_PCat_NoECC {

	public static void main(String[] args) {

		BlockCode code = new HammingCode_4();
		
		ParallelConcatenation pcat = new ParallelConcatenation(code);
		Channel<Integer> encoder = pcat.getEncodeChannel();
		Channel<Integer> decoder = pcat.getDecodeChannelNoECC();
		CompareChannel compare = new CompareChannel();

		float snr_start =  1.0f;
		float snr_end   = 10.0f;
		float snr_step  =  0.1f;
		int cycles =  10000;

		// TODO NO ECC
		System.out.println("parallel concatenation of a (7,4,3) Hamming Code");
		System.out.println("-> code rate = " + pcat.getCodeRate());
		System.out.print("noisy channel with SNR from " + snr_start + " dB to ");
		System.out.println( snr_end + " dB in " + snr_step + " dB steps");
		System.out.println("simple decoding with no ECC");
		System.out.println();

		System.out.println("TIME: " + System.nanoTime());

		for (float snr = snr_start; snr <= snr_end + 0.01; snr += snr_step) {
			System.out.format("SNR = %4.1f -- ", snr);
			
			// setup source channel
			Channel<Integer> src = new RandomSource(2, 14337l);
			// setup channel noisy channel
			ErrorSourceAWGN awgn = new ErrorSourceAWGN(pcat.getCodeRate(), snr, 56784l);
			Channel<Float> channel = new NoisyChannelAWGN(awgn);
			// setup counters
			int error_count = 0;
			int success_count = 0;
			
			// modify cycles
			if (snr > 8.0) {
				cycles =  50000;
			}

			for (int cycle = 0; cycle < cycles ; ++cycle) {

				// TODO remove 16
				// generating inputs
				for (int i = 0; i < 16; i++) {
					int bit = src.getOutput();
					encoder.pushInput(bit);
					compare.pushInput(bit);
				}

				// encoding inputs
				while(encoder.hasOutput()) {
					float bit = Modulator.hardToSoft(encoder.getOutput());
					channel.pushInput(bit);
				}

				// adding noise
				while(channel.hasOutput()) {
					float bit = channel.getOutput();
					decoder.pushInput(Modulator.softToHard(bit));
				}

				// decoding
				while(decoder.hasOutput()) {
					int bit = decoder.getOutput();
					compare.pushComparandum(bit);
				}

				// compare input and output
				if (compare.compare()) {
					success_count++;
				} else {
					error_count++;
				}
			}
			System.out.format("ok -- %5d --  and fails -- %5d -- cycles -- %8d", success_count, error_count, cycles);
			System.out.println();
			System.out.println("TIME: " + System.nanoTime());
		}
	}
}
