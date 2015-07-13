package channels;

public abstract class AbstractSource<T> implements Channel<T> {

	@Override
	public boolean hasOutput() {
		return true;
	}

	@Override
	public void pushInput(T bit) {
		System.err.println("A source doesn't accept input.");
	}
	
	public abstract T getOutput();
}
