package main;

public class Modulator {
	
	public static float hardToSoft(int input) {
		if (input == 0) {
			return 1.0f;
		} else if (input == 1) {
			return -1.0f;
		} else {
			System.err.println("ParallelCatDecodeChannel works only with binary input.");
			return Float.NaN;
		}
	}
	
	public static int softToHard(float input) {
		if (input > 0f) {
			return 0;
		} else if (input < 0f) {
			return 1;
		} else {
			return -1;
		}
	}
}
