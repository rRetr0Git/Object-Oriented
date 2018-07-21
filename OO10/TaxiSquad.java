package Code;

import java.awt.Point;

public class TaxiSquad extends Thread{
	 /**
	 *@OVERVIEW: TaxiSquad is a set of 100 taxis and a MapShot
	 *			which contains every taxi's information
	 *			TaxiSquad helps to move and update every taxi;
	 */
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
		*@REQUIRES:id!=null,0<id<100;
		*@EFFECTS: 
		*		normal_behavior
		*		\result==squad[id];
		*		index>\old(list).size()||index<0==>exceptional_behavior(Exception);
		*/	
	public Taxi get(int id){
		return this.squad[id];
	}
		/**
		*@REQUIRES:interval!=null,0<interval<1500;
		*@MODIFIES:this.squad,this.MS,this.TG;
		*@EFFECTS:
		*		true==>(MS.Clear());
		*		(\all int i;0<=i<100)==>squad[i].update()&&MS.Set(squad[i].GetInfo);
		*		(\all int i;0<=i<100)==>TG.SetTaxiStatus(i,squad[i].GetLocPoint(),squad[i].GetStatus().GetNum());
		*		(\result==(the smallest interval of every taxi));
		*/	
	public long Update(long interval){
		MS.Clear();
		for(int i=0;i<100;i++){
			this.squad[i].interval-=interval;
			if(this.squad[i].interval==0){
				this.squad[i].update();
			}
			int[] Info=this.squad[i].GetInfo();
			MS.Set(Info);
		}
		for(int i=0;i<100;i++){
			Main.TG.SetTaxiStatus(i,this.squad[i].GetLocPoint(),this.squad[i].GetStatus().GetNum());
		}
		long min_interval=2000;
		for(int i=0;i<100;i++){
			if(this.squad[i].interval<min_interval){
				min_interval=this.squad[i].interval;
			}
		}
		//System.out.println(min_interval);
		return min_interval;
		//refresh every taxi's interval and choose the next interval
	}
		/**
		*@MODIFIES: System.out,this.Time,interval;
		*@EFFECTS: 
		*			true==>(wait for a certain interval)==>(update certain cars' status and select the next interval)
		*				==>(Time=\old Time+interval)==>(refreshRoad())==>(notifyAll());
		*			\result==(the next interval);
		*@THREAD_EFFECTS: this.MS;
		*/	
	public void run(){
		long begin=0,end=0,interval=500;
		int first=1;
		while(true){
			try {
				synchronized(this.MS){
					if(interval<10){
						this.MS.wait(10);
					}
					else{
						this.MS.wait(interval);
					}	
					Main.LC.interval-=interval;
					if(Main.LC.interval==0){
//						System.out.println("notify lc:"+Main.Time);
						synchronized(Main.LC){
							Main.LC.notify();
						}
						Main.LC.interval=Main.interval;
					}
//					long a=500-(end-begin);
//					if(a>0){
//						this.MS.wait(500-(end-begin));
//					}
//					else{
//						this.MS.wait(10);
//					}
//					begin=System.currentTimeMillis();
					
					interval=this.Update(interval);
					if(first==1){
						first=0;
						for(int i=0;i<Main.loadflow.size();i++){
							String[] element=Main.loadflow.get(i).split(" ");
							int a=Integer.parseInt(element[0]);
							int b=Integer.parseInt(element[1]);
							int c=Integer.parseInt(element[2]);
							int d=Integer.parseInt(element[3]);
							int e=Integer.parseInt(element[4]);
							while(e!=0){
								if(guigv.GetFlow(a,b,c,d)>1){
									guigv.flowmap.put(guigv.Key(a,b,c,d), guigv.GetFlow(a,b,c,d)-1);
									guigv.flowmap.put(guigv.Key(c,d,a,b), guigv.GetFlow(c,d,a,b)-1);
								}
								else if(guigv.GetFlow(a,b,c,d)==1){
									guigv.flowmap.remove(guigv.Key(a,b,c,d));
									guigv.flowmap.remove(guigv.Key(c,d,a,b));
								}
								e--;
							}
						}
					}
					if(interval>Main.LC.interval){
						interval=Main.LC.interval;
					}
					Main.Time+=interval;
//					end=System.currentTimeMillis();
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
		*@REQUIRES:id!=null,0<=id<100;
		*@MODIFIES:System.out
		*@EFFECTS: 
		*		normal_behavior
		*		(0<=id<100)==>\result==A certain String that contains the information of certain taxi;
		*		id>\old(list).size()||id<0==>exceptional_behavior(Exception);
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
		*@REQUIRES:status!=null,status=TaxiStatus;
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
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		if(this.squad==null)return false;
		for(int i=0;i<100;i++){
			if(!this.squad[i].repOK())return false;
		}
		if(this.gui==null)return false;
		if(this.MS==null)return false;
		if(!this.MS.repOK())return false;
		return true;
	}
}
