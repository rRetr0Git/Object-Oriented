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
	
	public void update(){
		switch(this.status){
			case WAITING:this.move();break;
			case TAKING:this.move(this.SRC);break;
			case SERVING:this.move(this.DST);break;
			case STOP:this.stopCount();break;
			default:break;
		}
		this.gui.SetTaxiStatus(this.id,this.Loc,this.status.GetNum());
	}
	
	public void AddCredit(int index){
		this.credit+=index;
	}
	
	public int GetCredit(){
		return this.credit;
	}
	
	public String GetLoc(){
		return "("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+")";
	}
	
	public TaxiStatus GetStatus(){
		return this.status;
	}
	public int[] GetInfo(){
		int[] Info={this.id,(int)this.Loc.getX(),(int)this.Loc.getY()};
		return Info;
	}
	
	public String TaxiInfo(){
		return "Taxi:"+this.id+" Location:("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+") Credit:"+this.credit+" Status:"+this.status+System.getProperty("line.separator");
	}
	
	public int getDistance(Point p){
		int x=(int)this.Loc.getX();
		int y=(int)this.Loc.getY();
		int dx=(int)p.getX();
		int dy=(int)p.getY();
		return Main.distance[80*x+y][80*dx+dy];
	}
	
	public void getOrder(Request r){
		this.SRC=r.GetSrc();
		this.DST=r.GetDst();
		this.status=TaxiStatus.TAKING;
		this.WaitCount=0;
	}
	public void stopCount(){
		if(this.WaitCount==5){
			this.WaitCount=0;
			this.status=TaxiStatus.WAITING;
			return;
		}
		this.WaitCount++;
	}
	public void move(){
		ArrayList<String> Dir = new ArrayList<String>();
		int x = (int)this.Loc.getX();
		int y = (int)this.Loc.getY();
		if(this.WaitCount==100){
			this.WaitCount=0;
			this.status=TaxiStatus.STOP;
			return;
		}
		if(y>0&&Main.map[x*80+y][x*80+y-1]==1){
			Dir.add("LEFT");
		}
		if(y<79&&Main.map[x*80+y][x*80+y+1]==1){
			Dir.add("RIGHT");
		}
		if(x>0&&Main.map[x*80+y][(x-1)*80+y]==1){
			Dir.add("UP");
		}
		if(x<79&&Main.map[x*80+y][(x+1)*80+y]==1){
			Dir.add("DOWN");
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
	
	public void move(Point p){
		int lx=(int)this.Loc.getX();
		int ly=(int)this.Loc.getY();
		int dx=(int)p.getX();
		int dy=(int)p.getY();
		int dis=Main.distance[lx*80+ly][dx*80+dy];
		if(lx==dx&&ly==dy){
			this.WaitCount++;
			if(this.status.equals(TaxiStatus.TAKING)){
				if(this.WaitCount==5){
					this.WaitCount=0;
					this.status=TaxiStatus.SERVING;
				}
			}
			try {
				String msg="Taxi:"+this.id+" arrive at "+"("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+") at "+(Main.Time+200)+System.getProperty("line.separator");
				byte data[] = msg.getBytes();
				output.write(data);
			} catch (Throwable e) {
				System.out.println("Error in Output");
				System.exit(0);
			}
			return;
		}
		if(ly>0&&Main.map[lx*80+ly][lx*80+ly-1]==1&&Main.distance[lx*80+ly-1][dx*80+dy]==(dis-1)){
			this.Loc.setLocation(lx, ly-1);
		}
		else if(ly<79&&Main.map[lx*80+ly][lx*80+ly+1]==1&&Main.distance[lx*80+ly+1][dx*80+dy]==(dis-1)){
			this.Loc.setLocation(lx, ly+1);
		}
		else if(lx>0&&Main.map[lx*80+ly][(lx-1)*80+ly]==1&&Main.distance[(lx-1)*80+ly][dx*80+dy]==(dis-1)){
			this.Loc.setLocation(lx-1, ly);
		}
		else if(lx<79&&Main.map[lx*80+ly][(lx+1)*80+ly]==1&&Main.distance[(lx+1)*80+ly][dx*80+dy]==(dis-1)){
			this.Loc.setLocation(lx+1, ly);
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
			String msg="Taxi:"+this.id+" arrive at "+"("+(int)this.Loc.getX()+","+(int)this.Loc.getY()+") at "+(Main.Time+200)+System.getProperty("line.separator");
			byte data[] = msg.getBytes();
			output.write(data);
		} catch (Throwable e) {
			System.out.println("Error in Output");
			System.exit(0);
		}
	}
}
