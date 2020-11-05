package utils;

import java.io.Serializable;
import java.util.ArrayList;
import server.News;

public class Task implements Serializable{
	private static final long serialVersionUID = -637747067555134355L;
	private int id;
	private News news;
	private String word;
	private ArrayList<Integer> index;
	private int idx;

	public Task(News news, String word, int idx) {
		this.news = news;
		this.word = word;
		this.idx = idx;
		index = new ArrayList<Integer>();
	}
	
	public int getID(){
		return id;
	}

	public News getNews() {
		return this.news;
	}

	public String getWord() {
		return this.word;
	}
	
	public int getIdx() {
		return this.idx;
	}
	
	public ArrayList<Integer> getIndex(){
		return index;
	}

	public void setIndex(ArrayList<Integer> index){
		this.index = index;
	}
}