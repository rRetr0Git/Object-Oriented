package Code;

import java.util.ArrayList;

public class RequestQueue{
	ArrayList RQ = new ArrayList();
	
	public synchronized void add(Request r){
		RQ.add(r);
	}
	
	public synchronized void add(int index,Request r){
		RQ.add(index, r);
	}
	
	public synchronized void remove(int index){
		RQ.remove(index);
		
	}
	
	public synchronized int size(){
		return RQ.size();
	}
	
	public synchronized Request get(int index){
		return (Request) RQ.get(index);
	}
}
