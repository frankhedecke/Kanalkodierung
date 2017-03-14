package channels;

import main.Modulator;
import entity.BinaryMatrix;
import entity.BinaryWord;
import blockcode.BlockCode;

public class WBF_DecodeChannel extends FloatInputChannel {

	private BlockCode code;
	private BinaryMatrix controlMatrix;
	private int max_iter;
	private int K;
	private int N;

	public WBF_DecodeChannel(BlockCode code, int max_iter) {
		super(code.getN());
		this.code = code;
		this.controlMatrix = code.getControlMatrix();
		this.K = this.code.getK();
		this.N = this.code.getN();
		this.max_iter = max_iter;
	}

	@Override
	public void process() {
		int iteration = 0;
		float y_soft[] = this.inputBuffer;
		// calculate weights
		float weights[] = new float[this.K];
		for (int i = 0; i < this.K; ++i) {
			float min = Float.MAX_VALUE;
			for(int j = 0; j < this.N; ++j) {
				if (this.controlMatrix.getElement(i + 1, j + 1) == 1)
					if(Math.abs(y_soft[j]) < min) min = Math.abs(y_soft[j]);
			}
			weights[i] = min;
		}
		// calculate y_hard for first iteration
		int y_h[] = new int[this.N];
		for (int i = 0; i < this.N; ++i) {
			y_h[i] = Modulator.softToHard(y_soft[i]);
		}

		while (iteration <= this.max_iter) {
			iteration ++;

			// calculate syndroms
			int syndroms[] = new int[this.K];
			for (int i = 0; i < this.K; ++i) {
				int sum = 0;
				for(int j = 0; j < this.N; ++j) {
					if (this.controlMatrix.getElement(i + 1, j + 1) == 1)
						sum += y_h[j];
					syndroms[i] = sum % 2;
				}
			}

			// check if syndrom = 0
			boolean syndromIsZero = true;
			for (int s : syndroms) {
				if (s == 1)
					syndromIsZero = false;
			}
			if (syndromIsZero) {
				// decode and output corrected word
				decode(y_h);
				return;
			} else {
				// do bit flipping

				// sum up errors
				float errors[] = new float[this.N];
				for (int i = 0; i < this.N; i++) {
					errors[i] = 0.0f;
					for (int j = 0; j < this.K; j++) {
						if (this.controlMatrix.getElement(j+1, i+1) == 1)
							errors[i] += (2.0f * syndroms[j] - 1) * weights[j];
					}
				}
				// identify flipping positions
				float max_error = 0.0f; // TODO rethink
				for (float e : errors)
					if (e > max_error) max_error = e;
				// flip positions
				for (int i = 0; i < this.N; i++) {
					if (errors[i] == max_error) {
						y_h[i] = flip(y_h[i]);
					}
				}
			}
		}
		// output current guess after max iterations
		decode(y_h);
	}

	private void decode(int[] word) {
		BinaryWord decoded = this.code.decode(new BinaryWord(word));

		for (int i = 1; i <= decoded.getLength(); i++)
			this.buffer.add(Modulator.hardToSoft(decoded.getElement(i)));
	}

	private int flip(int i) {
		if (i == 1)
			return 0;
		else
			return 1;
	}
}