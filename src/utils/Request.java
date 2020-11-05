package utils;

import java.io.Serializable;
import java.util.ArrayList;

import server.News;

/* Por exemplo, para utilizar um ObjectOutputStream e salvar 
*  um objeto num arquivo do disco será necessário implementar essa interface.
*  https://pt.stackoverflow.com/questions/88270/qual-a-finalidade-da-interface-serializable
*/

public class Request implements Serializable{
	private static final long serialVersionUID = 7368089823283567355L;
	private ArrayList<News> list;
	private String word;
	
	public Request(String word) {
		this.word = word;
	}
	
	public Request(ArrayList<News> list) {
		super();
		this.list = list;
	}
	
	public String getWord() {
		return this.word;
	}
	
	public ArrayList<News> getList(){
		return this.list;
	}
}