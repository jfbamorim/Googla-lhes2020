package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import client.ClientHandler;
import utils.Task;
import utils.Barrier;
import utils.BlockingQueue;
import worker.WorkerHandler;

public class Server {
    private static final String path = "./src/news";
    private static final int PORT = 8080;
    private ServerSocket serverSocket;
    private Socket socket;
    private ArrayList<News> listOfNews;
    private ArrayList<ClientHandler> listOfClients;
    private BlockingQueue<Task> listOfTasks;
    private Barrier<Task> barrier;
    private int clientCounter = 0;
    
    // getters
    public ArrayList<News> getListOfNews(){
    	return listOfNews;
    }
    
    public ArrayList<ClientHandler> getListOfClients(){
    	return listOfClients;
    }
    
    public void decrementCounter() {
    	clientCounter--;
    }
    
    private void loadNews() {  	
    	//carrega as noticias - base de conhecimento do servidor
    	//https://stackoverflow.com/questions/10257981/read-text-file-into-an-array
    	//le os ficheiros todos e coloca numa lista que permite fazer as procuras
    	File[] files = new File(path).listFiles();
    	if(files.length < 0) {
    		System.out.println("Erro na leitura dos ficheiros");
    		System.exit(4);
    	}
    	listOfNews = new ArrayList<>();
    	for(File f : files) 	{
    		listOfNews.add(new News(f));
    	}
    	// devolve para o output o nº ficheiros carregados para o servidor
    	System.out.println("***************** SERVIDOR ***************************");
    	System.out.println("Foram carregadas para o servidor: " + listOfNews.size() + " noticias."); 
    }
    
    // ideia retirada dos slides de apoio à disciplina - Aula 6 - Network Programming: exemplo servidor - startserving
    private void startServing() throws IOException{
    	//carrega as noticias para uma lista de noticias
    	loadNews();
    	serverSocket = new ServerSocket(PORT);
    	
    	System.out.println("Servidor: OK");
    	System.out.println("Servidor: A espera de conexões ...");
    	
    	//cria-se uma lista de clientes (client-server)
    	listOfClients = new ArrayList<ClientHandler>();
    	listOfTasks = new BlockingQueue<Task>();
    	
    	//servidor fica a espera de ligações atraves do accept
    	try {
	    	while(true) {
	    		socket = serverSocket.accept();
	    		System.out.println("Conexão estabelecida: OK");
	    		doConnections(socket);
	    	}
    	}finally{
    		System.out.println("Servidor: A fechar...");
    		socket.close();
    	}
    }
    
 // ideia retirada dos slides de apoio à disciplina - Aula 6 - Network Programming: exemplo servidor - doconnections
    private void doConnections(Socket socket) throws IOException {
    	String connection;
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        try{
        	//precisa da conversão para string uma vez que devolve um Object
            connection = (String)in.readObject();
            if(connection.contentEquals("Client")) {
            	clientCounter++;
            	ClientHandler clienthandler = new ClientHandler(this, socket, out, in, clientCounter);
            	listOfClients.add(clienthandler);
            	System.out.println("Cliente " + clientCounter + " conectado ao Servidor");
            	//inicia-se uma thread relativamente ao cliente
            	clienthandler.start();
            }
            //worker liga-se e fica pendurado à espera de uma tarefa
            else if (connection.contentEquals("Worker")){
            	WorkerHandler workerhandler = new WorkerHandler(this, socket, out, in, listOfTasks);
            	System.out.println("Worker conectado ao Servidor");
            	workerhandler.start();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public synchronized void searchWord(int id, String palavra) {
    	for (News news : listOfNews) {
    		Task t = new Task(news, palavra, id);
    		listOfTasks.put(t);
    	}
    }
    
    public static void main(String[] args) throws IOException {
    	new Server().startServing();
    }
}
