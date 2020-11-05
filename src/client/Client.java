package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import server.News;
import utils.Request;

public class Client {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private ClientGUI clientGui;
    private ArrayList<News> results;
    private static final int PORT = 8080;

    // ideia retirada dos slides de apoio à disciplina - Aula 6 - Network Programming: exemplo cliente - runclient
    private void runClient() throws IOException{
        try{
        	connectToServer();
            initializeClientGui();
        } catch (IOException e) {
        	closeSocket();
            e.printStackTrace();
        }
    }
    
    // ideia retirada dos slides de apoio à disciplina - Aula 6 - Network Programming: exemplo cliente - connecttoserver
    private void connectToServer() throws IOException {
        socket = new Socket("localhost", PORT);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        out.writeObject(new String("Client"));
        System.out.println("***************** CLIENTE ***************************");
        System.out.println("Ligação realizada com o servidor com sucesso");
    }
    
    private void initializeClientGui() throws IOException{
    	//inicializa novo cliente GUI e adiciona funcionalidades à interface
        clientGui = new ClientGUI();
        addFunctionality();
        clientGui.open();
    }
    
    private void addFunctionality() {
    	clientGui.getButton().addActionListener(new ActionListener(){
    		
    		@Override
    		public void actionPerformed(ActionEvent e) {
    			buttonSearch();
    		}
    		
    		private void buttonSearch() {
    			//clientGui.getModelLeft().clear();
    			String word = clientGui.getTextSearch().getText();
    			System.out.println(word);
    			try {
    				//envia para o servidor um pedido com a palavra
    				out.writeObject(new Request(word));
    				System.out.println("Pedido enviado para o servidor");
    				
    				//fica a espera de resposta do servidor com os resultados obtidos
    				//pelo worker
    				Request resultSearch = (Request)in.readObject();
    				
    				System.out.println("Cliente" + resultSearch);
    				
    				results = resultSearch.getList();
    				
    				System.out.println("Cliente" + results.size());
    				
    				for(News n : results) {
    					clientGui.getModelLeft().addElement(n.getName());
    				}
					System.out.println("Resultado da pesquisa concluido");
					clientGui.getListLeft().updateUI();
    			}catch (IOException e) {
					e.printStackTrace();
				}catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
    		}
    	});
    	
    	// slides Swing GUI - Event and Listeners
    	clientGui.getListLeft().addListSelectionListener(new ListSelectionListener(){
    		
    		@Override
    		public void valueChanged(ListSelectionEvent e) {
    			openNew(e);
    		}
    		
    		public void openNew(ListSelectionEvent e) {
    			openSelected(e);
    		}
    		
    	});
    }
    
    private void openSelected(ListSelectionEvent e) {
    	clientGui.getTextArea().setText("");
    	if(!e.getValueIsAdjusting()) {
    		String selValue = clientGui.getListLeft().getSelectedValue();
    		if (selValue != null) {
    			for(News n : results) {
    				if(n.getName().equals(selValue)) {
    					boolean firstLine = true;
    					for (String t : n.getContent()) {
    						if(firstLine) {
    							clientGui.getTextArea().append(t+"\n\n");
    							firstLine = false;
    						}
    						else {
    							clientGui.getTextArea().append(t);
    						}
    					}
    				}
    			}
    		}
    	}
    }
    
    private void closeSocket(){
    	try {
    		System.out.println("Cliente a encerrar");
    		out.writeObject(new Request(new String("EXITCLIENT")));
    	}catch (IOException e) {
    		e.printStackTrace();
		}
    }
    
	// https://www.quora.com/What-does-public-static-void-main-throws-IOException-mean
	// uma vez que estamos a utilizar operações I/O
    public static void main(String [] args) throws IOException{
    	new Client().runClient();
    }
}

