package Code;

import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		try{
			IFTTTLIST list = new IFTTTLIST();
			InputHandler IH = new InputHandler(list);
			OutputSummary OS = new OutputSummary();
			OutputDetail OD = new OutputDetail();
			IH.start();
			try {
				IH.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0;i<list.size();i++){
				ScanAndCompare s = new ScanAndCompare(list.get(i).getPath(),list.get(i).getTriger(),list.get(i).getTask(),OS,OD);
				s.start();
			}
			OS.start();
			OD.start();
			TestThread TT = new TestThread();
			TT.start();	
		}catch(Throwable t){
			System.out.println("Error in Main!");
		}
	}
	// IF D:\Monitor\test.txt path-changed THEN dosth
	// IF D:\Monitor renamed THEN dosth
	// IF D:\Monitor\333 size-changed THEN dosth
	// IF D:\Monitor\222\051.txt modified THEN dosth
}
