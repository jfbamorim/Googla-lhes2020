package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import utils.Request;
import utils.Task;

public class Worker {
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private static final int PORT = 8080;
	private int i = 0;
	
	// cria um worker para se ligar ao servidor
	public void runWorker() {
		try {
			socket = new Socket("localhost", PORT);
			out	= new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.writeObject(new String("Worker"));
			System.out.println("***************** WORKER ***************************");
			System.out.println("Ligação estabelecida com o servidor");
			while(!socket.isClosed())
				executeTask();
		}catch(IOException e) {
			System.out.println("Exceção apanhada");
			closeSocket();
			e.printStackTrace();
		}
	}
	
	private synchronized void executeTask() throws IOException {
		System.out.println("Entrou" + i++);
		try {
			Task t = (Task)in.readObject();
			ArrayList<Integer> index = t.getNews().find(t.getWord());
			t.setIndex(index);
			out.writeObject(t);
			notifyAll();
			
		}catch(ClassNotFoundException e) {
			e.getStackTrace();
		}
	}
	
	private void closeSocket() {
		try {
			out.writeObject(new Request(new String("EXITWORKER")));
			System.out.println("Worker encerrado");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// https://www.quora.com/What-does-public-static-void-main-throws-IOException-mean
	// uma vez que estamos a utilizar operações I/O
	public static void main(String [] args) throws IOException {
		new Worker().runWorker();
	}	
}