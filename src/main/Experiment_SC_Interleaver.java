package main;

import blockcode.BlockCode;
import blockcode.HammingCode_4;
import channels.Channel;
import channels.CompareChannel;
import channels.ErrorSourceAWGN;
import channels.NoisyChannelAWGN;
import channels.RandomSource;

public class Experiment_SC_Interleaver {

	public static void main(String[] args) {

		BlockCode code = new HammingCode_4();

		SerialConcatenation scat = new SerialConcatenation(code, code);
		Channel<Integer> encoder = scat.createEncodeChannelWithInterleaver();
		Channel<Integer> decoder = scat.createDecodeChannelWithInterleaver();
		CompareChannel comparator = new CompareChannel();

		float snr_start =  1.0f;
		float snr_end   = 10.0f;
		float snr_step  =  0.1f;
		int   cycles    = 10000;
		int   chunk_size =   16;

		System.out.println("serial concatenation of a (7,4,3) Hamming Code and a (7,4,3) Hamming Code");
		System.out.println("-> code rate = " + scat.getCodeRate());
		System.out.print("noisy channel with SNR from " + snr_start + " dB to ");
		System.out.println( snr_end + " dB in " + snr_step + " dB steps");
		System.out.println("4X7 BlockInterleaver");
		System.out.println("hard decision decoding");
		System.out.println();

		//System.out.println("TIME: " + System.nanoTime());
		for (float snr = snr_start; snr <= snr_end + 0.01; snr += snr_step) {
			System.out.format("SNR -- %4.1f -- ", snr);

			// setup source channel
			Channel<Integer> src = new RandomSource(2, 14337l);
			// setup channel noisy channel
			ErrorSourceAWGN awgn = new ErrorSourceAWGN(scat.getCodeRate(), snr, 56784l);
			Channel<Float> channel = new NoisyChannelAWGN(awgn);
			// setup counters
			int error_count = 0;
			int success_count = 0;

			// modify cycles
			if (snr > 8.0) {
				cycles =  50000;
			}

			for (int cycle = 0; cycle < cycles ; ++cycle) {

				// get generated input
				for (int i = 0; i < chunk_size; i++) {
					int bit = src.getOutput();
					encoder.pushInput(bit);
					comparator.pushInput(bit);
				}

				// get encoded input
				while(encoder.hasOutput()) {
					float bit = Modulator.hardToSoft(encoder.getOutput());
					channel.pushInput(bit);
				}

				// get channel output wih noise
				while(channel.hasOutput()) {
					int bit = Modulator.softToHard(channel.getOutput());
					decoder.pushInput(bit);
				}

				// decoding
				while(decoder.hasOutput()) {
					int bit = decoder.getOutput();
					comparator.pushComparandum(bit);
				}

				// compare input and output
				if (comparator.compare()) {
					success_count++;
				} else {
					error_count++;
				}
			}

			// System.out.print("TIME: " + System.nanoTime() + " --");
			System.out.format(" ok -- %5d --  and fails -- %5d -- cycles -- %8d", success_count, error_count, cycles);
			System.out.format(" -- WER -- %.4f", 1.0f * error_count / cycles);
			System.out.println();
		}
	}
}
