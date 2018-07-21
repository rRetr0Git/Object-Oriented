package Code;

public class TestThread extends Thread{
	/**
	*@MODIFIES: System.out;
	*@EFFECTS: 
	*			true==>(Use Test Function);
	*/	
	public void run(){
		while(true){
			try {
				synchronized(this){
					this.wait(100);
				}
			} catch (Throwable e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
	}	
}
