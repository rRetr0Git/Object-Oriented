package Code;


public class elevator implements elevator_interface{
	
	/**
	 *@OVERVIEW: elevator is a class that has some basic get&set function, we use it to simulate the movement of a elevator.
	 */
	private String str;
	public double Time;
	private int currentfloor;
	private String direction;
	private String[] element;
	/**
	*@REQUIRES:str!=null;
	*@MODIFIES:this;this.str;this.Time;this.direction;this.currentfloor;
	*@EFFECTS: 
	*		true==>(A new elevator has been constructed, do some initialization);
	*/
	public elevator(String str){
		this.repOK();
		this.str=str;
		this.repOK();
		this.Time=-5;
		this.repOK();
		this.Time=0;
		this.repOK();
		this.direction="UP";
		this.repOK();
		this.currentfloor=15;
		this.repOK();
		this.currentfloor=1;
	}
	/**
	*@MODIFIES:this;this.str;this.Time;this.direction;this.currentfloor;
	*@EFFECTS: 
	*		true==>(A new elevator has been constructed, do some initialization);
	*/
	public elevator(){
		this.repOK();
		this.str="";
		this.repOK();
		this.Time=-5;
		this.repOK();
		this.Time=0;
		this.repOK();
		this.direction="UP";
		this.repOK();
		this.currentfloor=15;
		this.repOK();
		this.currentfloor=1;
	}
	/**
	*@MODIFIES:this;this.element;
	*@EFFECTS: 
	*		true==>(check the range of the time and set the value of element);
	*		(floor out of range or time out of range)==>(\result==false);
	*		\result==true;
	*/
	public boolean check(){
		this.element=this.str.split(",");	
		int a = Integer.parseInt(element[1]);
		double max = Double.parseDouble("4294967295");
		if(a>=1&&a<=10){
			element[2]=element[2].replaceAll("\\)","");
			double b = Double.parseDouble(element[2]);
			if(b>max){
				return false;
			}
			return true;
		}
		else{
			return false;
		}
	}
	/**
	*@EFFECTS: 
	*		\result==this.direction;
	*/
	public String get_direction(){
		return this.direction;
	}
	/**
	*@REQUIRES:element[1]!=null;element[2]!=null;this.check()==true;
	*@MODIFIES:this.p;
	*@EFFECTS: 
	*		true==>(create a request, set its basic value and return it);
	*/
	public request analysis(){
		request p = new request(str);
		p.set_type("ER");
		p.set_floor(Integer.parseInt(element[1]));
		p.set_time(Double.parseDouble(element[2]));
		return p;
	}
	/**
	*@REQUIRES:str!=null;
	*@MODIFIES:this.str;
	*@EFFECTS: 
	*		this.str==str;
	*/
	public void set(String str){
		this.str=str;
	}
	/**
	*@EFFECTS: 
	*		\result==this.floor;
	*/
	public int get_floor(){
		return this.currentfloor;
	}	
	/**
	*@EFFECTS: 
	*		\result==(a string that represent the status of the elevator);
	*/
	@Override
	public String toString(){
		return ("("+this.currentfloor+","+this.direction+","+String.format("%.1f)", this.Time-1));
	}
	/**
	*@REQUIRES:r!=null;rl!=null;rl.size()>=1;index!=null;index>=0;index<rl.size();
	*@EFFECTS: 
	*		true==>(return the index of the best shortcut);
	*/
	public int FindBest(request r,request_list rl,int index){
		String Direction="";
		double finishtime;
		double arrivetime;
		int best=999;
		int min = 20;
		
		if(rl.get_size()==1){
			return best;
		}
		if(r.get_floor()>this.currentfloor){
			Direction="UP";
		}
		else if(r.get_floor()<this.currentfloor){
			Direction="DOWN";
		}
		
		if(this.Time>r.get_time()){
			finishtime=this.Time+Math.abs(this.currentfloor-r.get_floor())*0.5;
		}
		else{
			finishtime=Math.abs(this.currentfloor-r.get_floor())*0.5+r.get_time();
		}	
		if(rl.get_size()<index+1){
			return best;
		}
		for(int i=index;rl.get(i).get_time()<finishtime;i++){
			if(this.Time>r.get_time()){
				arrivetime=this.Time+Math.abs(this.currentfloor-rl.get(i).get_floor())*0.5;
			}
			else{
				arrivetime=Math.abs(this.currentfloor-rl.get(i).get_floor())*0.5+r.get_time();
			}	
			if(rl.get(i).get_type().equals("FR")){
				if(rl.get(i).get_direction().equals(Direction)&&arrivetime>rl.get(i).get_time()){
					if(Direction.equals("UP")&&this.currentfloor<rl.get(i).get_floor()&&r.get_floor()>=rl.get(i).get_floor()){
						if(Math.abs(this.currentfloor-rl.get(i).get_floor())<min){
							best=i;
							min=Math.abs(this.currentfloor-rl.get(i).get_floor());
						}
					}
					else if(Direction.equals("DOWN")&&this.currentfloor>rl.get(i).get_floor()&&r.get_floor()<=rl.get(i).get_floor()){
						if(Math.abs(this.currentfloor-rl.get(i).get_floor())<min){
							best=i;
							min=Math.abs(this.currentfloor-rl.get(i).get_floor());
						}
					}
				}
			}
			else{
				if(Direction.equals("UP")&&rl.get(i).get_floor()>this.currentfloor&&arrivetime>rl.get(i).get_time()){
					if(this.currentfloor<rl.get(i).get_floor()&&r.get_floor()>=rl.get(i).get_floor()){
						if(Math.abs(this.currentfloor-rl.get(i).get_floor())<min){
							best=i;
							min=Math.abs(this.currentfloor-rl.get(i).get_floor());
						}
					}
				}
				else if(Direction.equals("DOWN")&&rl.get(i).get_floor()<this.currentfloor&&arrivetime>rl.get(i).get_time()){
					if(this.currentfloor>rl.get(i).get_floor()&&r.get_floor()<=rl.get(i).get_floor()){
						if(Math.abs(this.currentfloor-rl.get(i).get_floor())<min){
							best=i;
							min=Math.abs(this.currentfloor-rl.get(i).get_floor());
						}
					}
				}
			}
			if(i+1==rl.get_size()){
				break;
			}
		}
		return best;
	}
	/**
	*@REQUIRES:r!=null;rl!=null;rl.size()>=1;s!=null;
	*@EFFECTS: 
	*		true==>(return the index of the best shortcut);
	*/
	public int FindBest(request r,request_list rl,String s){
		String Direction="";
		double finishtime;
		double arrivetime;
		int best=999;
		
		if(rl.get_size()==1){
			return best;
		}
		
		Direction=this.direction;
		finishtime=this.Time-1;
		if(rl.get_size()<2){
			return best;
		}
		for(int i=1;rl.get(i).get_time()<finishtime;i++){
			if(rl.get(i).get_type().equals("ER")){
				if(Direction.equals("UP")&&rl.get(i).get_floor()>this.currentfloor){
					best=i;
					break;
				}
				else if(Direction.equals("DOWN")&&rl.get(i).get_floor()<this.currentfloor){
					best=i;
					break;
				}
			}
			if(i+1==rl.get_size()){
				break;
			}
		}
		return best;
	}
	/**
	*@REQUIRES:r!=null;
	*@MODIFIES:this.direction;this.Time;this.currentfloor;
	*@EFFECTS: 
	*		true==>(command a request and change the status of the elevator,such as time, current floor and direction);
	*/
	public void run(request r){
		
		if(this.currentfloor>r.get_floor()){		
			this.direction="DOWN";
		}
		else if(this.currentfloor<r.get_floor()){
			this.direction="UP";
		}
		else{
			this.direction="STILL";
		}
		if(this.Time>r.get_time()){
			this.Time+=Math.abs(this.currentfloor-r.get_floor())*0.5;
			if(this.direction.equals("STILL")){
				this.Time++;
			}
		}
		else{
			this.Time=Math.abs(this.currentfloor-r.get_floor())*0.5+r.get_time();
			if(this.direction.equals("STILL")){
				this.Time++;
			}
		}	
		this.currentfloor=r.get_floor();
		
		System.out.println("["+r.get_str().substring(1, r.get_str().length()-1)+"]/("+this.currentfloor+","+this.direction+","+String.format("%.1f)", this.Time));
		if(!this.direction.equals("STILL")){
			this.Time++;
		}
	}
	/**
	*@REQUIRES:r!=null;
	*@MODIFIES:this.direction;this.Time;this.currentfloor;
	*@EFFECTS: 
	*		true==>(command main request and a shortcut, change the status of the elevator,such as time, current floor and direction);
	*/
	public void run(request r1,request r2,int flag){
		
		if(this.currentfloor>r1.get_floor()){		
			this.direction="DOWN";
		}
		else{
			this.direction="UP";
		}
		if(this.Time>=r2.get_time()){
			this.Time+=Math.abs(this.currentfloor-r1.get_floor())*0.5;
		}
		else{
			this.Time=Math.abs(this.currentfloor-r1.get_floor())*0.5+r2.get_time();
		}
		
		this.currentfloor=r1.get_floor();
		this.Time++;
	}
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		if(str==null)return false;
		if(Time<0)return false;
		if(currentfloor<=0||currentfloor>10)return false;
		return true;
	}
}
