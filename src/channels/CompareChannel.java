package channels;

import java.util.LinkedList;

public class CompareChannel extends IntegerBufferChannel {

	private LinkedList<Integer> compareBuffer;

	public CompareChannel() {
		this.compareBuffer = new LinkedList<Integer>();
	}

	@Override
	public void pushInput(Integer bit) {
		this.buffer.add(bit);
	}
	
	public void pushComparandum(Integer bit) {
		this.compareBuffer.add(bit);
	}
	
	public boolean compare() {

		while(! this.buffer.isEmpty()) {
			if (this.buffer.remove(0) != this.compareBuffer.remove(0)) {

				this.buffer.clear();
				this.compareBuffer.clear();
				return false;
			}
		}

		return true;
	}
}