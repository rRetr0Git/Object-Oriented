package Code;

import java.util.ArrayList;

public class IFTTTLIST {
	private ArrayList<IFTTT> list;
	
	public IFTTTLIST(){
		this.list = new ArrayList<IFTTT>();
	}
	public synchronized void add(IFTTT x){
		this.list.add(x);
	}
	public synchronized IFTTT get(int index){
		return this.list.get(index);
	}
	public synchronized int size(){
		return this.list.size();
	}
}
