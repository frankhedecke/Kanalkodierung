package blockcode;

import channels.Channel;
import entity.BinaryMatrix;
import entity.BinaryWord;

public interface BlockCode {

	public BinaryWord decode(BinaryWord input);

	public BinaryWord encode(BinaryWord input);

	public BinaryWord getRedundancy(BinaryWord input);

	public int[] getBitOrder();

	public BinaryMatrix getGeneratorMatrix();

	public BinaryMatrix getControlMatrix();

	public int getK();

	public int getN();

	public int getL();

	public Channel<Integer> getDecodeChannel();

	public Channel<Integer> getEncodeChannel();

	public Channel<Integer> createOrderedEncodeChannel();
}