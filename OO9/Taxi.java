package Code;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Random;

public class Taxi {
	
	private int id;
	private int credit;
	private TaxiStatus status;
	private Point Loc;
	private TaxiGUI gui;
	private Point SRC;
	private Point DST;
	private int WaitCount;
	private OutputStream output;
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
		Random rand = new Random();
		this.Loc=new Point(rand.nextInt(80), rand.nextInt(80));
		try {
			this.output=new FileOutputStream(Main.f_move,true);
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
	}
		/**
		*@EFFECTS: 
		*		(status==WAITING)==>move();
		*		(status==TAKING)==>move(SRC);
		*		(status==SERVING)==>move(DST);
		*		(status==STOP)==>stopCount();
		*/	
	public void update(){
		switch(this.status){
			case WAITING:this.move();break;
			case TAKING:this.move(this.SRC);break;
			case SERVING:this.move(this.DST);break;
			case STOP:this.stopCount();break;
			default:break;
		}
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
			return;
		}
		this.WaitCount++;
	}
		/**
		*@MODIFIES: this.WaitCount,this.status,this.Loc;
		*@EFFECTS: 
		*		true==>(randomly choose a direction which has the least flow from the four available direction
		*		and update the taxi's location
		*		stop if the condition is fulfilled);
		*/	
	public void move(){
		ArrayList<String> Dir =new ArrayList<String>();;
		int x = (int)this.Loc.getX();
		int y = (int)this.Loc.getY();
		if(this.WaitCount==40){
			this.WaitCount=0;
			this.status=TaxiStatus.STOP;
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
		String Select = Dir.get(rand.nextInt(Dir.size()));
		switch(Select){
			case "UP":this.Loc.setLocation(x-1, y);break;
			case "DOWN":this.Loc.setLocation(x+1, y);break;
			case "LEFT":this.Loc.setLocation(x, y-1);break;
			case "RIGHT":this.Loc.setLocation(x, y+1);break;
			default:break;
		}
		this.WaitCount++;
	}	
		/**
		*@MODIFIES: this.WaitCount,this.status,this.Loc;
		*@EFFECTS: 
		*		true==>(randomly choose a direction which has the least flow from the four available direction
		*		and update the taxi's location,output the necessary information to files
		*		stop if the condition is fulfilled);
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
			this.Loc.setLocation(lx, ly-1);
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
			this.Loc.setLocation(lx, ly+1);
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
			this.Loc.setLocation(lx-1, ly);
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
			this.Loc.setLocation(lx+1, ly);
		}
		Random rand = new Random();
		String Select = dir.get(rand.nextInt(dir.size()));
		switch(Select){
			case "UP":this.Loc.setLocation(lx-1, ly);break;
			case "DOWN":this.Loc.setLocation(lx+1, ly);break;
			case "LEFT":this.Loc.setLocation(lx, ly-1);break;
			case "RIGHT":this.Loc.setLocation(lx, ly+1);break;
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
	}
}
