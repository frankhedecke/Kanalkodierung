public interface Channel {

	public boolean hasOutput();

	public int getOutput();

	public void pushInput(int bit);

}
