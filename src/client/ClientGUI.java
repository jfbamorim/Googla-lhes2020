package client;

import javax.swing.*;
import java.awt.*;

public class ClientGUI {
    private JFrame frame;
    private JPanel panel;
    private JButton searchButton;
    private JTextField searchText;
    private JTextArea textArea;
    private DefaultListModel<String> modelLeft;
    private JList<String> listLeft;

    public ClientGUI(){
        frame = new JFrame("Projeto PCD 2020");
        panel = new JPanel(new BorderLayout());
        // https://www.geeksforgeeks.org/java-swing-jlist-with-examples/
        listLeft = new JList<String>();
        addContent();
    }
    
    // getters & setters
    public JButton getButton(){
        return this.searchButton;
    }

    public JTextField getTextSearch(){
        return searchText;
    }

    public JTextArea getTextArea(){
        return textArea;
    }

    public JFrame getFrame(){
        return frame;
    }
    
    public DefaultListModel<String> getModelLeft(){
    	return modelLeft;
    }
    
    public JList<String> getListLeft() {
		return listLeft;
	}
    
    public void setAreaText(JTextArea textArea) {
    	this.textArea = textArea;
    }
    
    // verificar o tamanho do ecrã e colocar a GUI no centro do mesmo
    public void setDimension(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    //construcao da janela
    private void addContent(){
        //adicionar funções de fecho e tamanho da janela
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setResizable(true);

        //adicionar conteudo no painel Norte no BorderLayout
        JPanel north = new JPanel();
        searchText = new JTextField();
        searchText.setPreferredSize(new Dimension(300, 30));
        north.add(searchText);

        searchButton = new JButton("Search");
        searchButton.setPreferredSize(new Dimension(120, 30));
        north.add(searchButton);

        //adicionar conteudo no painel Centro no BorderLayout
        JPanel center = new JPanel();
        center.setLayout(new GridLayout(1,2));

        JScrollPane scrollListLeft = new JScrollPane();
        JScrollPane scrollListRight = new JScrollPane();

        center.add(scrollListLeft);
        center.add(scrollListRight);

        panel.add(north, BorderLayout.NORTH);
        panel.add(center, BorderLayout.CENTER);
        frame.add(panel);
    }

    public void open(){
        //posicionar o panel no centro do ecrã
        setDimension();
        
        //deixar a JFrame e o seu conteúdo visível
        frame.setVisible(true);
    }

    public static void main(String[] args){
        ClientGUI c = new ClientGUI();
        c.open();
    }

}