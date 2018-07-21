package Code;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SuperTaxi extends Taxi{
	
	RequestList ReqList;
	/**
	*@REQUIRES: id!=null;gui!=null;0<=id<=29;
	*@MODIFIES: this,this,ReqList;
	*@EFFECTS: 
	*		true==>(A new SuperTaxi extends from Taxi has been constructed);
	*/
	public SuperTaxi(int id, TaxiGUI gui) {
		super(id, gui);
		this.ReqList=new RequestList();
		// TODO Auto-generated constructor stub
	}	
	/**
	*@EFFECTS: 
	*		\result==(A new SuperTaxiGen);
	*/
	public SuperTaxiGen terms(){
		return new SuperTaxiGen(this);
	}

	public static class SuperTaxiGen implements Iterator{
		private SuperTaxi ST;
		private int n;
		/**
		*@REQUIRES: it!=null;
		*@MODIFIES: this.ST,this.n;
		*@EFFECTS: 
		*		true==>(A new SuperTaxiGen has been constructed);
		*/
		SuperTaxiGen(SuperTaxi it){
			ST=it;
			n=0;
		}
		/**
		*@EFFECTS: 
		*		\result==(n<ST.ReqList.size);
		*/
		public boolean hasNext(){
			return n<ST.ReqList.size();
		}
		/**
		*@EFFECTS: 
		*		\result==(n>0);
		*/
		public boolean hasLast(){
			return n>0;
		}
		/**
		*@MODIFIES: this.n;
		*@EFFECTS: 
		*		\result==>(The next Request);
		*		exceptional_behavior(NoSuchElementException);
		*/
		public Request next()throws NoSuchElementException{
			for(int e=n;e<=ST.ReqList.size();e++){
				if(ST.ReqList.get(e)!=null){
					n=e+1;
					return ST.ReqList.get(e);
				}
			}
			throw new NoSuchElementException("emmmmm");
		}
		/**
		*@MODIFIES: this.n;
		*@EFFECTS: 
		*		\result==>(The last Request);
		*		exceptional_behavior(NoSuchElementException);
		*/
		public Request last()throws NoSuchElementException{
			for(int e=n-1;e>=0;e--){
				if(ST.ReqList.get(e)!=null){
					if(e>0){
						n=e;
					}
					else{
						n=0;
					}
					return ST.ReqList.get(e);
				}
			}
			throw new NoSuchElementException("emmmmm");
		}
	}
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		return super.repOK();
	}
	
}
