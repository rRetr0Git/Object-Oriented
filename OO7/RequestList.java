package Code;

import java.util.ArrayList;

public class RequestList{
	private ArrayList<Request> list;
	
	public RequestList(){
		this.list = new ArrayList<Request>();
	}
	
	public synchronized void add(Request r){
		this.list.add(r);
	}
	
	public synchronized Request get(int index){
		return this.list.get(index);
	}
	
	public synchronized void remove(int index){
		this.list.remove(index);
	}
	
	public synchronized int size(){
		return this.list.size();
	}	
	
	public synchronized boolean contains(Request r){
		if(this.size()>0){
			for(int i=0;i<this.list.size();i++){
				if(this.list.get(i).equals(r)){
					return true;
				}
			}
		}
		return false;
	}
}
