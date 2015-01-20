import java.util.LinkedList;
import java.util.List;

public class AbstractDecodeChannel implements Channel {

	private BlockCode code;
	private int[] inputBuffer;
	private int inputPtr;
	private List<Integer> buffer;

	public AbstractDecodeChannel(BlockCode code) {
		this.code = code;
		this.buffer = new LinkedList<Integer>();
		// maybe N+1 ?
		this.inputBuffer = new int[code.getN()];
		this.inputPtr = 0;
	}

	@Override
	public boolean hasOutput() {
		return !buffer.isEmpty();
	}

	@Override
	public int getOutput() {
		int bit = buffer.remove(buffer.size() - 1);
		return bit;
	}

	@Override
	public void pushInput(int bit) {
		this.inputBuffer[this.inputPtr] = bit;
		this.inputPtr++;

		if (this.inputPtr == this.inputBuffer.length) {
			this.inputPtr = 0;

			BinaryWord input = new BinaryWord(inputBuffer);
			input.reverse();
			// TODO delete/overwrite inputBuffer?
			BinaryWord output = code.decode(input);

			for (int i : output.toArray()) {
				buffer.add(i);
			}

		}
	}
}
