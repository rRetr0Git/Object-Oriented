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
				Thread.sleep(2000);
				Main.TS.GetInfo(20);
				Main.TS.GetTaxiOf(TaxiStatus.WAITING);
			} catch (Throwable e) {
				System.exit(0);
			}
		}
	}
	
}
