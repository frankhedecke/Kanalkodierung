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
	
	public void decode() {
		System.out.println("\ndecode test");
		int paraN = code.getN();
		int paraL = code.getL();
		int paraK = code.getN() - code.getL();
		
		float[][] extrinsicsH = new float[paraL][paraL];
		float[][] extrinsicsV = new float[paraL][paraL];
		for (int i = 0; i< paraL; i++) {
			for (int j = 0; j < paraL; j++) {
				extrinsicsV[i][j] = 0.0f;
			}
		}
		
		/* 
		 * For better understanding read:
		 * "Informations- und Kodierungstheorie" - pp. 254-259
		 * by Schönfeld Dagmar; Klimant Herbert; Piotraschke Rudi
		 * Wiesbaden: Springer Vieweg (2012)
		 * ISBN 978-3-8348-0647-5
		 */
		for (int iter = 0; iter < this.iterations; iter++) {
			// horizontal step
			for (int i = 0; i< paraL; i++) { // for every row
				for (int j = 0; j < paraL; j++) { // for every column
					// horizontal element
					float exVal = Float.POSITIVE_INFINITY;
					int signum = 1;
					for (int u = 0; u < paraN; u++) {
						if (u != j) {
							float compVal = 0f;
							if (u < paraL) {
								// get compVal for l1 .. l_end
								compVal = this.elements.get(i * paraL + u) + extrinsicsV[i][u];
							} else {
								// get compVal for k1 .. k_end
								int offset = paraL * paraL + i * paraK; 
								compVal = this.elements.get(offset + (u - paraL));
							}
							
							if (compVal < 0) {
								signum *= -1;
							}
							exVal = signum * Math.min(Math.abs(exVal), Math.abs(compVal));
						}
					}
					extrinsicsH[i][j] = exVal;
					// System.out.println("H = " + exVal); 
				}
			}
			
			// vertical step
			for (int j = 0; j< paraL; j++) { // for every column
				for (int i = 0; i < paraL; i++) { // for every row
					// horizontal element
					float exVal = Float.POSITIVE_INFINITY;
					int signum = 1;
					for (int u = 0; u < paraN; u++) {
						if (u != i) {
							float compVal = 0f;
							if (u < paraL) {
								// get compVal for l1 .. l_end
								float exHValue = extrinsicsH[u][j];
								float infoValue = this.elements.get(j + paraL * u);
								compVal = infoValue + exHValue;
							} else {
								// get compVal for k1 .. k_end
								int offset = paraL * paraL + paraL * paraK + j * paraK;
								float infoValue = this.elements.get(offset + (u - paraL));
								compVal = infoValue;
							}
							
							if (compVal < 0) {
								signum *= -1;
							}
							exVal = signum * Math.min(Math.abs(exVal), Math.abs(compVal));
						}
					} 
					extrinsicsV[i][j] = exVal;
					// System.out.println("V = " + exVal); 
				}
			}	
		}
		
		for (int i = 0; i < paraL; i++) {
			for (int j = 0; j < paraL; j++) {
				float decodedValue = this.elements.remove(0);
				decodedValue += extrinsicsH[i][j];
				decodedValue += extrinsicsV[i][j];
				System.out.println("= " + decodedValue);
				
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
