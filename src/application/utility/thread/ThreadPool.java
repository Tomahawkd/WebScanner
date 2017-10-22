package application.utility.thread;

import java.util.LinkedList;

/**
 * Intercepter: Intercepter Thread Pool
 * 
 * @author Tomahawkd
 */

public class ThreadPool extends ThreadGroup {

	/**
	 * Indicate the thread pool is closed
	 */

	private boolean isClosed = false;

	/**
	 * Work queue
	 */

	private LinkedList<Runnable> workQueue;

	private int activeCount;

	/**
	 * Thread pool ID
	 */

	private static int threadPoolID;

	/**
	 * Thread ID
	 */

	private int threadID;

	/**
	 * Create a new Thread pool
	 * 
	 * @param poolSize
	 *            The size of working thread
	 */

	public ThreadPool(int poolSize) {
		super("ThreadPool-" + (threadPoolID++));
		setDaemon(true);
		workQueue = new LinkedList<>();

		// Initialize active thread
		for (int i = 0; i < poolSize; i++) {
			new WorkThread().start();
		}
	}

	/**
	 * Add a new Thread work
	 * 
	 * @param task
	 *            The runnable task
	 */

	public synchronized void execute(Runnable task) {

		// Check if the thread pool is closed
		if (isClosed) {
			return;
		}

		if (task != null) {
			workQueue.add(task);
			notify();
		}
	}

	public void waitFor() throws InterruptedException {
		while (activeCount > 0) {
		}
	}

	/**
	 * Get a task from queue
	 */

	private synchronized Runnable getTask() throws InterruptedException {
		while (workQueue.size() == 0) {
			if (isClosed)
				return null;
			wait();
		}
		return workQueue.removeFirst();
	}

	/**
	 * Close Thread pool
	 */

	public synchronized void close() {
		if (!isClosed) {
			isClosed = true;

			// Clear queue
			workQueue.clear();

			// Interrupt thread
			interrupt();
		}
	}

	/**
	 * Stores Threads
	 */

	private class WorkThread extends Thread {
		WorkThread() {
			super(ThreadPool.this, "WorkThread-" + (threadID++));
		}

		public void run() {
			while (!isInterrupted()) {
				Runnable task = null;
				try {

					// Get a new task
					task = getTask();
				} catch (InterruptedException ignored) {
				}

				// End the thread if the next task is null
				if (task == null)
					return;

				try {
					activeCount++;
					task.run();
					activeCount--;
				} catch (Throwable ignored) {
					activeCount--;
				}
			}
		}
	}

}