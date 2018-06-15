package Code;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class request {
	private String str;
	private double time;
	private String type;
	private String direction;
	private int floor;
	private final String regEx1 = "^\\(FR,\\+?[0-9]{1,5},((UP)|(DOWN)),\\+?[0-9]{1,10}\\)$";
	private final String regEx2 = "^\\(ER,\\+?[0-9]{1,5},\\+?[0-9]{1,10}\\)$";
	private int order;
	 
	public request(String str){
		this.str=str.replaceAll(" ", "");	
		this.time=99999999;
		this.floor=11;
		this.type="NULL";
		this.direction="NULL";
		this.order=-1;
	}
	public void set_order(int n){
		this.order=n;
	}
	public int get_order(){
		return this.order;
	}
	public void set_floor(int floor){
		this.floor=floor;
	}
	
	public void set_type(String str){
		this.type=str;
	}
	
	public void set_direction(String str){
		this.direction=str;
	}
	
	public void set_str(String str){
		this.str=str.replaceAll(" ", "");	
	}
	
	public void set_time(double time){
		this.time=time;
	}
	public boolean check(){
		Pattern p1 = Pattern.compile(regEx1);
		Matcher m1 = p1.matcher(this.str);
		Pattern p2 = Pattern.compile(regEx2);
		Matcher m2 = p2.matcher(this.str);
		return (m1.matches()||m2.matches());
	}
	
	public String get_str(){
		return this.str;
	}
	
	public double get_time(){
		return this.time;
	}
	
	public int get_floor(){
		return floor;
	}
	
	public String get_type(){
		return type;
	}
	
	public String get_direction(){
		return direction;
	}
	
	public void remove_repeat(elevator e,request_list r,int flag){
		double finishtime=0;
		if(flag==1){
			finishtime=e.get_Time();
		}
		else{
			finishtime=e.get_Time()+Math.abs(r.get(0).get_floor()-e.get_floor())*0.5+1;
		}
		for(int i=1;(i<r.get_size()&&r.get(i).get_time()<=finishtime);i++){
			if(this.get_type()==r.get(i).get_type()&&this.get_direction().equals(r.get(i).get_direction())&&this.get_floor()==r.get(i).get_floor()){
				System.out.println("#SAME["+r.get(i).get_str().substring(1, r.get(i).get_str().length()-1)+"]");
				r.remove(i);
				i--;
			}
		}
	}
	
	public static void main(String[] args) {
		try{
			String str="";
			request_list list = new request_list();
			Scanner s = new Scanner(System.in);	
			str=s.nextLine();
			request req = new request(str);					
			elevator e = new elevator(req.get_str());
			floor f = new floor(req.get_str());
			while(!req.get_str().equals("RUN")){
				if(req.check()){
					if(req.get_str().charAt(1)=='E'){
						e.set(req.get_str());
						if(e.check()){
							list.add(e.analysis());
						}
					}
					else{
						f.set(req.get_str());
						if(f.check()){
							list.add(f.analysis());
						}
					}
				}
				else{
					System.out.println("INVALID["+req.get_str()+"]");
				}
				req.set_str(s.nextLine());
			}
			for(int i=0;i<list.get_size();i++){
				list.get(i).set_order(i+1);
			}
			scheduler_re schedule = new scheduler_re(list);
			while(list.get_size()>0){
				schedule.command(schedule.schedule());
			}	
		}catch(Exception e){
			
		}
		
	}
}
