
public abstract class AbstractSource implements Channel<Integer> {

	@Override
	public boolean hasOutput() {
		return true;
	}

	@Override
	public void pushInput(Integer bit) {
		System.err.println("A source doesn't accept input.");
	}

}
