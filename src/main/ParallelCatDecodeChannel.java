package main;
import java.util.*;

public class ParallelCatDecodeChannel extends IntegerBufferChannel {

	private BlockCode code;
	private Modulator demodulator;
	private List<Float> elements;
	private int counter;
	private int iterations;

	public ParallelCatDecodeChannel(BlockCode code) {
		this.code = code;
		this.demodulator = new Modulator();
		this.elements = new LinkedList<Float>();
		this.counter = 0;
		this.iterations = 4;
	}

	public Modulator getDemodulator() {
		return this.demodulator;
	}
	
	private float getFactor(int element) {
		int paraK = this.code.getN() - this.code.getL();
		BinaryMatrix controlMatrix = this.code.getControlMatrix();
		// Hamming bit order : k1-k2-l1-k3-l2-l3-l4
		// Cat-Matrix bit order: l1-l2-l3-l4-k1-k2-k3
		int[] positions = { 3, 5, 6, 7, 1, 2, 4 };

		float factor = 0f;
		int divisor = 0;
		for (int f = 1; f <= paraK; f++) {
			int elem = controlMatrix.getElement(f, positions[element]);
			if (elem != 0) {
				divisor++;
			}
		}
		
		if (divisor != 0) {
			factor = 1f / divisor;
		} else {
			System.err.println("factor for intrinsic value is 0");
		}
		
		return factor;
	}

	public void decode() {
		int paraN = this.code.getN();
		int paraL = this.code.getL();
		int paraK = this.code.getN() - this.code.getL();
		BinaryMatrix controlMatrix = this.code.getControlMatrix();
		// Hamming bit order : k1-k2-l1-k3-l2-l3-l4
		// Cat-Matrix bit order: l1-l2-l3-l4-k1-k2-k3
		int[] positions = { 3, 5, 6, 7, 1, 2, 4 };

		float[][] extrinsicsHor = new float[paraL][paraL];
		float[][] extrinsicsVer = new float[paraL][paraL];
		for (int i = 0; i < paraL; i++) {
			for (int j = 0; j < paraL; j++) {
				extrinsicsVer[i][j] = 0.0f;
			}
		}

		/*
		 * For better understanding read: "Informations- und Kodierungstheorie" - pp. 254-259
		 * by Schönfeld Dagmar; Klimant Herbert; Piotraschke Rudi Wiesbaden: Springer Vieweg (2012) 
		 * ISBN 978-3-8348-0647-5
		 */
		for (int iter = 0; iter < this.iterations; iter++) {
			// horizontal step
			for (int row = 0; row < paraL; row++) { // for every row
				for (int rowE = 0; rowE < paraL; rowE++) { // for every row element

					// horizontal element
					float factor = this.getFactor(rowE);
					float extrinsicValue = 0f;

					for (int c = 1; c <= paraK; c++) { // for every control equation
						// check if current control equation covers the horizontal element
						boolean hasInfluence = controlMatrix.getElement(c, positions[rowE]) == 1;
						if (hasInfluence) {
							float partVal = Float.POSITIVE_INFINITY;
							int signum = 1;
							for (int u = 0; u < paraN; u++) {
								int h_element = controlMatrix.getElement(c, positions[u]);
								if ((u != rowE) && (h_element != 0)) {
									float compVal = 0f;
									if (u < paraL) {
										// get compVal for l1 .. l_end
										float exVerValue = extrinsicsVer[row][u];
										float infoValue = this.elements.get(row * paraL + u);
										compVal = infoValue + exVerValue;
									} else {
										// get compVal for k1 .. k_end
										int offset = paraL * paraL + row * paraK;
										compVal = this.elements.get(offset + (u - paraL));
									}

									if (compVal < 0) {
										signum *= -1;
									}
									partVal = signum * Math.min(Math.abs(partVal), Math.abs(compVal));
								}
							}
							extrinsicValue += factor * partVal;
						}
					}
					extrinsicsHor[row][rowE] = extrinsicValue;
				}
			}

			// vertical step
			for (int col = 0; col < paraL; col++) { // for every column
				for (int colE = 0; colE < paraL; colE++) { // for every column element

					// vertical element
					float factor = this.getFactor(colE);
					float extrinsicValue = 0f;

					for (int c = 1; c <= paraK; c++) { // for every control equation
						// check if current control equation covers the vertical element
						boolean hasInfluence = controlMatrix.getElement(c, positions[colE]) == 1;
						if (hasInfluence) {
							float partVal = Float.POSITIVE_INFINITY;
							int signum = 1;
							for (int u = 0; u < paraN; u++) {
								int h_element = controlMatrix.getElement(c, positions[u]);
								if ((u != colE) && (h_element != 0)) {
									float compVal = 0f;
									if (u < paraL) {
										// get compVal for l1 .. l_end
										float exHorValue = extrinsicsHor[u][col];
										float infoValue = this.elements.get(col + paraL * u);
										compVal = infoValue + exHorValue;
									} else {
										// get compVal for k1 .. k_end
										int offset = paraL * paraL + paraL * paraK + col * paraK;
										compVal = this.elements.get(offset + (u - paraL));
									}

									if (compVal < 0) {
										signum *= -1;
									}
									partVal = signum * Math.min(Math.abs(partVal), Math.abs(compVal));
								}
							}
							extrinsicValue += factor * partVal;
						}
					}
					extrinsicsVer[colE][col] = extrinsicValue;
				}
			}
		}

		for (int i = 0; i < paraL; i++) {
			for (int j = 0; j < paraL; j++) {
				float decodedValue = this.elements.remove(0);
				decodedValue += extrinsicsHor[i][j];
				decodedValue += extrinsicsVer[i][j];

				int bit = this.demodulator.softToHard(decodedValue);
				this.buffer.add(bit);
			}
		}
	}

	@Override
	public void pushInput(Integer bit) {
		int paraN = code.getN();
		int paraK = code.getN() - code.getL();
		int frameSize = (int) (Math.pow(paraN, 2) - Math.pow(paraK, 2));

		this.counter++;
		this.elements.add(this.demodulator.hardToSoft(bit));
		if (this.counter == frameSize) {
			this.counter = 0;
			decode();
		}
	}

	public void pushTestInput(Float bit) {
		int paraN = code.getN();
		int paraK = code.getN() - code.getL();
		int frameSize = (int) (Math.pow(paraN, 2) - Math.pow(paraK, 2));

		this.counter++;
		this.elements.add(bit);
		if (this.counter == frameSize) {
			decode();
		}
	}
}
