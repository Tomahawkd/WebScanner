package application.utility.thread;

import application.spider.SpiderHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskControl {

	private static TaskControl taskControl;
	private ThreadController controller;

	public static TaskControl getInstance() {
		if (taskControl == null) taskControl = new TaskControl();
		return taskControl;
	}

	private TaskControl() {
		controller = new ThreadController();
	}

	public void shutDown() {
		controller.shutdown();
	}

	public ThreadController getController() {
		return controller;
	}

	public class ThreadController {

		private ExecutorService executor;
		private ControllableThreadController threadController;

		private ThreadController() {
			executor = Executors.newFixedThreadPool(10);
			threadController = new ControllableThreadController();
		}

		public void addTask(Runnable task) {
			executor.execute(task);
		}

		public String addControllableTask(Runnable task) {
			return threadController.addTask(task);
		}

		public void stopControllableTask(String token) {
			threadController.stop(token);
		}

		private void shutdown() {
			SpiderHandler.getInstance().suspend();
			threadController.shutDown();
			executor.shutdown();
		}
	}

	private static class ControllableThreadController {

		private int threadCount = 0;
		private Map<String, Thread> threadMap;

		private ControllableThreadController() {
			threadMap = new ConcurrentHashMap<>();
		}

		String addTask(Runnable task) {
			String token = "ControllableTask" + (threadCount++);
			Thread thread = new Thread(task, token);
			threadMap.put(token, thread);
			thread.start();
			return token;
		}

		void stop(String token) {
			Thread thread = threadMap.get(token);
			if (thread != null) {
				if (thread.isAlive()) {
					thread.interrupt();
				}
				threadMap.remove(token, thread);
			}
		}

		void shutDown() {
			for (String token : threadMap.keySet()) {
				stop(token);
			}
		}
	}
}
