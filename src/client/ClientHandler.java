package client;

import server.News;
import server.Server;
import utils.Barrier;
import utils.Request;
import utils.Task;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

/* Considerado o nosso deal with client */

public class ClientHandler extends Thread{

    private Server server;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private int id;	
    private Barrier<Task> barrier;
    
    public ClientHandler(Server server, Socket socket, ObjectOutputStream out, ObjectInputStream in, int id) {
    	super();
    	this.server = server;
    	this.socket = socket;
    	this.out = out;
    	this.in = in;
    	this.id = id;
    }
    
    public void run(){
        try{
            while(!socket.isClosed()){
                receive();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Barrier<Task> getBarrierOfTasks() {
		// TODO Auto-generated method stub
		return barrier;
	}

   

    public synchronized void receive(){
    	System.out.println("Entrou no Receive do ClientHandler");
    	LinkedList<Task> listCompleted;
    	ArrayList<News> auxListOfNews;
        try{
        	Request req = (Request) in.readObject();
        	System.out.println(req);
        	if(req.getWord().equals("EXITCLIENT"))
        		closeSocket();
        	else {
        		System.out.println("entrou no else do receive");
        		barrier = new Barrier<Task>(server.getListOfNews().size());
        		server.searchWord(this.id, req.getWord());
        		listCompleted = barrier.getResults();
        		auxListOfNews = new ArrayList<News>();
        		
        		System.out.println(auxListOfNews.size());
        		
        		for(Task t : listCompleted) {
        			if(t.getIndex().size() > 0) {
        				t.getNews().setIndex(t.getIndex());
        				auxListOfNews.add(t.getNews());
        			}
        		}
        		notifyAll();
        		out.writeObject(new Request(auxListOfNews));
        		notifyAll();
        	}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void closeSocket() {
    	try {
    		System.out.println("Conexão com o cliente a fechar");
    		server.getListOfClients().remove(this);
    		server.decrementCounter();
    		socket.close();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}