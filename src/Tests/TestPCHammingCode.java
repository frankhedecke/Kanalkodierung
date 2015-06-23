package Tests;
import channels.Channel;
import channels.ParallelCatDecodeChannel;
import channels.RandomSource;
import blockcode.*;
import main.*;

public class TestPCHammingCode {

	public static void main(String[] args) {

		BlockCode code = new HammingCode_4();
		
		ParallelConcatenation pcat = new ParallelConcatenation(code);
		
		Channel<Integer> src = new RandomSource(2, 14337l);
		Channel<Integer> encoder = pcat.getEncodeChannel();
		ParallelCatDecodeChannel decoder = (ParallelCatDecodeChannel) pcat.getDecodeChannel();

		System.out.print("  input = ");
		for (int i = 0; i < 16; i++) {
			int bit = src.getOutput();
			System.out.print(bit + " ");
			encoder.pushInput(bit);
		}
		
		System.out.println();
		while(encoder.hasOutput()) {
			int bit = encoder.getOutput();
			System.out.print(bit + " ");
			decoder.pushInput(bit);
		}

		System.out.print("\n output = ");
		while(decoder.hasOutput()) {
			int bit = decoder.getOutput();
			System.out.print(bit + " ");
		}
		
		System.out.println();
	}
}
