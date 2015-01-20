import java.util.Random;

public class RandomSource extends AbstractSource {
	
	Random random;
	int modulo;
	
	public RandomSource(int modulo, long seed) {
		
		if (modulo < 2) {
			System.err.println("modulo must be > 1; will use 2 instead");
			this.modulo = 2;
		} else {
			this.modulo = modulo;
		}
		
		this.random = new Random(seed);
	}

	@Override
	public int getOutput() {
		return random.nextInt(this.modulo);
	}
}
