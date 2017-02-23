package Tests;

import channels.Channel;
import main.BlockInterleaver;

public class TestBlockInterleaver {

	public static void main(String[] args) {

		Channel<Integer> interleaver = new BlockInterleaver(4,7);
		Channel<Integer> deinterleaver = new BlockInterleaver(7,4);

		for (int i = 0; i < 60; i++) {

			interleaver.pushInput(i);
		}

		while (interleaver.hasOutput()) {
			int bit = interleaver.getOutput();
			System.out.print(bit + " ");
			deinterleaver.pushInput(bit);
		}

		System.out.println();
		while (deinterleaver.hasOutput()) {
			int bit = deinterleaver.getOutput();
			System.out.print(bit + " ");
		}
	}
}
