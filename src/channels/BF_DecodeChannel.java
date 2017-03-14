package channels;

import entity.BinaryMatrix;
import entity.BinaryWord;
import blockcode.BlockCode;

public class BF_DecodeChannel extends IntegerInputChannel {

	private BlockCode code;
	private BinaryMatrix controlMatrix;
	private int max_iter;
	private int K;
	private int N;

	public BF_DecodeChannel(BlockCode code, int max_iter) {
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
		int y[] = this.inputBuffer;
		while (iteration <= this.max_iter) {
			iteration ++;
			// calculate syndroms
			int syndroms[] = new int[this.K];
			for (int i = 0; i < this.K; ++i) {
				int sum = 0;
				for(int j = 0; j < this.N; ++j) {
					if (this.controlMatrix.getElement(i + 1, j + 1) == 1)
						sum += y[j];
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
				decode(y);
				return;
			} else {
				// do bit flipping

				// sum up errors
				int errors[] = new int[this.N];
				for (int i = 0; i < this.N; i++) {
					errors[i] = 0;
					for (int j = 0; j < this.K; j++) {
						if (this.controlMatrix.getElement(j+1, i+1) == 1)
							errors[i] += syndroms[j];
					}
				}
				// identify flipping positions
				int max_error = 1;
				for (int e : errors)
					if (e > max_error) max_error = e;
				// flip positions
				for (int i = 0; i < this.N; i++) {
					if (errors[i] == max_error) {
						y[i] = flip(y[i]);
					}
				}
			}
		}
		// output current guess after max iterations
		decode(y);
	}

	private void decode(int[] word) {
		BinaryWord decoded = this.code.decode(new BinaryWord(word));

		for (int i = 1; i <= decoded.getLength(); i++)
			this.buffer.add(decoded.getElement(i));
	}

	private int flip(int i) {
		if (i == 1)
			return 0;
		else
			return 1;
	}
}