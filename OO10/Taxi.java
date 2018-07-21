package Code;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Taxi {
	/**
	 *@OVERVIEW:Taxi class save all the information for a taxi
	 *			such as id,credit,status and other contributes, it also offer method to get or set them
	 *			the most important usage of it is to move to next position according to its position and status
	 *			the map, flow map, light map are also used to decide the way to go, it is a very powerful class;^_^
	 */
	private int id;
	private int credit;
	private TaxiStatus status;
	private Point Pre;
	private Point Loc;
	private TaxiGUI gui;
	private Point SRC;
	private Point DST;
	private int WaitCount;
	private OutputStream output;
	private boolean RedLight;
	private boolean Blocked;
	private String Dir;
	public long interval;
		/**
		*@REQUIRES: id!=null,gui!=null;
		*@MODIFIES: this;
		*@EFFECTS: 
		*		true==>(A new Taxi has been constructed);
		*/	
	public Taxi(int id,TaxiGUI gui){

		this.gui=gui;
		this.id=id;
		this.credit=0;
		this.status=TaxiStatus.WAITING;
		this.WaitCount=0;
		this.interval=500;
		this.RedLight=false;
		this.Blocked=false;
		this.Dir="";
		Random rand = new Random();
		this.Loc=new Point(rand.nextInt(80), rand.nextInt(80));
		this.Pre=new Point();
		this.Pre.setLocation(this.Loc);
		try {
			this.output=new FileOutputStream(Main.f_move,true);
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
		Main.TG.SetTaxiStatus(this.id, this.Loc, this.status.GetNum());
	}
		/**
		*@EFFECTS: 
		*		(status==WAITING)==>move();
		*		(status==TAKING)==>move(SRC);
		*		(status==SERVING)==>move(DST);
		*		(status==STOP)==>stopCount();
		*/	
	public void update(){
		int x1=this.Pre.x;
		int y1=this.Pre.y;
		int x2=this.Loc.x;
		int y2=this.Loc.y;
		switch(this.status){
			case WAITING:this.move();break;
			case TAKING:this.move(this.SRC);break;
			case SERVING:this.move(this.DST);break;
			case STOP:this.stopCount();break;
			default:break;
		}
		this.Pre.setLocation(new Point(x2,y2));
		int x3=this.Loc.x;
		int y3=this.Loc.y;
//		System.out.println(x1+" "+y1+" "+x2+" "+y2+" "+x3+" "+y3+" ");
		guigv.ClearFlow(x1, y1, x2, y2, x3, y3);
		//this.gui.SetTaxiStatus(this.id,this.Loc,this.status.GetNum());
	}
		/**
		*@REQUIRES: index!=null;
		*@MODIFIES: this.credit;
		*@EFFECTS: 
		*		credit==index;
		*/	
	public void AddCredit(int index){

		this.credit+=index;
	}
		/**
		*@EFFECTS: 
		*		\result==credit;
		*/	
	public int GetCredit(){
		return this.credit;
	}
		/**
		*@REQUIRES: credit!=null;
		*@MODIFIES: this.credit;
		*@EFFECTS: 
		*		this.credit==credit;
		*/	
	public void SetCredit(int credit){
		this.credit=credit;
	}
		/**
		*@REQUIRES: x!=null,y!=null;
		*@MODIFIES: this.Loc;
		*@EFFECTS: 
		*		true==>(Loc.setLocation(x,y));
		*/	
	public void SetLoc(int x,int y){
		this.Loc.setLocation(x, y);
	}	
		/**
		*@REQUIRES: x!=null,y!=null;
		*@MODIFIES: this.Pre;
		*@EFFECTS: 
		*		true==>(Pre.setLocation(x,y));
		*/	
	public void SetPre(int x,int y){
		this.Pre.setLocation(x, y);
	}	

		/**
		*@REQUIRES: s!=null;
		*@MODIFIES: this.status;
		*@EFFECTS: 
		*		status==s;
		*/	
	public void SetStatus(TaxiStatus s){
		this.status=s;
	}
		/**
		*@EFFECTS: 
		*		\result==A certain String that contains the location of the taxi;
		*/	
	
	public String GetLoc(){
		return "("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+")";
	}	
		/**
		*@EFFECTS: 
		*		\result==Loc;
		*/	
	public Point GetLocPoint(){
		return this.Loc;
	}
		/**
		*@EFFECTS: 
		*		\result==status;
		*/	
	
	public TaxiStatus GetStatus(){
		return this.status;
	}
		/**
		*@EFFECTS: 
		*		\result==A certain array that contains the infomation of the taxi;
		*/	
	public int[] GetInfo(){
		int[] Info={this.id,(int)this.Loc.getX(),(int)this.Loc.getY()};
		return Info;
	}
		/**
		*@EFFECTS: 
		*		\result==A certain String that contains the infomation of the taxi;
		*/	
	public String TaxiInfo(){
		return "Taxi:"+this.id+" Location:("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+") Credit:"+this.credit+" Status:"+this.status+System.getProperty("line.separator");
	}
		/**
		*@EFFECTS: 
		*		\result==A certain integer that represents the location of the taxi;
		*/	
	public int getPointOrder(){
		return ((int)this.Loc.getY()*80)+(int)this.Loc.getY();
	}
		/**
		*@REQUIRES: r!=null;
		*@MODIFIES: this.SRC,this.DST,this.status,this.WaitCount;
		*@EFFECTS: 
		*		SRC==r.GetSrc();
		*		DST==r.GetDst();
		*		status==TAKING;
		*		WaitCount==0;
		*/	
	public void getOrder(Request r){
		this.SRC=r.GetSrc();
		this.DST=r.GetDst();
		this.status=TaxiStatus.TAKING;
		this.WaitCount=0;
	}
		/**
		*@MODIFIES: this.WaitCount,this.status;
		*@EFFECTS: 
		*		(WaitCount==2)==>status==WAITING;
		*		(WaitCount!=2)==>（WaitCount++);
		*/	
	public void stopCount(){
		if(this.WaitCount==2){
			this.WaitCount=0;
			this.status=TaxiStatus.WAITING;
			this.interval+=500;
			return;
		}
		this.WaitCount++;
		this.interval+=500;
	}
		/**
		*@MODIFIES: this.WaitCount,this.status,this.Loc;
		*@EFFECTS: 
		*		true==>(randomly choose a direction which has the least flow from the four available direction
		*		if the direction can be passed update the taxi's location 
		*		stop if the condition is fulfilled
		*		and update the interval);
		*/	
	public void move(){
		ArrayList<String> Dir =new ArrayList<String>();;
		int x = (int)this.Loc.getX();
		int y = (int)this.Loc.getY();
		if(this.WaitCount==40){
			this.WaitCount=0;
			this.status=TaxiStatus.STOP;
			this.interval+=500;
			return;
		}
		int flow=100;
		if(y>0&&Main.map[x*80+y][x*80+y-1]==1&&guigv.GetFlow(x, y, x, y-1)<flow){
			if(guigv.GetFlow(x, y, x, y-1)<flow){
				Dir.clear();
				Dir.add("LEFT");
				flow=guigv.GetFlow(x, y, x, y-1);
			}
			else{
				Dir.add("LEFT");
			}
		}
		if(y<79&&Main.map[x*80+y][x*80+y+1]==1&&guigv.GetFlow(x, y, x, y+1)<=flow){
			if(guigv.GetFlow(x, y, x, y+1)<flow){
				Dir.clear();
				Dir.add("RIGHT");
				flow=guigv.GetFlow(x, y, x, y+1);
			}
			else{
				Dir.add("RIGHT");
			}
		}
		if(x>0&&Main.map[x*80+y][(x-1)*80+y]==1&&guigv.GetFlow(x, y, x-1, y)<=flow){
			if(guigv.GetFlow(x, y, x-1, y)<flow){
				Dir.clear();
				Dir.add("UP");
				flow=guigv.GetFlow(x, y, x-1, y);
			}
			else{
				Dir.add("UP");
			}
		}
		if(x<79&&Main.map[x*80+y][(x+1)*80+y]==1&&guigv.GetFlow(x, y, x+1, y)<=flow){
			if(guigv.GetFlow(x, y, x+1, y)<flow){
				Dir.clear();
				Dir.add("DOWN");
				flow=guigv.GetFlow(x, y, x+1, y);
			}
			else{
				Dir.add("DOWN");
			}
		}
		Random rand = new Random();
		String Select;
		if(this.RedLight){
			if(Dir.contains(this.Dir)){
				Select=this.Dir;
				this.Blocked=false;
			}
			else{
				this.Blocked=false;
				Select = Dir.get(rand.nextInt(Dir.size()));
			}
		}
		else{
			Select = Dir.get(rand.nextInt(Dir.size()));
			this.Blocked=true;
		}
		switch(Select){
			case "UP":{
				if((Main.light[x][y]==1&&(this.Pre.x==x-1||this.Pre.x==x+1||this.Pre.y==y-1))||Main.light[x][y]==0||this.Pre.y==y+1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(x-1, y);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="UP";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}		
			}
			case "DOWN":{
				if((Main.light[x][y]==1&&(this.Pre.x==x-1||this.Pre.x==x+1||this.Pre.y==y+1))||Main.light[x][y]==0||this.Pre.y==y-1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(x+1, y);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="DOWN";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}
			}
			case "LEFT":{
				if((Main.light[x][y]==2&&(this.Pre.y==y-1||this.Pre.y==y+1||this.Pre.x==x+1))||Main.light[x][y]==0||this.Pre.x==x-1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(x, y-1);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="LEFT";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}
			}
			case "RIGHT":{
				if((Main.light[x][y]==2&&(this.Pre.y==y-1||this.Pre.y==y+1||this.Pre.x==x-1))||Main.light[x][y]==0||this.Pre.x==x+1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(x, y+1);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="RIGHT";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}
			}
			default:break;
		}
		this.WaitCount++;
	}	
		/**
		*@MODIFIES: this.WaitCount,this.status,this.Loc;
		*@EFFECTS: 
		*		true==>(randomly choose a direction which has the least flow from the four available direction
		*		if the direction can be passed update the taxi's location
		*		output the necessary information to files
		*		stop if the condition is fulfilled
		*		and update the interval);
		*/	
	public void move(Point p){
		int lx=(int)this.Loc.getX();
		int ly=(int)this.Loc.getY();
		int dx=(int)p.getX();
		int dy=(int)p.getY();
		guiInfo.pointbfs(dx*80+dy);
		int dis=guiInfo.Dd[lx*80+ly];
		if(lx==dx&&ly==dy){
			this.WaitCount++;
			if(this.status.equals(TaxiStatus.TAKING)){
				if(this.WaitCount==2){
					this.WaitCount=0;
					this.status=TaxiStatus.SERVING;
				}
			}
			try {
				String msg="Taxi:"+this.id+" arrive at "+"("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+") at "+(Main.Time+500)+System.getProperty("line.separator");
				byte data[] = msg.getBytes();
				output.write(data);
			} catch (Throwable e) {
				System.out.println("Error in Output");
				System.exit(0);
			}
			this.interval+=500;
			return;
		}
		int flow=100000;
		ArrayList<String> dir = new ArrayList<String>();
		if(ly>0&&Main.map[lx*80+ly][lx*80+ly-1]==1&&guiInfo.Dd[lx*80+ly-1]==(dis-1)&&guiInfo.flow[lx*80+ly-1]<=flow){
			if(guiInfo.flow[lx*80+ly-1]+guigv.GetFlow(lx, ly, lx, ly-1)<flow){
				dir.clear();
				dir.add("LEFT");
				flow=guiInfo.flow[lx*80+ly-1];
			}
			else{
				dir.add("LEFT");
			}
		}
		else if(ly<79&&Main.map[lx*80+ly][lx*80+ly+1]==1&&guiInfo.Dd[lx*80+ly+1]==(dis-1)&&guiInfo.flow[lx*80+ly+1]<=flow){
			if(guiInfo.flow[lx*80+ly+1]+guigv.GetFlow(lx, ly, lx, ly+1)<flow){
				dir.clear();
				dir.add("RIGHT");
				flow=guiInfo.flow[lx*80+ly+1];
			}
			else{
				dir.add("RIGHT");
			}
		}
		else if(lx>0&&Main.map[lx*80+ly][(lx-1)*80+ly]==1&&guiInfo.Dd[(lx-1)*80+ly]==(dis-1)&&guiInfo.flow[(lx-1)*80+ly]<=flow){
			if(guiInfo.flow[(lx-1)*80+ly]+guigv.GetFlow(lx, ly, lx-1, ly)<flow){
				dir.clear();
				dir.add("UP");
				flow=guiInfo.flow[(lx-1)*80+ly];
			}
			else{
				dir.add("UP");
			}
		}
		else if(lx<79&&Main.map[lx*80+ly][(lx+1)*80+ly]==1&&guiInfo.Dd[(lx+1)*80+ly]==(dis-1)&&guiInfo.flow[(lx+1)*80+ly]<=flow){
			if(guiInfo.flow[(lx+1)*80+ly]+guigv.GetFlow(lx, ly, lx+1, ly)<flow){
				dir.clear();
				dir.add("DOWN");
				flow=guiInfo.flow[(lx+1)*80+ly];
			}
			else{
				dir.add("DOWN");
			}
		}
		Random rand = new Random();
		String Select;
		if(this.RedLight){
			if(dir.contains(this.Dir)){
				Select=this.Dir;
				this.Blocked=false;
			}
			else{
				this.Blocked=false;
				Select = dir.get(rand.nextInt(dir.size()));
			}
		}
		else{
			Select = dir.get(rand.nextInt(dir.size()));
			this.Blocked=true;
		}
		switch(Select){
			case "UP":{
				if((Main.light[lx][ly]==1&&(this.Pre.x==lx-1||this.Pre.x==lx+1||this.Pre.y==ly-1))||Main.light[lx][ly]==0||this.Pre.y==ly+1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(lx-1, ly);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="UP";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}		
			}
			case "DOWN":{
				if((Main.light[lx][ly]==1&&(this.Pre.x==lx-1||this.Pre.x==lx+1||this.Pre.y==ly+1))||Main.light[lx][ly]==0||this.Pre.y==ly-1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(lx+1, ly);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="DOWN";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}
			}
			case "LEFT":{
				if((Main.light[lx][ly]==2&&(this.Pre.y==ly-1||this.Pre.y==ly+1||this.Pre.x==lx+1))||Main.light[lx][ly]==0||this.Pre.x==lx-1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(lx, ly-1);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="LEFT";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}
			}
			case "RIGHT":{
				if((Main.light[lx][ly]==2&&(this.Pre.y==ly-1||this.Pre.y==ly+1||this.Pre.x==lx-1))||Main.light[lx][ly]==0||this.Pre.x==lx+1||this.Blocked==false){
					this.RedLight=false;
					this.Loc.setLocation(lx, ly+1);this.interval=500;break;
				}
				else{
					//
					this.RedLight=true;
					this.Dir="RIGHT";
					this.interval=Main.interval-(Main.Time%Main.interval)+500;break;
				}
			}
			default:break;
		}
		lx=(int)this.Loc.getX();
		ly=(int)this.Loc.getY();
		dx=(int)p.getX();
		dy=(int)p.getY();
		if(lx==dx&&ly==dy){
			if(this.status.equals(TaxiStatus.SERVING)){
				this.status=TaxiStatus.STOP;
			}
		}
		if(this.RedLight==false){
			try {
				String msg="";
				if(this.status.equals(TaxiStatus.STOP)){
					msg="DST---Taxi:"+this.id+" arrive at "+"("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+") at "+(Main.Time+500)+System.getProperty("line.separator");
				}
				else{
					msg="Taxi:"+this.id+" arrive at "+"("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+") at "+(Main.Time+500)+System.getProperty("line.separator");
				}
				byte data[] = msg.getBytes();
				output.write(data);
			} catch (Throwable e) {
				System.out.println("Error in Output");
				System.exit(0);
			}	
		}else{
			this.interval-=500;
		}
	}
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		if(this.status==null)return false;
		if(this.status!=TaxiStatus.SERVING&&this.status!=TaxiStatus.WAITING&&this.status!=TaxiStatus.STOP&&this.status!=TaxiStatus.TAKING)return false;
		if(this.Pre==null)return false;
		if(this.Pre.x<0||this.Pre.x>79||this.Pre.y<0||this.Pre.y>79)return false;
		if(this.Loc==null)return false;
		if(this.Loc.x<0||this.Loc.x>79||this.Loc.y<0||this.Loc.y>79)return false;
		if(this.gui==null)return false;
		if(this.output==null)return false;
		return true;
	}
}
