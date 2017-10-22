package application.utility.counter;

public class IntegerCounter {
	private int counter;

	public IntegerCounter(int initNum) {
		counter = initNum;
	}

	public void increase(int num) {
		counter += num;
	}

	public int getCount() {
		return counter;
	}
}
