package Code;

import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

public class Elevator extends Thread implements SchedulableCarrier {
	private int id;
	private ElevatorStatus status;
	private int currentfloor;
	private Direction dir;
	private int SumWork;
	private long Time;
	private RequestQueue RQ;
	private int flag;
	private RequestQueue BRQ;
	private boolean END;
	private PrintStream ps;
	
	public Elevator(int id,RequestQueue BRQ,PrintStream ps){
		this.id=id;
		this.status=ElevatorStatus.IDLE;
		this.currentfloor=1;
		this.SumWork=0;
		this.flag=0;
		this.Time=System.currentTimeMillis();
		this.RQ=new RequestQueue();
		this.BRQ=BRQ;
		this.END=false;
		this.ps=ps;
		System.setOut(this.ps);
	}
	
	
	public ElevatorStatus GetStatus(){
		return this.status;
	}
	public void SetStatus(ElevatorStatus es){
		this.status=es;
	}
	public void SetFloor(int index){
		this.currentfloor=index;
	}
	
	public int GetFloor(){
		return this.currentfloor;
	}
	public Direction GetDir(){
		return this.dir;
	}
	public void Work(){
		this.SumWork++;
	}
	public int GetWork(){
		return this.SumWork;
	}
	public RequestQueue GetQueue(){
		return this.RQ;
	}
		
	public void run(){
		while(!this.END){
			int num=0;
			while(this.RQ.size()==0){
				try{
					this.status=ElevatorStatus.IDLE;
					synchronized(this.BRQ){
						this.BRQ.notifyAll();	
					}
					if(num!=2){
						synchronized(this.RQ){
							this.RQ.wait(1);
						}
						num++;
					}
					else{
						if(this.BRQ.size()>0&&this.BRQ.get(0).GetDst()==0){
							return;
						}
						synchronized(this.RQ){
							this.RQ.wait(5000);
						}
					}
				}catch(InterruptedException e){
					System.exit(0);
				}
			}
			SetDir(this.RQ.get(0));
			synchronized(this.BRQ){
				this.BRQ.notifyAll();	
			}
			synchronized(this.RQ){
				execute(this.RQ.get(0));
			}
		}
	}
	public void SetDir(Request r){
		if(r.GetDst()>this.currentfloor){
			this.dir=Direction.UP;
		}
		else if(r.GetDst()<this.currentfloor){
			this.dir=Direction.DOWN;
		}
		else{
			this.dir=Direction.STILL;
		}
	}
	
	public void execute(Request r){
		if(flag==0){
			this.Time=r.GetBeginTime();
			flag=1;
		}
		if(r.GetDst()>this.currentfloor){
			this.dir=Direction.UP;
			MoveUp(r);
		}
		else if(r.GetDst()<this.currentfloor){
			this.dir=Direction.DOWN;
			MoveDown(r);
		}
		else{
			this.dir=Direction.STILL;
			Stay(r);
		}
		this.RQ.remove(0);
	}
	
	
	public void MoveUp(Request r){
		for(int i=this.currentfloor;i<r.GetDst();i++){
			try{
				this.RQ.wait(2999);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			this.currentfloor++;
			this.SumWork++;
			if(this.RQ.size()>1){
				int Flag=0;
				for(int j=1;j<this.RQ.size();j++){
					if(this.currentfloor==this.RQ.get(j).GetDst()){
						Flag=1;
						long time=System.currentTimeMillis();
						synchronized(this.ps){
							System.out.println(time+":"+this.RQ.get(j).toString()+"/"+this.toString());	
						}
						//this.RQ.remove(j);j--;
					}
				}
				if(Flag==1&&this.currentfloor!=r.GetDst()){
					try{
						this.RQ.wait(5999);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}	
				}	
				for(int j=1;j<this.RQ.size();j++){
					if(this.currentfloor==this.RQ.get(j).GetDst()){
						this.RQ.remove(j);j--;
					}
				}
			}
		}
		long time=System.currentTimeMillis();
		synchronized(this.ps){
			System.out.println(time+":"+r.toString()+"/"+this.toString());
		}
		try{
			this.RQ.wait(5999);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void MoveDown(Request r){
		for(int i=this.currentfloor;i>r.GetDst();i--){
			try{
				this.RQ.wait(2999);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
			this.currentfloor--;
			this.SumWork++;
			if(this.RQ.size()>1){
				int Flag=0;
				for(int j=1;j<this.RQ.size();j++){
					if(this.currentfloor==this.RQ.get(j).GetDst()){
						Flag=1;
						long time=System.currentTimeMillis();
						synchronized(this.ps){
							System.out.println(time+":"+this.RQ.get(j).toString()+"/"+this.toString());
						}
						//this.RQ.remove(j);j--;
					}
				}
				if(Flag==1&&this.currentfloor!=r.GetDst()){
					try{
						this.RQ.wait(5999);
					}catch(InterruptedException e) {
						e.printStackTrace();
					}	
				}	
				for(int j=1;j<this.RQ.size();j++){
					if(this.currentfloor==this.RQ.get(j).GetDst()){
						this.RQ.remove(j);j--;
					}
				}
			}
		}
		long time=System.currentTimeMillis();
		synchronized(this.ps){
			System.out.println(time+":"+r.toString()+"/"+this.toString());
		}
		try{
			this.RQ.wait(5999);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void Stay(Request r){
		try{//
			this.RQ.wait(5999);
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
		long time=System.currentTimeMillis();
		synchronized(this.ps){
			System.out.println(time+":"+r.toString()+"/"+this.toString());
		}
	}
	
	public String toString(){
		long time=System.currentTimeMillis();
		return "(#"+this.id+","+this.currentfloor+","+this.dir+","+this.SumWork+","+ String.format("%.1f", (double)(time-this.Time)/1000)+")";
	}
}
