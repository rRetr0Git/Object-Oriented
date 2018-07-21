package Code;

import java.awt.Point;
import java.util.ArrayList;

public class Request {
	
	private Point SRC;
	private Point DST;
	private long time;
	private ArrayList<Integer> taxiList;
		/**
		*@REQUIRES:srcx!=null,srcy!=null,dstx!=null,dsty!=null;
		*@MODIFIES:this;
		*@EFFECTS: 
		*		true==>(A new Request has been constructed);
		*/	
	public Request(int srcx,int srcy,int dstx,int dsty ){
		this.SRC=new Point(srcx, srcy);
		this.DST=new Point(dstx, dsty);
		this.time=(System.currentTimeMillis()/100)*100;
		this.taxiList=new ArrayList<Integer>();
	}
		/**
		*@REQUIRES:r!=null;
		*@EFFECTS: 
		*		(SRC.equals(r.GetSrc())&&DST.equals(r.GetDst())&&time.equals(r.GetTime()))==>\result==true;
		*		(!SRC.equals(r.GetSrc())||!DST.equals(r.GetDst())||!time.equals(r.GetTime()))==>\result==false;
		*/	
	public boolean equals(Request r){
		return this.SRC.equals(r.GetSrc())&&this.DST.equals(r.GetDst())&&this.time==r.GetTime();
	}
		/**
		*@EFFECTS: 
		*		\result==(A certain String of the request);
		*/	
	public String toString(){
		return "[CR,"+"("+(int)this.SRC.getX()+","+(int)this.SRC.getY()+"),("+(int)this.DST.getX()+","+(int)this.DST.getY()+")]";
	}
		/**
		*@EFFECTS: 
		*		\result==SRC;
		*/	
	public Point GetSrc(){
		return this.SRC;
	}
		/**
		*@EFFECTS: 
		*		\result==DST;
		*/	
	public Point GetDst(){
		return this.DST;
	}
		/**
		*@EFFECTS: 
		*		\result==time;
		*/	
	public long GetTime(){
		return this.time;
	}
		/**
		*@REQUIRES:id!=null;
		*@MODIFIES:taxiList;
		*@EFFECTS: 
		*		(!taxiList.contains())==>(taxiList.contains()&&\result==true);
		*		(taxiList.contains())==>(\result==false);
		*/	
	public boolean addTaxi(int id){
		if(!this.taxiList.contains(id)){
			this.taxiList.add(id);	
			//System.out.println("add taxi:"+id);
			return true;
		}
		else{
			return false;
		}
	}
		/**
		*@EFFECTS: 
		*		\result==A certain String to output to file;
		*/	
	public String OutputInfo(){
		return "Time:"+this.time+" SRC"+"("+(int)this.SRC.getX()+","+(int)this.SRC.getY()+") DST("+(int)this.DST.getX()+","+(int)this.DST.getY()+")"+System.getProperty("line.separator");
	}
		/**
		*@EFFECTS: 
		*		\result==taxiList;
		*/	
	public ArrayList<Integer> getList(){
		return this.taxiList;
	}
}