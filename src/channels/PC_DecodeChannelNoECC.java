package channels;

import blockcode.BlockCode;

public class PC_DecodeChannelNoECC extends IntegerBufferChannel {
	
	private BlockCode code;
	private int count;
	private int [] inputBuffer;
	private int bufferDepth; 

	public PC_DecodeChannelNoECC(BlockCode code) {
		this.code = code;
		count = 0;
		bufferDepth = code.getL() * code.getL() + 2 * code.getL() * (code.getN() - code.getL());
		inputBuffer = new int[bufferDepth];
	}

	@Override
	public void pushInput(Integer bit) {
		
		inputBuffer[count] = bit;
		
		count ++;
		if (count == bufferDepth) {
			
			for (int i = 0; i < code.getL() * code.getL(); ++i) {
				this.buffer.add(inputBuffer[i]);
			}
			
			count = 0;
		}
	}

}
