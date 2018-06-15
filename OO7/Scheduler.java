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
	
	public int Select(Request r){
		int fid=-1;
		int credit=-1;
		int distance=10000000;
		for(int i=0;i<r.getList().size();i++){
			int id=r.getList().get(i);
			int status=this.Squad[id].GetStatus().GetNum();
			int tcredit=this.Squad[id].GetCredit();
			int tdistance=this.Squad[id].getDistance(r.GetSrc());
			if(status==2&&tcredit>credit){
				fid=id;
				credit=tcredit;
				distance=tdistance;	
			}
			else if(status==2&&tcredit==credit){
				if(tdistance<distance){
					fid=id;
					credit=tcredit;
					distance=tdistance;
				}
			}
		}
		return fid;
	}
	
	public boolean due(Request r){
		if(Main.Time-r.GetTime()>=3000){
			return true;
		}
		return false;
	}
	
	public void search(Request r){
		Point SRC = r.GetSrc();
		int x=(int)SRC.getX();
		int y=(int)SRC.getY();
		for(int i=x-2;i<=x+2;i++){
			for(int j=y-2;j<=y+2;j++){
				if(i>=0&&i<=79&&j>=0&&j<=79){
					//
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
	
	public void run(){
		while(true){
			try {
				synchronized(this.MS){
					this.MS.wait();
					Main.Time+=200;
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
