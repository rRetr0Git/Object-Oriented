package Code;

import java.io.PrintStream;

public class Scheduler extends Scheduler_original implements Runnable{
	private Elevator e1;
	private Elevator e2;
	private Elevator e3;
	private RequestQueue RQ;
	private InputHandler IH;
	private boolean END;
	private PrintStream ps;
	
	public Scheduler(RequestQueue RQ,Elevator e1,Elevator e2,Elevator e3,InputHandler IH,PrintStream ps){
		this.RQ=RQ;
		this.e1=e1;
		this.e2=e2;
		this.e3=e3;
		this.IH=IH;
		this.END=false;
		this.ps=ps;
		System.setOut(this.ps);
	}

	public void run(){
		while(!this.END){
			synchronized(RQ){
				while(this.RQ.size()==0){
					try{
						RQ.wait();
					}catch(InterruptedException e){
						System.exit(0);
					}	
				}
				if(this.RQ.get(0).GetDst()==0){
					this.END=true;
					break;
				}

				Same(this.RQ);

				Dispatch(this.RQ);

				try{
					RQ.wait();
				}catch(InterruptedException e){
					System.exit(0);
				}
			}
		}
	}
	
	public void Same(RequestQueue rq){
		int flag=0;
		for(int i=0;i<rq.size();i++){
			flag=0;
			synchronized(this.e1.GetQueue()){
				for(int j=0;j<this.e1.GetQueue().size();j++){
					if(rq.get(i).equals(this.e1.GetQueue().get(j))){
						synchronized(this.ps){
							System.out.println("#"+System.currentTimeMillis()+":SAME"+rq.get(i).toString());
						}
						rq.remove(i);
						flag=1;i--;
						break;
					}
				}
			}
			if(flag==0){
				synchronized(this.e2.GetQueue()){
					for(int j=0;j<this.e2.GetQueue().size();j++){
						if(rq.get(i).equals(this.e2.GetQueue().get(j))){
							synchronized(this.ps){
								System.out.println("#"+System.currentTimeMillis()+":SAME"+rq.get(i).toString());
							}
							rq.remove(i);
							flag=1;i--;
							break;
						}
					}
				}		
			}
			if(flag==0){
				synchronized(this.e3.GetQueue()){
					for(int j=0;j<this.e3.GetQueue().size();j++){
						if(rq.get(i).equals(this.e3.GetQueue().get(j))){
							synchronized(this.ps){
								System.out.println(System.currentTimeMillis()+":#SAME"+rq.get(i).toString());
							}
							rq.remove(i);
							flag=1;i--;
							break;
						}
					}
				}
			}
		}
	}
	
