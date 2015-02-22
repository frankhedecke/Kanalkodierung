package blockcode;

import entity.BinaryMatrix;
import entity.BinaryWord;
import main.Channel;

public interface BlockCode {

	public BinaryWord decode(BinaryWord input);

	public BinaryWord encode(BinaryWord input);
	
	public BinaryWord getRedundancy(BinaryWord input);

	public BinaryMatrix getGeneratorMatrix();

	public BinaryMatrix getControlMatrix();

	public int getN();

	public int getL();
	
	public Channel<Integer> getDecodeChannel();
	
	public Channel<Integer> getEncodeChannel();
}
