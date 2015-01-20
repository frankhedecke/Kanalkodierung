
public abstract class AbstractSource implements Channel {

	@Override
	public boolean hasOutput() {
		return true;
	}

	@Override
	public void pushInput(int bit) {
		System.err.println("A source doesn't accept input.");
	}

}
