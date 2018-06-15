package Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class ElevatorSystem {
	public static void main(String[] args) {
		try{
			File file = new File("result.txt");
			if(!file.exists()){
				file.createNewFile();
			}
			PrintStream ps = new PrintStream(file);
			RequestQueue RQ = new RequestQueue();
			InputHandler IH = new InputHandler(RQ,ps);
			Elevator E1 = new Elevator(1,RQ,ps);
			Elevator E2 = new Elevator(2,RQ,ps);
			Elevator E3 = new Elevator(3,RQ,ps);
			Thread S = new Thread(new Scheduler(RQ,E1,E2,E3,IH,ps));
			IH.start();
			E1.start();
			E2.start();
			E3.start();
			S.start();	
		}catch(Throwable t){
			System.exit(0);
		}
	}
}
