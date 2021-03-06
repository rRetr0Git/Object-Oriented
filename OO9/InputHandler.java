package Code;

import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler extends Thread{
	
	private final String RegEx="^\\[CR,\\([0-9]{1,2},[0-9]{1,2}\\),\\([0-9]{1,2},[0-9]{1,2}\\)\\]$";
	private final String RegExOpen="^\\[OPEN,[0-9]{1,4},[0-9]{1,4}\\]$";
	private final String RegExClose="^\\[CLOSE,[0-9]{1,4},[0-9]{1,4}\\]$";
	private RequestList rl;
	private TaxiGUI gui;
	private OutputStream output;
	public static ArrayList<String> list;
		/**
		*@REQUIRES: rl!=null;gui!=null;
		*@MODIFIES: this;
		*@EFFECTS: 
		*		true==>(A new InputHandler has been constructed);
		*/
	public InputHandler(RequestList rl,TaxiGUI gui){
		
		this.rl=rl;
		this.gui=gui;
		try {
			this.output=new FileOutputStream(Main.f_req,true);
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
		list = new ArrayList<String>();
	}
		/**
		*@MODIFIES: System.out;
		*@EFFECTS: 
		*		true==>(scan());
		*/
	public void run(){
		
		try{
			Scanner s = new Scanner(System.in);
			scan(s);	
		}catch(Throwable t){
			System.out.println("Error in Input");
			System.exit(0);
		}
	}
		/**
		*@REQUIRES: s!=null,System.in;
		*@MODIFIES: System.out,this.output,this.rl;
		*@EFFECTS: 
		*		((true)==>(check()==>inRange()==>rl.contains()==>rl.contains(r)));
		*/
	public void scan(Scanner s){
		
		System.out.println("Ready to Input");
		String line = s.nextLine();
		String[] element;
		while(true){
			line=line.replaceAll(" ","");
			if(this.check(line)){
				element=line.split(",");
				//[CR,(A,B),(C,D)]
				int A=Integer.parseInt(element[1].replace("(",""));
				int B=Integer.parseInt(element[2].replace(")",""));
				int C=Integer.parseInt(element[3].replace("(",""));
				int D=Integer.parseInt(element[4].replace(")]",""));
				if(inRange(A,B,C,D)&&!(A==C&&B==D)){
					Request r = new Request(A,B,C,D);
					if(this.rl.contains(r)){
						System.out.println("SAME");
					}
					else{
						this.rl.add(r);
						this.gui.RequestTaxi(r.GetSrc(), r.GetDst());
						try {
							String msg=r.OutputInfo();
							byte data[] = msg.getBytes();
							output.write(data);
						} catch (Throwable e) {
							System.out.println("Error in Output");
							System.exit(0);
						}
						//System.out.println("add "+r.toString());
					}
				}
				else{
					System.out.println("Invalid Request");
				}
			}
			line=s.nextLine();
		}
	}
		/**
		*@REQUIRES: s!=null;
		*@MODIFIES: System.out;
		*@EFFECTS: 
		*		(s.matches(RegEx))==>(\result==true);
		*		(s.matches(RegExOpen))==>(OPEN()&&(\result==false)))
		*		(s.matches(RegExClose))==>(CLOSE()&&(\result==false)))
		*/
	public boolean check(String s){
		
		Pattern p = Pattern.compile(RegEx);
		Matcher m = p.matcher(s);
		if(m.matches()){
			return true;
		}
		else{
			Pattern p1 = Pattern.compile(RegExOpen);
			Matcher m1 = p1.matcher(s);
			Pattern p2 = Pattern.compile(RegExClose);
			Matcher m2 = p2.matcher(s);
			String[] element=s.split(",");
			if(m1.matches()){
				int A = Integer.parseInt(element[1]);
				int B = Integer.parseInt(element[2].replaceAll("]", ""));
				if(inRange(A,B)&&Open(A,B)){
					System.out.println("The road will be opened in next round");
				}
				else{
					System.out.println("Open Failed");
				}
				return false;
			}
			else if(m2.matches()){
				int A = Integer.parseInt(element[1]);
				int B = Integer.parseInt(element[2].replaceAll("]", ""));
				if(inRange(A,B)&&Close(A,B)){
					System.out.println("The road will be closed in next round");
				}
				else{
					System.out.println("Close Failed");
				}
				return false;
			}
			else{
				System.out.println("Wrong Format");
				return false;	
			}	
		}
	}
		/**
		*@REQUIRES:\all int a,b,c,d;
		*@EFFECTS:
		*		(\exist int k;true==>(k=a,k=b,k=c,k=d);k<0||k>79)==>(\result==false);
		*		(\all int k;true==>(k=a,k=b,k=c,k=d);0<=k<=79)==>(\result==true);
		*/
	public boolean inRange(int a,int b,int c,int d){
		
		return (a>=0&&a<=79)&&(b>=0&&b<=79)&&(c>=0&&c<=79)&&(d>=0&&d<=79);
	}
		/**
		*@REQUIRES: \all int a,b;
		*@EFFECTS: 
		*		(\exist int k;true==>(k=a,k=b);k<0||k>6399)==>(\result==false);
		*		(\all int k;true==>(k=a,k=b);0<=k<=6399)==>(\result==true);
		*/	
	public boolean inRange(int a,int b){

		return (a>=0&&a<=6399)&&(b>=0&&b<=6399)&&((a-b==1)||(a-b==-1)||(a-b==80)||(a-b==-80));
	}
		/**
		*@REQUIRES: \all int a,b;
		*@MODIFIES: this.list;
		*@EFFECTS: 
		*		(map[a][b]==1||oldmap[a][b]==1000000)==>(\result==false);
		*		(map[a][b]!=1&&oldmap[a][b]!=1000000)==>(String s=a+" "+b+" "+1)&&(list.contains())&&\result==true;
		*/
	public boolean Open(int a,int b){

		if(Main.map[a][b]==1||Main.oldmap[a][b]==1000000){
			return false;
		}
		else{
			synchronized(list){
				list.add(a+" "+b+" "+1);
			}
			return true;
		}
	}
		/**
		*@REQUIRES: \all int a,b;
		*@MODIFIES: this.list;
		*@EFFECTS: 
		*		(map[a][b]==1000000)==>(\result==false);
		*		(map[a][b]!=1000000)==>(String s=a+" "+b+" "+0)&&(list.contains())&&\result==true;
		*/	
	public boolean Close(int a,int b){

		if(Main.map[a][b]==1000000){
			return false;
		}
		else{
			synchronized(list){
				list.add(a+" "+b+" "+0);
			}
			return true;
		}
	}
}
