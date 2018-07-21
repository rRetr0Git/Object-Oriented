package Code;

import java.awt.Point;

public class TaxiSquad extends Thread{
	private Taxi[] squad;
	private TaxiGUI gui;
	private MapShot MS;
		/**
		*@REQUIRES:gui!=null,MS!=null;
		*@MODIFIES:this;
		*@EFFECTS:
		*		true==>(A new TaxiSquad has been constructed);
		*/	
	public TaxiSquad(TaxiGUI gui,MapShot MS){
		this.gui=gui;
		this.MS=MS;
		this.squad=new Taxi[100];
		for(int i=0;i<100;i++){
			this.squad[i]=new Taxi(i,gui);
			//this.squad[i].update();
		}
	}
		/**
		*@REQUIRES:id!=null;
		*@EFFECTS: 
		*		\result==squad[id];
		*/	
	public Taxi get(int id){
		return this.squad[id];
	}
		/**
		*@MODIFIES:this.squad,this.MS,this.TG;
		*@EFFECTS:
		*		true==>(MS.Clear());
		*		(\all int i;0<=i<100)==>squad[i].update()&&MS.Set(squad[i].GetInfo);
		*		(\all int i;0<=i<100)==>TG.SetTaxiStatus(i,squad[i].GetLocPoint(),squad[i].GetStatus().GetNum());
		*/	
	public void Update(){
		MS.Clear();
		for(int i=0;i<100;i++){
			this.squad[i].update();
			int[] Info=this.squad[i].GetInfo();
			MS.Set(Info);
		}
		for(int i=0;i<100;i++){
			Main.TG.SetTaxiStatus(i,this.squad[i].GetLocPoint(),this.squad[i].GetStatus().GetNum());
		}
	}
		/**
		*@MODIFIES: System.out;
		*@EFFECTS: 
		*			true==>(wait())==>(ClearFlow())==>(Update())==>(refreshRoad())==>(notifyAll());
		*/	
	public void run(){
		long begin=0,end=0;
		while(true){
			try {
				synchronized(this.MS){
					long a=500-(end-begin);
					if(a>0){
						this.MS.wait(500-(end-begin));
					}
					else{
						this.MS.wait(10);
					}
					guigv.ClearFlow();
					begin=System.currentTimeMillis();
					this.Update();
					end=System.currentTimeMillis();
					this.refreshRoad();
					this.MS.notifyAll();
				}
			} catch (Throwable e) {
				System.out.println("Error in TaxiSquad");
				System.exit(0);
			}	
		}	
	}
		/**
		*@MODIFIES: this.list,this.map,this.TG;
		*@EFFECTS: 
		*		(list.size()>0)==>map.update()&&TG.SetRoadStatus()&&list.remove();
		*/	
	public void refreshRoad(){
		synchronized(InputHandler.list){
			while(InputHandler.list.size()>0){
				String[] element = InputHandler.list.get(0).split(" ");
				int A = Integer.parseInt(element[0]);
				int B = Integer.parseInt(element[1]);
				int C = Integer.parseInt(element[2]);
				if(C==1){
					Main.map[A][B]=1;
					Main.map[B][A]=1;
					Point c = new Point(A/80,A%80);
					Point d = new Point(B/80,B%80);
					Main.TG.SetRoadStatus(c, d, 1);
				}
				else{
					Main.map[A][B]=1000000;
					Main.map[B][A]=1000000;
					Point c = new Point(A/80,A%80);
					Point d = new Point(B/80,B%80);
					Main.TG.SetRoadStatus(c, d, 0);
				}
				InputHandler.list.remove(0);
			}
		}
	}
		/**
		*@REQUIRES:id!=null;
		*@MODIFIES:System.out
		*@EFFECTS: 
		*		(0<=id<100)==>\result==A certain String that contains the information of certain taxi;
		*/	
	public void GetInfo(int id){
		if(id>=0&&id<100){
			System.out.println(Main.Time+":"+this.squad[id].GetLoc()+" "+this.squad[id].GetStatus());
		}
		else{
			System.out.println("Invalid ID");
		}
	}
		/**
		*@REQUIRES:status!=null;
		*@MODIFIES:System.out
		*@EFFECTS: 
		*		true==>(search in the TaxiSquad and output the id of taxis that in the certain status);
		*/	
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
		/**
		*@EFFECTS: 
		*		\result==squad;
		*/	
	public Taxi[] ReturnSquad(){
		return this.squad;
	}
}
