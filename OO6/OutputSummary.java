package Code;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class OutputSummary extends Thread{
	private int[] TrigerNum;
	private SafeFile summary; 
	
	public OutputSummary(){
		this.TrigerNum = new int[4];
		this.summary= new SafeFile("D:\\Summary.txt");
		if(!summary.exists()){
			summary.createNewFile();
		}
		else{
			summary.delete();
			summary.createNewFile();
		}
		for(int i=0;i<4;i++){
			this.TrigerNum[i]=0;
		}
	}
	public synchronized void add(int index){
		this.TrigerNum[index]++;
	}
	
	public synchronized void write(){
		try{
			OutputStream output = new FileOutputStream(summary.getFile());
			for(int i=0;i<4;i++){
				String msg="";
				switch(i){
				case 0:msg="renamed:"+TrigerNum[0]+System.getProperty("line.separator");break;
				case 1:msg="modified:"+TrigerNum[1]+System.getProperty("line.separator");break;
				case 2:msg="path-changed:"+TrigerNum[2]+System.getProperty("line.separator");break;
				case 3:msg="size-changed:"+TrigerNum[3]+System.getProperty("line.separator");break;
				}
				byte data[] = msg.getBytes();
				output.write(data);
			}
			output.close();	
		}catch(Throwable t){
			System.out.println("Error in OutputSummary");
		}
	}
	public void run(){
		try{
			while(true){
				this.write();
				try {
					Thread.sleep(5000);
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println("Error in run summary");
				}	
			}	
		}catch(Throwable t){
			System.out.println("Error in OS");
		}
	}
}
