public interface BlockCode {

	public BinaryWord decode(BinaryWord input);

	public BinaryWord encode(BinaryWord input);

	public BinaryMatrix getGeneratorMatrix();

	public BinaryMatrix getControlMatrix();

	public int getN();

	public int getL();
	
	public Channel getDecodeChannel();
	
	public Channel getEncodeChannel();
}
