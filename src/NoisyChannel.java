import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class NoisyChannel implements Channel {

	ErrorSource errors;
	private List<Integer> buffer;

	// do I need this constructor?
	public NoisyChannel(ErrorSource errors) {
		this.errors = errors;
		this.buffer = new LinkedList<Integer>();
	}

	public NoisyChannel(int errorRate, long seed) {
		this.errors = new ErrorSource(errorRate, seed);
		this.buffer = new LinkedList<Integer>();
	}

	@Override
	public boolean hasOutput() {
		return !buffer.isEmpty();
	}

	@Override
	public int getOutput() {
		int bit = buffer.remove(0);
		return bit;
	}

	@Override
	public void pushInput(int bit) {
		int blub = errors.getOutput() + bit;
		blub %= 2;
		buffer.add(blub);
	}

}
