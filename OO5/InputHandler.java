package Code;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler extends Thread{
	private final String regEx1="^\\(FR,[0-9]{1,2},((UP)|(DOWN))\\)$";
	private final String regEx2="^\\(ER,#[0-9],[0-9]{1,2}\\)$";
	private RequestQueue rq;
	private PrintStream ps;
	
	public InputHandler(RequestQueue rq,PrintStream ps){
		this.rq=rq;
		this.ps=ps;
		System.setOut(this.ps);
	}
	
	public void run(){
		Scanner s = new Scanner(System.in);
		this.scan(s,this.ps);
	}
	
	public boolean validate(String str){
		Pattern p1 = Pattern.compile(this.regEx1);
		Matcher m1 = p1.matcher(str);
		Pattern p2 = Pattern.compile(this.regEx2);
		Matcher m2 = p2.matcher(str);
		return (m1.matches()||m2.matches());
	}	
	
	public boolean parse(String str){
		String[] part = str.split(",");
		if(part[0].charAt(1)=='F'){
			int floor = Integer.parseInt(part[1]);
			if(floor>0&&floor<21){
				if((floor==1&&part[2].replace(")", "").equals("DOWN"))
						||(floor==20&&part[2].replace(")", "").equals("UP"))){
					return false;
				}
				else{
					return true;
				}
			}
			else{
				return false;
			}
		}
		else{
			int elevator = Integer.parseInt(part[1].replace("#", ""));
			int floor = Integer.parseInt(part[2].replace(")", ""));
			if(floor>0&&floor<21&&elevator<4&&elevator>0){
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	public void scan(Scanner s,PrintStream ps){
		int num=0;
		int lines=0;
		String line = s.nextLine();
		lines++;
		long BeginTime=System.currentTimeMillis();
		long time=BeginTime;
		if(line.equals("END")){
			Request req = new Request("END");
			synchronized(rq){
				rq.add(req);
				rq.notifyAll();
			}
		}
		while(!line.equals("END")&&lines<50){
			num=0;
			line=line.replaceAll(" ", "");
			String[] element=line.split(";",-1);
			for(int i=0;i<element.length;i++){
				if(validate(element[i])&&parse(element[i])&&num<10){
					Request req = new Request(element[i],(double)(time-BeginTime)/1000,BeginTime);
					synchronized(rq){
						rq.add(req);
						rq.notifyAll();
					}	
				}
				else{
					synchronized(this.ps){
						System.out.println(System.currentTimeMillis()+":INVALID["+element[i]+","+String.format("%.1f", (double)(time-BeginTime)/1000)+"]");	
					}
				}
				num++;
			}
			line=s.nextLine();
			lines++;
			time=System.currentTimeMillis();
			if(line.equals("END")||lines==50){
				Request req = new Request("END");
				synchronized(rq){
					rq.add(req);
					rq.notifyAll();
				}
			}
		}
	}
}
