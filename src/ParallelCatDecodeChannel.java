// TODO write decoder and output method
public class ParallelCatDecodeChannel extends IntegerBufferChannel {
	
	private ParallelDemodulator demodulator;
	
	public ParallelCatDecodeChannel() {
		this.demodulator = new ParallelDemodulator();
	}
	
	public ParallelDemodulator getDemodulator() {
		return this.demodulator;
	}

	@Override
	public void pushInput(Integer bit) {
		this.demodulator.pushInput(bit);
	}
}
