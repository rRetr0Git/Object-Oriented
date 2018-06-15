package Code;

public class Request {
	private double time;
	private String str;
	private String type;
	private int dstfloor;
	private long BeginTime;
	private int id;
	private String dir;
	
	public Request(String str,double time,long BeginTime){
		this.str=str;
		this.time=time;
		this.BeginTime=BeginTime;
		this.type=str.split(",")[0].replace("(", "");
		if(this.type.equals("FR")){
			this.id=0;
			this.dstfloor=Integer.parseInt(str.split(",")[1]);
			this.dir=str.split(",")[2].replace(")", "");
		}
		else{
			this.id=Integer.parseInt(str.split(",")[1].replace("#", ""));
			this.dstfloor=Integer.parseInt(str.split(",")[2].replace(")", ""));
			this.dir="";
		}
	}
	
	public Request(String str){
		this.str=str;
		this.type="END";
		this.dstfloor=0;
		this.dir="END";
		this.id=-1;
	}
	
	public String GetType(){
		return type;
	}
	public int GetDst(){
		return dstfloor;
	}
	public String GetDir(){
		return dir;
	}
	public double GetTime(){
		return time;
	}
	public int GetId(){
		return id;
	}
	public long GetBeginTime(){
		return BeginTime;
	}
	
	public boolean equals(Request r){
		return(this.type.equals(r.GetType())&&this.dstfloor==r.GetDst()&&this.dir.equals(r.GetDir())&&this.id==r.GetId());
	}
	
	public String toString(){
		return "["+this.str.replace("(", "").replace(")", "")+","+String.format("%.1f",this.time)+"]";
	}
}
