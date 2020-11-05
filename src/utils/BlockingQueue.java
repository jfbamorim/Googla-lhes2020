package utils;

import java.util.LinkedList;

public class BlockingQueue<T> {
	private LinkedList<T> list;
	
	public BlockingQueue() {
		list = new LinkedList<T>();
	}
	
	public synchronized void put(T t) {
		list.add(t);
		notifyAll();
	}
	
	public synchronized T get() {
		try {
			while(list.size() == 0)
				wait();
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		T t = list.removeFirst();
		notifyAll();
		return t;
	}
}
