import java.util.LinkedList;
import java.util.List;

public abstract class AbstractBufferChannel implements Channel {

	protected List<Integer> buffer;
	
	public AbstractBufferChannel() {
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

}
