package Code;

public class floor {
	
	private String str;
	private String[] element;
	private double UP_Time;
	private double DOWN_Time;
	
	public floor(String str){
		this.str=str;
	}
	
	public floor(){
		this.UP_Time=-1;
		this.DOWN_Time=-1;
	}
	
	public double get_UP_time(){
		return UP_Time;
	}
	
	public double get_DOWN_time(){
		return DOWN_Time;
	} 
	
	public void set_UP_time(double time){
		this.UP_Time=time;
	}
	
	public void set_DOWN_time(double time){
		this.DOWN_Time=time;
	}
	
	public boolean check(){
		this.element=this.str.split(",");	
		int a = Integer.parseInt(element[1]);
		double max = Double.parseDouble("4294967295");
		if(a>=1&&a<=10){
			if((a==1&&element[2].equals("DOWN"))||(a==10&&element[2].equals("UP"))){
				System.out.println("ERROR\n#invalid order");
				return false;
			}
			else{
				element[3]=element[3].replaceAll("\\)","");
				try{
					double b = Double.parseDouble(element[3]);
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
		}
		else{
			System.out.println("ERROR\n#invalid floor");
			return false;
		}
	}
	
	public request analysis(){
		request p = new request(str);
		p.set_type("FR");
		p.set_direction(element[2]);
		p.set_floor(Integer.parseInt(element[1]));
		p.set_time(Double.parseDouble(element[3]));
		return p;
	}
	
	public void set(String str){
		this.str=str;
	}
}
