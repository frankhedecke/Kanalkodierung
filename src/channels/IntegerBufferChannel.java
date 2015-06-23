package channels;
import java.util.LinkedList;
import java.util.List;

public abstract class IntegerBufferChannel implements Channel<Integer> {

	protected List<Integer> buffer;

	public IntegerBufferChannel() {
		this.buffer = new LinkedList<Integer>();
	}

	@Override
	public boolean hasOutput() {
		return !buffer.isEmpty();
	}

	@Override
	public Integer getOutput() {
		Integer bit = buffer.remove(0);
		return bit;
	}
}
