package Code;

import java.awt.Point;
import java.util.ArrayList;

public class Request {
	
	private Point SRC;
	private Point DST;
	private long time;
	private ArrayList<Integer> taxiList;
	
	public Request(int srcx,int srcy,int dstx,int dsty ){
		this.SRC=new Point(srcx, srcy);
		this.DST=new Point(dstx, dsty);
		this.time=(System.currentTimeMillis()/100)*100;
		this.taxiList=new ArrayList<Integer>();
	}
	public boolean equals(Request r){
		return this.SRC.equals(r.GetSrc())&&this.DST.equals(r.GetDst())&&this.time==r.GetTime();
	}
	
	public String toString(){
		return "[CR,"+"("+(int)this.SRC.getX()+","+(int)this.SRC.getY()+"),("+(int)this.DST.getX()+","+(int)this.DST.getY()+")]";
	}
	
	public Point GetSrc(){
		return this.SRC;
	}
	
	public Point GetDst(){
		return this.DST;
	}
	public long GetTime(){
		return this.time;
	}
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
	public String OutputInfo(){
		return "Time:"+this.time+" SRC"+"("+(int)this.SRC.getX()+","+(int)this.SRC.getY()+") DST("+(int)this.DST.getX()+","+(int)this.DST.getY()+")"+System.getProperty("line.separator");
	}
	public ArrayList<Integer> getList(){
		return this.taxiList;
	}
}
