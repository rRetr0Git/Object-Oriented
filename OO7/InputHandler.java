package Code;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler extends Thread{
	
	private final String RegEx="^\\[CR,\\([0-9]{1,2},[0-9]{1,2}\\),\\([0-9]{1,2},[0-9]{1,2}\\)\\]$";
	private RequestList rl;
	private TaxiGUI gui;
	private OutputStream output;
	
	public InputHandler(RequestList rl,TaxiGUI gui){
		this.rl=rl;
		this.gui=gui;
		try {
			this.output=new FileOutputStream(Main.f_req,true);
		} catch (FileNotFoundException e) {
			System.exit(0);
		}
	}
	
	public void run(){
		try{
			Scanner s = new Scanner(System.in);
			scan(s);	
		}catch(Throwable t){
			System.out.println("Error in Input");
			System.exit(0);
		}
	}
	
	public void scan(Scanner s){
		System.out.println("Ready to Input");
		String line = s.nextLine();
		String[] element;
		while(true){
			if(this.check(line)){
				line=line.replaceAll(" ","");
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
	
	public boolean check(String s){
		Pattern p = Pattern.compile(RegEx);
		Matcher m = p.matcher(s);
		if(m.matches()){
			return true;
		}
		else{
			System.out.println("Wrong Format");
			return false;
		}
	}
	
	public boolean inRange(int a,int b,int c,int d){
		return (a>=0&&a<=79)&&(b>=0&&b<=79)&&(c>=0&&c<=79)&&(d>=0&&d<=79);
	}
}
