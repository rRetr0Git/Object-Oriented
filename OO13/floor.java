package Code;

public class floor {
	
	private String str;
	private String[] element;
	
	public floor(String str){
		this.str=str;
	}
	
	public floor(){
		this.str="";
	}
	
	public boolean check(){
		this.element=this.str.split(",");	
		int a = Integer.parseInt(element[1]);
		double max = Double.parseDouble("4294967295");
		if(a>=1&&a<=10){
			if((a==1&&element[2].equals("DOWN"))||(a==10&&element[2].equals("UP"))){
				return false;
			}
			else{
				element[3]=element[3].replaceAll("\\)","");
				double b = Double.parseDouble(element[3]);
				if(b>max){
					return false;
				}
				return true;
			}
		}
		else{
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
