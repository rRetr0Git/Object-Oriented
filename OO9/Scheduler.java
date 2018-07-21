package Code;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class Scheduler extends Thread{
	
	private RequestList RL;
	private TaxiSquad TS;
	private MapShot MS;
	private int first;
	private Taxi[] Squad;
	private OutputStream output;
		/**
		*@REQUIRES: RL!=null,TS!=null,MS!=null;
		*@MODIFIES: this;
		*@EFFECTS: 
		*		true==>(A new Scheduler has been constructed);
		*/	
	public Scheduler(RequestList RL,TaxiSquad TS,MapShot MS){
		this.RL=RL;
		this.TS=TS;
		this.MS=MS;
		this.first=1;
		this.Squad=this.TS.ReturnSquad();
		try {
			this.output=new FileOutputStream(Main.f_win,true);
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
	}
		/**
		*@MODIFIES: System.out,this.output,this.RL,this.Squad;
		*@EFFECTS: 
		*		normal_behavior
		*		(\exist int i;0<=i<RL.size();due(RL.get(i)))==>(Select(RL.get(i))!=-1)
		*			==>(Squad[Select(RL.get(i))].getOrder(RL.get(i)))&&(RL.remove(i));
		*		(\exist int i;0<=i<RL.size();due(RL.get(i)))==>(Select(RL.get(i))==-1)
		*			==>(RL.remove(i));
		*		(\exist int i;0<=i<RL.size();!due(RL.get(i)))==>search(RL.get(i));
		*/	
	public void dispatch(){
		for(int i=0;i<this.RL.size();i++){
			if(this.first==1){
				Main.Time=this.RL.get(i).GetTime();
				this.first=0;
			}
			if(due(this.RL.get(i))){
				int id=Select(this.RL.get(i));
				if(id!=-1){
					try {
						String msg=this.RL.get(i).toString()+" Window at "+Main.Time+System.getProperty("line.separator");
						byte data[] = msg.getBytes();
						output.write(data);
						for(int j=0;j<this.RL.get(i).getList().size();j++){
							msg=this.Squad[this.RL.get(i).getList().get(j)].TaxiInfo();
							data= msg.getBytes();
							output.write(data);
						}
						msg="Dispatch to:Taxi "+id+System.getProperty("line.separator")+System.getProperty("line.separator");
						data= msg.getBytes();
						output.write(data);
					} catch (Throwable e) {
						System.out.println("Error in Output");
						System.exit(0);
					}
					this.Squad[id].getOrder(this.RL.get(i));
					System.out.println("Taxi:"+id+" get the order "+this.RL.get(i).toString());
					this.RL.remove(i);
					i--;
				}
				else{
					System.out.println("No Taxi Respond "+this.RL.get(i).toString());
					this.RL.remove(i);
					i--;
				}
			}
			else{
				search(this.RL.get(i));
			}
		}
	}
		/**
		*@REQUIRES: r!=null;
		*@EFFECTS: 
		*		\result==The taxi id in the taxiList to dispatch the request;
		*/	
	public int Select(Request r){
		int fid=-1;
		int credit=-1;
		int distance=10000000;
		int begin=1;
		for(int i=0;i<r.getList().size();i++){
			int id=r.getList().get(i);
			int status=this.Squad[id].GetStatus().GetNum();
			int tcredit=this.Squad[id].GetCredit();
			if(status==2&&tcredit>credit){
				if(begin==1){
					guiInfo.pointbfs(((int)r.GetSrc().getX())*80+(int)r.GetSrc().getY());
					begin=0;
				}
				int tdistance=guiInfo.Dd[this.Squad[id].getPointOrder()];
				fid=id;
				credit=tcredit;
				distance=tdistance;	
			}
			else if(status==2&&tcredit==credit){
				int tdistance=guiInfo.Dd[this.Squad[id].getPointOrder()];
				if(tdistance<distance){
					fid=id;
					credit=tcredit;
					distance=tdistance;
				}
			}
		}
		return fid;	
	}
		/**
		*@REQUIRES:r!=null;
		*@EFFECTS: 
		*		(Time-r.GetTime()>=7500)==>(\result==true);
		*		(!(Time-r.GetTime()>=7500))==>(\result==false);
		*/	
	public boolean due(Request r){
		if(Main.Time-r.GetTime()>=7500){
			return true;
		}
		return false;
	}
		/**
		*@REQUIRES: r!=null;
		*@MODIFIES: this.MS,this.Squad,r;
		*@EFFECTS: 
		*		true==>(Search in the range of the request(i,j,k));
		*		(MS.GetId(i,j,k)!=-1)==>(Squad[MS.GetId(i,j,k)].GetStatus==WAITING)
		*			==>(r.contains(MS.GetId(i,j,k)))&&(Squad[MS.GetId(i,j,k)].AddCredit(1));
		*/	
	public void search(Request r){
		Point SRC = r.GetSrc();
		int x=(int)SRC.getX();
		int y=(int)SRC.getY();
		for(int i=x-2;i<=x+2;i++){
			for(int j=y-2;j<=y+2;j++){
				if(i>=0&&i<=79&&j>=0&&j<=79){
					for(int k=0;k<this.MS.ValidLength(i,j);k++){
						int id=this.MS.GetId(i,j,k);
						if(id!=-1&&this.Squad[id].GetStatus()==(TaxiStatus.WAITING)){
							if(r.addTaxi(id)){
								this.Squad[id].AddCredit(1);
							}
						}	
					}
				}
			}
		}
	}
		/**
		*@MODIFIES: System.out;
		*@EFFECTS: 
		*		true==>(do the things below in an endless loop)==>(wait())==>(dispatch());
		*/	
	public void run(){
		while(true){
			try {
				synchronized(this.MS){
					this.MS.wait();
					Main.Time+=500;
					this.dispatch();
				}
				//System.out.println(System.currentTimeMillis());
			} catch (Throwable e) {
				System.out.println("Error in Scheduler");
				System.exit(0);
			}
		}
	}
}
