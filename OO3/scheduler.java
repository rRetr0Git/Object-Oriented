package Code;

public class scheduler {
	
	public floor[] f_list;
	public elevator e;
	public request_list r_list;
	public int order;
	
	public scheduler(request_list r){
		this.f_list=new floor[11];
		for(int i=0;i<11;i++){
			f_list[i]=new floor();
		}
		this.e = new elevator();
		this.r_list = r;
		this.order=0;
	}
	 
	public request schedule(){
		return this.r_list.get(order++);
	}
	
	public int get_order(){
		return this.order;
	}
	
	public boolean confirm(request r){
		if(r.get_type()=="FR"){
			if(r.get_direction().equals("UP")){
				return f_list[r.get_floor()].get_UP_time()<r.get_time();
			}
			else{
				return f_list[r.get_floor()].get_DOWN_time()<r.get_time();
			}
		}
		else{
			return e.get_Light_Time(r.get_floor())<r.get_time();
		}
	}
	
	public void command(request r){
		if(confirm(r)){
			e.run(r);
			if(r.get_type().equals("FR")){
				if(r.get_direction().equals("UP")){
					f_list[r.get_floor()].set_UP_time(e.get_Time());
				}
				else{
					f_list[r.get_floor()].set_DOWN_time(e.get_Time());
				}
			}
		}
		else{
			System.out.println("#repeat request:"+r.get_str());
		}
	}
	
}
