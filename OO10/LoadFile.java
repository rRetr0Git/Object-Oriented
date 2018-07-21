package Code;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class LoadFile extends Thread{
	/**
	 *@OVERVIEW: LoadFile is a kind of class which can read information from file
	 *			during its reading, it can verify the validity and update the map,light map,taxis and etc.
	 *			when there is something wrong with the file, it can feedback and exit program or ignore the error;
	 */
	private File file;
	private Scanner s;
	private RequestList rl;
	private TaxiSquad TS;
		/**
		*@REQUIRES:rl!=null,TS!=null;
		*@MODIFIES:this;
		*@EFFECTS:
		*		true==>(A new LoadFile has been constructed);
		*/
	public LoadFile(RequestList rl,TaxiSquad TS){

		s= new Scanner(System.in);
		this.rl=rl;
		this.TS=TS;
	}
		/**
		*@MODIFIES: System.out;
		*@EFFECTS: 
		*		(System.in=="LOAD"==>load());
		*/	
	public void run(){
		try{
			System.out.println("Please input \"LOAD\" for loadfile, other input to cancel loadfile");
			String line = s.nextLine();
			if(line.equals("LOAD")){
				System.out.println("Please input the absolute address");
				line=s.nextLine();
				this.load(line);
			}
		}catch(Throwable e){
			System.out.println("Error in LoadFile");
			System.exit(0);
		}
	}
		/**
		*@REQUIRES: s!=null,file!=null;
		*@MODIFIES: this.Matrix,this.flowmap,this.TS,this.rl,System.out,this.light;
		*@EFFECTS: 
		*		(file.exists()==>Matrix_update==>Light_update==>flowmap_update==>TS_update==>rl_update);	
		*/	
	public void load(String s){
		file=new File(s);
		if(file.exists()){
			BufferedReader reader=null; 
			String temp = null;
			try{
				reader=new BufferedReader(new FileReader(file)); 
				temp=reader.readLine();
				while(temp!=null){
					temp=reader.readLine();
					temp=reader.readLine();
					temp=reader.readLine();
					int count=0;
					while(!temp.equals("#end_map")){
						temp=temp.replaceAll(" ","");
						temp=temp.replaceAll("\t","");
						for(int i=0;i<80;i++){
							Main.m.Matrix[count][i]=temp.charAt(i)-'0';
						}
						temp=reader.readLine();
						count++;
					}
					temp=reader.readLine();
					temp=reader.readLine();
					temp=reader.readLine();
					count=0;
					Random rand = new Random();
					while(!temp.equals("#end_light")){
						temp=temp.replaceAll(" ","");
						temp=temp.replaceAll("\t","");
						for(int i=0;i<80;i++){
							if((temp.charAt(i)-'0')==1){
								int degree=0;
								if(count>0&&(Main.m.Matrix[count-1][i]==2||Main.m.Matrix[count-1][i]==3)){
									degree++;
								}
								if(i>0&&(Main.m.Matrix[count][i-1]==1||Main.m.Matrix[count][i-1]==3)){
									degree++;
								}
								if(Main.m.Matrix[count][i]==1||Main.m.Matrix[count][i]==2){
									degree++;
								}
								else if(Main.m.Matrix[count][i]==3){
									degree+=2;
								}
								if(degree>=3){
									Main.light[count][i]=1+rand.nextInt(2);
									if(Main.light[count][i]==1){
										Main.TG.SetLightStatus(new Point(count,i),2);
									}
									else{
										Main.TG.SetLightStatus(new Point(count,i),1);
									}	
								}
								else{
									System.out.println("Light at ("+count+","+i+") won't be added!");
									Main.light[count][i]=0;
									Main.TG.SetLightStatus(new Point(count,i),0);
								}		
							}
							else{
								Main.light[count][i]=0;
								Main.TG.SetLightStatus(new Point(count,i),0);
							}	
						}
						temp=reader.readLine();
						count++;
					}
					temp=reader.readLine();
					temp=reader.readLine();
					temp=reader.readLine();
					while(!temp.equals("#end_flow")){
						String[] element1=temp.split(",");
						int a=Integer.parseInt(element1[0].replace("(",""));
						int b=Integer.parseInt(element1[1].replace(")",""));
						int c=Integer.parseInt(element1[2].replace("(",""));
						int d=Integer.parseInt(element1[3].replace(")",""));
						int e=Integer.parseInt(element1[4]);
						Main.loadflow.add(a+" "+b+" "+c+" "+d+" "+e);
						while(e!=0){
							guigv.AddFlow(a, b, c, d);
							e--;
						}
						temp=reader.readLine();
					}
					temp=reader.readLine();
					temp=reader.readLine();
					temp=reader.readLine();
					while(!temp.equals("#end_taxi")){
						String[] element2=temp.split(",");
						int num=Integer.parseInt(element2[0]);
						String status=element2[1];
						int credit=Integer.parseInt(element2[2]);
						int x=Integer.parseInt(element2[3].replace("(", ""));
						int y=Integer.parseInt(element2[4].replace(")", ""));
						Taxi t = this.TS.get(num);
						t.SetCredit(credit);
						t.SetLoc(x, y);
						t.SetPre(x, y);
						if(status.equals("STOP")){
							t.SetStatus(TaxiStatus.STOP);
						}
						else{
							t.SetStatus(TaxiStatus.WAITING);
						}
						Main.TG.SetTaxiStatus(num, t.GetLocPoint(), t.GetStatus().GetNum());
						temp=reader.readLine();
					}
					temp=reader.readLine();
					temp=reader.readLine();
					temp=reader.readLine();
					while(!temp.equals("#end_request")){
						String[] element3=temp.split(",");
						//[CR,(A,B),(C,D)]
						int A=Integer.parseInt(element3[1].replace("(",""));
						int B=Integer.parseInt(element3[2].replace(")",""));
						int C=Integer.parseInt(element3[3].replace("(",""));
						int D=Integer.parseInt(element3[4].replace(")]",""));
						Request r = new Request(A,B,C,D);
						this.rl.add(r);
						temp=reader.readLine();
					}
					break;
				}
			}catch(Throwable e){
				e.printStackTrace();
				System.out.println("Something wrong with the file");
				System.exit(0);
			}
		}
		else{
			System.out.println("file not exist,load failed");
		}
	}
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		if(this.s==null)return false;
		if(this.rl==null)return false;
		if(this.TS==null)return false;
		if(Main.light==null)return false;
		if(Main.m.Matrix==null)return false;
		if(Main.loadflow==null)return false;
		return true;
	}
}