	public void Dispatch(RequestQueue rq){
		for(int i=0;i<rq.size();i++){
			if(rq.get(0).GetType().equals("END")){
				break;
			}
			if(rq.get(i).GetType().equals("ER")){
				switch(rq.get(i).GetId()){
					case 1:{
						if((rq.get(i).GetDst()>this.e1.GetFloor()&&this.e1.GetDir()==Direction.UP)
								||(rq.get(i).GetDst()<this.e1.GetFloor()&&this.e1.GetDir()==Direction.DOWN)
								||this.e1.GetStatus()==ElevatorStatus.IDLE){
							synchronized(this.e1.GetQueue()){
								
								this.e1.GetQueue().add(this.RQ.get(i));
								this.RQ.remove(i);i--;
								if(e1.GetStatus()==ElevatorStatus.IDLE){
									this.e1.SetStatus(ElevatorStatus.RUNNING);
									this.e1.GetQueue().notifyAll();
								}		
							}
						}
//						else{
//							System.out.println("NOT DISPATCH "+rq.get(i).toString());
//						}
					}break;
					case 2:{
						if((rq.get(i).GetDst()>this.e2.GetFloor()&&this.e2.GetDir()==Direction.UP)
								||(rq.get(i).GetDst()<this.e2.GetFloor()&&this.e2.GetDir()==Direction.DOWN)
								||this.e2.GetStatus()==ElevatorStatus.IDLE){
							synchronized(this.e2.GetQueue()){
								
								this.e2.GetQueue().add(this.RQ.get(i));
								this.RQ.remove(i);i--;
								if(e2.GetStatus()==ElevatorStatus.IDLE){
									this.e2.SetStatus(ElevatorStatus.RUNNING);
									this.e2.GetQueue().notifyAll();
								}		
							}
						}
//						else{
//							System.out.println("NOT DISPATCH "+rq.get(i).toString());
//						}
					}break;
					case 3:{
						if((rq.get(i).GetDst()>this.e3.GetFloor()&&this.e3.GetDir()==Direction.UP)
								||(rq.get(i).GetDst()<this.e3.GetFloor()&&this.e3.GetDir()==Direction.DOWN)
								||this.e3.GetStatus()==ElevatorStatus.IDLE){
							synchronized(this.e3.GetQueue()){
								
								this.e3.GetQueue().add(this.RQ.get(i));
								this.RQ.remove(i);i--;
								if(e3.GetStatus()==ElevatorStatus.IDLE){
									this.e3.SetStatus(ElevatorStatus.RUNNING);
									this.e3.GetQueue().notifyAll();
								}		
							}
						}
//						else{
//							System.out.println("NOT DISPATCH "+rq.get(i).toString());
//						}
					}break;
				}	
			}
			else{
				boolean h1=Hitch(this.e1,rq.get(i));
				boolean h2=Hitch(this.e2,rq.get(i));
				boolean h3=Hitch(this.e3,rq.get(i));
				if(h1||h2||h3){
					int order = SelectHitch(h1,h2,h3);
					switch(order){
						case 1:
							synchronized(this.e1.GetQueue()){	
								this.e1.GetQueue().add(this.RQ.get(i));
								this.RQ.remove(i);i--;
								if(e1.GetStatus()==ElevatorStatus.IDLE){
									this.e1.SetStatus(ElevatorStatus.RUNNING);
									this.e1.GetQueue().notifyAll();
								}
							}break;
						case 2:
							synchronized(this.e2.GetQueue()){	
								
								this.e2.GetQueue().add(this.RQ.get(i));
								this.RQ.remove(i);i--;
								if(e2.GetStatus()==ElevatorStatus.IDLE){
									this.e2.SetStatus(ElevatorStatus.RUNNING);
									this.e2.GetQueue().notifyAll();
								}
							}break;
						case 3:
							synchronized(this.e3.GetQueue()){	
								this.e3.GetQueue().add(this.RQ.get(i));
								this.RQ.remove(i);i--;
								if(e3.GetStatus()==ElevatorStatus.IDLE){
									this.e3.SetStatus(ElevatorStatus.RUNNING);
									this.e3.GetQueue().notifyAll();
								}
							}break;
					}
				}
				else{
					switch(SelectIdle()){
					case 0:
//						System.out.println("NOT DISPATCH "+rq.get(i).toString());
						break;
					case 1:
						synchronized(this.e1.GetQueue()){	
							this.e1.GetQueue().add(this.RQ.get(i));
							this.RQ.remove(i);i--;
							if(e1.GetStatus()==ElevatorStatus.IDLE){
								this.e1.SetStatus(ElevatorStatus.RUNNING);
								this.e1.GetQueue().notifyAll();
							}
						}break;
					case 2:
						synchronized(this.e2.GetQueue()){	
							this.e2.GetQueue().add(this.RQ.get(i));
							this.RQ.remove(i);i--;
							if(e2.GetStatus()==ElevatorStatus.IDLE){
								this.e2.SetStatus(ElevatorStatus.RUNNING);
								this.e2.GetQueue().notifyAll();
							}
						}break;
					case 3:
						synchronized(this.e3.GetQueue()){	
							this.e3.GetQueue().add(this.RQ.get(i));
							this.RQ.remove(i);i--;
							if(e3.GetStatus()==ElevatorStatus.IDLE){
								this.e3.SetStatus(ElevatorStatus.RUNNING);
								this.e3.GetQueue().notifyAll();
							}
						}break;
					}
				}
			}	
		}
	}
	
	public int SelectHitch(boolean a,boolean b,boolean c){
		int min = 100000000;
		int order=0;
		if(a){
			min=this.e1.GetWork();
			order=1;
		}
		if(b&&this.e2.GetWork()<min){
			min=this.e2.GetWork();
			order=2;
		}
		if(c&&this.e3.GetWork()<min){
			order=3;
		}
		return order;
	}
	
	public int SelectIdle(){
		int min = 100000000;
		int order=0;
		if(this.e1.GetStatus()==ElevatorStatus.IDLE){
			min=this.e1.GetWork();
			order=1;
		}
		if(this.e2.GetStatus()==ElevatorStatus.IDLE&&this.e2.GetWork()<min){
			min=this.e2.GetWork();
			order=2;
		}
		if(this.e3.GetStatus()==ElevatorStatus.IDLE&&this.e3.GetWork()<min){
			order=3;
		}
		return order;
	}
	
	public boolean Hitch(Elevator e,Request r){
		if(e.GetStatus()==ElevatorStatus.RUNNING){
			if(r.GetDir().equals("UP")
					&&r.GetDst()>e.GetFloor()&&r.GetDst()<=e.GetQueue().get(0).GetDst()){
				return true;
			}
			else if(r.GetDir().equals("DOWN")
					&&r.GetDst()<e.GetFloor()&&r.GetDst()>=e.GetQueue().get(0).GetDst()){
				return true;
			}
			else{
				return false;
			}	
		}
		else{
			return false;
		}	
	}
}
