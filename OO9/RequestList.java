package Code;

import java.util.ArrayList;

public class RequestList{
	private ArrayList<Request> list;
		/**
		*@MODIFIES:this;
		*@EFFECTS: 
		*		true==>(A new RequestList has been constructed);
		*/	
	public RequestList(){
		this.list = new ArrayList<Request>();
	}
		/**
		*@REQUIRES:r!=null;
		*@MODIFIES:this.list;
		*@EFFECTS: 
		*		list.contains(r);
		*/	
	public synchronized void add(Request r){
		this.list.add(r);
	}
		/**
		*@REQUIRES:index!=null;
		*@EFFECTS: 
		*		\result==list.get(index);
		*/	
	public synchronized Request get(int index){
		return this.list.get(index);
	}
		/**
		*@REQUIRES:index!=null;
		*@MODIFIES:this.list;
		*@EFFECTS: 
		*		list.remove(index);
		*/	
	public synchronized void remove(int index){
		this.list.remove(index);
	}
		/**
		*@EFFECTS: 
		*		\result==list.size();
		*/	
	public synchronized int size(){
		return this.list.size();
	}	
		/**
		*@REQUIRES:r!=null;
		*@EFFECTS: 
		*		(list.size()>0)==>(\all int i;0<=i<list.size();!list.get(i).equals(r))==>(\result==false);
		*		(list.size()>0)==>(\exist int i;0<=i<list.size();list.get(i).equals(r))==>(\result==true);
		*		(!list.size()>0)==>(\result==false);
		*/	
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
