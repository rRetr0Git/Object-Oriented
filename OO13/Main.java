package Code;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		try{
			int num=0;
			String str="";
			request_list list = new request_list();
			Scanner s = new Scanner(System.in);	
			str=s.nextLine();
			request req = new request(str);					
			elevator e = new elevator(req.get_str());
			floor f = new floor(req.get_str());
			while(!req.get_str().equals("RUN")){
				num++;
				if(num<=100){
					if(req.check()){
						if(req.get_str().charAt(1)=='E'){
							e.set(req.get_str());
							if(e.check()){
								list.add(e.analysis());
							}
							else{
								System.out.println("INVALID["+req.get_str()+"]");
							}
						}
						else{
							f.set(req.get_str());
							if(f.check()){
								list.add(f.analysis());
							}
							else{
								System.out.println("INVALID["+req.get_str()+"]");
							}
						}
					}
					else{
						System.out.println("INVALID["+req.get_str()+"]");
					}	
				}
				req.set_str(s.nextLine());
			}
			for(int i=0;i<list.get_size();i++){
				list.get(i).set_order(i+1);
			}
			scheduler schedule = new scheduler(list);
			while(list.get_size()>0){
				schedule.command(schedule.schedule());
			}	
		}catch(Exception e){
			System.exit(0);
		}
	}
}
