package utils;

import java.util.LinkedList;

public class Barrier<T> {
	private LinkedList<T> list;
	private int currElem;
	private int totalElem;
	
	public Barrier(int totalElem) {
		list = new LinkedList<>();
		this.currElem = 0;
		this.totalElem = totalElem;
		
	}
	
	public synchronized LinkedList<T> getResults(){
		try {
			while(currElem < totalElem) {
				wait();
			}
			
		}catch(InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		// após a barreira estar completa, devolvemos a barreira e criamos uma nova
		currElem = 0;
		LinkedList<T> finalList = list;
		list = new LinkedList<T>();
		notifyAll();
		return finalList;
	}
	
	// adicionamos uma tarefa à barreira e notificamos todas as threads
	public synchronized void add(T t) {
		list.add(t);
		currElem++;
		if(currElem == totalElem)
			notifyAll();
	}
}
