
public class ParallelDemodulator extends BufferChannel<Integer, Float> {

	@Override
	public void pushInput(Integer bit) {
		if (bit == 0) {
			this.buffer.add(1.0f);
		} else if (bit == 1) {
			this.buffer.add(-1.0f);
		} else {
			System.err.println("ParallelCatDecodeChannel works only with binday input.");
		}
	}
}
