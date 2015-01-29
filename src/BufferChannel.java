import java.util.LinkedList;
import java.util.List;

public abstract class BufferChannel<T> implements Channel<T> {

	protected List<T> buffer;

	public BufferChannel() {
		this.buffer = new LinkedList<T>();
	}

	@Override
	public boolean hasOutput() {
		return !buffer.isEmpty();
	}

	@Override
	public T getOutput() {
		T bit = buffer.remove(0);
		return bit;
	}
}
