package worker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import server.Server;
import utils.BlockingQueue;
import utils.Task;

/* Considerado o nosso deal with worker */

public class WorkerHandler extends Thread {
	private Server server;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private BlockingQueue<Task> list;
	private Task newtask;
	
	public WorkerHandler(Server server, Socket socket, ObjectOutputStream out, ObjectInputStream in, BlockingQueue<Task> list) {
		super();
		this.server = server;
		this.socket = socket;
		this.out = out;
		this.in = in;
		this.list = list;
	}
	
	public void run() {
		while(!socket.isClosed()) {
			try {
				getTasks();
				receive();
			} catch (IOException e) {
				closeSocket();
			}
		}
	}
	
	private synchronized void getTasks() throws IOException {
		//validar novamente que o socket não foi fechado
		while(!socket.isClosed()) {
			newtask = list.get();
			out.writeObject(newtask);
		}
	}
	
	private synchronized void receive() throws IOException {
		Task t;
		try {
			//para o caso da palavra recebida ser EXITWORKER
			t = (Task) in.readObject();
			if(t.getWord().equals("EXITWORKER")) {
				closeSocket();
			}
			//no caso de querermos adicionar tarefas a barreira de tarefas
			else {
				server.getListOfClients().get(t.getID()).getBarrierOfTasks().add(t);
			}
		}catch(ClassNotFoundException e) {
			closeSocket();
		}
	}
	
	private void closeSocket() {
		try {
			System.out.println("A fechar socket do Worker");
			socket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
}
