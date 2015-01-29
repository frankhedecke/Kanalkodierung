public interface Channel<T> {

	public boolean hasOutput();

	public T getOutput();

	public void pushInput(T bit);

}
