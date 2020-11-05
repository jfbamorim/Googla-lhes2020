package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

public class News implements Serializable{
	private static final long serialVersionUID = -6763036241565008641L;
	private String title;
	private ArrayList<String> content;
	private ArrayList<Integer> index;
	private int count;
	
	public News(File file) {
		content = new ArrayList<>();
		try {
			Scanner sc = new Scanner(file, "UTF-8");
			//guardamos o título da notícia
			if(sc.hasNextLine()) {
				String aux = sc.nextLine();
				title = aux;
				content.add(aux);
			}
			//iteramos o ficheiro de noticia
			//e guardamos o seu conteudo apos
			//o titulo, no ArrayList content.
			while(sc.hasNextLine()) {
				content.add(sc.nextLine());
			}
			sc.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		this.index = new ArrayList<>();
		this.count = 0;
	}
	
	// getters & setters
	public ArrayList<String> getContent(){
		return content;
	}
	
	public String getText() {
		return title;
	}
	
	public String getName() {
		return this.count + " - " + title;
	}
	
	public void setIndex(ArrayList<Integer> index) {
		this.index = index;
		this.count = index.size();
	}
	
	//encontrar uma palavra no ficheiro das noticias
	public ArrayList<Integer> find(String word){
		int index;
		ArrayList<Integer> found = new ArrayList<>();
		
		for (String w: content) {
			index = 0;
			while(index != -1) {
				index = w.indexOf(word, index);
				if(index != -1) {
					found.add(index);
					index += w.length();
				}
			}
		}
		return found;
	}
}
