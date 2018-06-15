package Code;

import java.awt.Point;

public class TaxiSquad extends Thread{
	private Taxi[] squad;
	private TaxiGUI gui;
	private MapShot MS;
	
	public TaxiSquad(TaxiGUI gui,MapShot MS){
		this.gui=gui;
		this.MS=MS;
		this.squad=new Taxi[100];
		for(int i=0;i<100;i++){
			this.squad[i]=new Taxi(i,gui);
			this.squad[i].update();
		}
	}
	
	public Taxi get(int id){
		return this.squad[id];
	}
	
	public void Update(){
		MS.Clear();
		for(int i=0;i<100;i++){
			this.squad[i].update();
			int[] Info=this.squad[i].GetInfo();
			MS.Set(Info);
		}
	}
	
	public void run(){
		while(true){
			try {
				synchronized(this.MS){
					this.MS.wait(200);
					this.Update();
					this.MS.notifyAll();
				}
			} catch (Throwable e) {
				System.out.println("Error in TaxiSquad");
				System.exit(0);
			}	
		}	
	}
	
	public void GetInfo(int id){
		if(id>=0&&id<100){
			System.out.println(Main.Time+":"+this.squad[id].GetLoc()+" "+this.squad[id].GetStatus());
		}
		else{
			System.out.println("Invalid ID");
		}
	}
	
	public void GetTaxiOf(TaxiStatus status){
		int[] list=new int[100];
		int count=0;
		for(int i=0;i<100;i++){
			if(this.squad[i].GetStatus().equals(status)){
				list[count]=i;
				count++;
			}
		}
		if(count>0){
			for(int i=0;i<count;i++){
				System.out.print(list[i]+" ");
			}
			System.out.println();
		}
	}
	
	public Taxi[] ReturnSquad(){
		return this.squad;
	}
}
