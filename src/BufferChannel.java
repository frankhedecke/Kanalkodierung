import java.util.LinkedList;
import java.util.List;

public abstract class BufferChannel<I, O> {

	protected List<O> buffer;

	public BufferChannel() {
		this.buffer = new LinkedList<O>();
	}

	public boolean hasOutput() {
		return !buffer.isEmpty();
	}

	public O getOutput() {
		O bit = buffer.remove(0);
		return bit;
	}
	
	public abstract void pushInput(I bit);
}
