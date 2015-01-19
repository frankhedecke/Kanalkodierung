import java.util.Random;

public class RandomSource implements Channel {
	
	Random random;
	int modulo;
	
	public RandomSource(int modulo, long seed) {
		
		this.random = new Random(seed);
		this.modulo = modulo;
	}

	@Override
	public boolean hasOutput() {
		return true;
	}

	@Override
	public int getOutput() {
		return random.nextInt(this.modulo);
	}

	@Override
	public void pushInput(int bit) {
		System.err.println("\"RandomSource\" doesn't accept input.");
	}
}
