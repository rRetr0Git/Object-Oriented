package Code;


public class elevator {
	 
	private String str;
	private double Time;
	private double[] Light_Time;
	private int currentfloor;
	private String direction;
	private String[] element;
	
	public elevator(String str){
		this.str=str;
	}
	
	public elevator(){
		this.Time=0;
		this.currentfloor=1;
		this.Light_Time=new double[11];
		for(int i=0;i<11;i++){
			Light_Time[i]=-1;
		}
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
					System.out.println("ERROR\n#t overflow");
					return false;
				}
			}catch(Exception e){
				System.out.println("ERROR\n#t overflow");
				return false;
			}
			return true;
		}
		else{
			System.out.println("ERROR\n#invalid floor");
			return false;
		}
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
	
	public double get_Light_Time(int index){
		return this.Light_Time[index];
	}
	
	public int get_floor(){
		return this.currentfloor;
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
		
		if(r.get_type().equals("ER")){
			this.Light_Time[r.get_floor()]=this.Time;
			if(!this.direction.equals("STILL")){
				this.Light_Time[r.get_floor()]++;
			}
		}
		System.out.println("("+this.currentfloor+","+this.direction+","+String.format("%.1f)", this.Time));
		if(!this.direction.equals("STILL")){
			this.Time++;
		}
	}
}
