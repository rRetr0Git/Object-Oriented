package Code;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class OutputDetail extends Thread{
	private SafeFile detail;
	private ArrayList<String> list;
	
	public OutputDetail(){
		this.list = new ArrayList();
		this.detail= new SafeFile("D:\\Detail.txt");
		if(!detail.exists()){
			detail.createNewFile();
		}
		else{
			detail.delete();
			detail.createNewFile();
		}
	}
	
	public synchronized void add(String str){
		this.list.add(str);
	}
	
	public synchronized void write(){
		try{
			OutputStream output = new FileOutputStream(detail.getFile());
			for(int i=0;i<this.list.size();i++){
				String msg=this.list.get(i);
				byte data[] = msg.getBytes();
				output.write(data);
			}
			output.close();	
		}catch(Throwable t){
			System.out.println("Error in OutputDetail");
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
					System.out.println("Error in run detail");
				}	
			}	
		}catch(Throwable t){
			System.out.println("Error in OD");
		}
	}
}
