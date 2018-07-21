package Code;

import java.awt.Point;
import java.util.Random;

public class LightChange extends Thread{
	/**
	 *@OVERVIEW:LightChange mainly need to refresh the array which stores the information of every light
	 *			when it's time to change light, this class update the array 
	 *			so that other class can get information from the latest light map;
	 *			
	 */
	public static long interval; 
	/**
	*@MODIFIES:this.interval,this.light;
	*@EFFECTS: 
	*		true==>(A new Thread to change lights has been constructed);
	*/	
	public LightChange(){
		Random rand = new Random();
		//Main.interval=((rand.nextInt(501)+500)/100+1)*100;
		Main.interval=rand.nextInt(501)+500;
		this.interval=Main.interval;
		System.out.println(Main.interval);
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				Main.light[i][j]=0;
			}
		}
	}
	/**
	*@MODIFIES:system.out
	*@EFFECTS:
	*		true==>(sleep for a certain interval and change every light's status);
	*/
	public void run(){
		while(true){
			try{
				synchronized(this){
					this.wait();
				}
				Change();
			}catch(Throwable e){
				System.out.println("Something wrong with LightChange");
				System.exit(0);
			}	
		}	
	}
	/**
	*@MODIFIES: this.light,this.lightmap;
	*@EFFECTS: 
	*		(\all int i,j,k;0<=i,j<80,light[i][j]>0)==>
	*		((light[i][j]==1)==>(light[i][j]==2&&lightmap[i][j]==1)&&(light[i][j]==2)==>(light[i][j]==1&&lightmap[i][j]==2));
	*/
	public void Change(){
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				if(Main.light[i][j]==1){
					Main.light[i][j]=2;
					guigv.lightmap[i][j]=1;
					Main.TG.SetLightStatus(new Point(i,j),1);
				}
				else if(Main.light[i][j]==2){
					Main.light[i][j]=1;
					guigv.lightmap[i][j]=2;
					Main.TG.SetLightStatus(new Point(i,j),2);
				}
			}
		}
	}
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		if(this.interval<0)return false;
		if(Main.light==null)return false;
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				if(Main.light[i][j]>2||Main.light[i][j]<0)return false;
			}
		}
		return true;
	}
}
