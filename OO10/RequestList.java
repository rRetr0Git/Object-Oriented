package Code;

import java.util.ArrayList;

public class RequestList{
	/**
	 *@OVERVIEW: RequestList contains a ArrayList to save all the request to be dispatched,
	 *			it can do some basic operation such as add,remove,get and etc.
	 *			this class is mainly used to maintenance the requests;
	 */
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
		*@REQUIRES:index!=null;0<=index<=\old(list).size();
		*@MODIFIES:this.list;
		*@EFFECTS: 
		*		normal_behavior
		*		!list.contains(\old(list).get(index))
		*		index>\old(list).size()||index<0==>exceptional_behavior(Exception);
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
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		if(this.list==null)return false;
		for(int i=0;i<this.list.size();i++){
			Object r1 = this.list.get(i);
			if(!(r1 instanceof Request))return false;
			for(int j=i+1;j<this.list.size();j++){
				Object r2 = this.list.get(j);
				if(((Request)r1).equals((Request)r2))return false;
			}
		}
		return true;
	}
}
