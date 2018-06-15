package Code;


public class elevator {
	 
	private String str;
	private double Time;
	private int currentfloor;
	private String direction;
	private String[] element;
	
	public elevator(String str){
		this.str=str;
	}
	
	public elevator(){
		this.Time=0;
		this.currentfloor=1;
	}
	
	public boolean check(){
		this.element=this.str.split(",");	
		int a = Integer.parseInt(element[1]);
		double max = Double.parseDouble("4294967295");
		if(a>=1&&a<=10){
			element[2]=element[2].replaceAll("\\)","");
			try{
				double b = Double.parseDouble(element[2]);
				if(b>max){
					return false;
				}
			}catch(Exception e){
				return false;
			}
			return true;
		}
		else{
			return false;
		}
	}
	public String get_direction(){
		return this.direction;
	}
	public request analysis(){
		request p = new request(str);
		p.set_type("ER");
		p.set_floor(Integer.parseInt(element[1]));
		p.set_time(Double.parseDouble(element[2]));
		return p;
	}
	
	public void set(String str){
		this.str=str;
	}
	
	public double get_Time(){
		return this.Time;
	}
	
	public int get_floor(){
		return this.currentfloor;
	}
	
	@Override
	public String toString(){
		return ("("+this.currentfloor+","+this.direction+","+this.Time+")");
	}

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
	
	public void run(request r1,request r2,int flag){
		
		if(this.currentfloor>r1.get_floor()){		
			this.direction="DOWN";
		}
		else if(this.currentfloor<r1.get_floor()){
			this.direction="UP";
		}
		else{
			this.direction="STILL";
		}
		if(this.Time>=r2.get_time()){
			this.Time+=Math.abs(this.currentfloor-r1.get_floor())*0.5;
		}
		else{
			this.Time=Math.abs(this.currentfloor-r1.get_floor())*0.5+r2.get_time();
		}
		
		this.currentfloor=r1.get_floor();
		
		if(!this.direction.equals("STILL")){
			this.Time++;
		}
	}
}
