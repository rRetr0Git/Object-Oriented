package Code;

import java.io.File;
import java.io.IOException;

public class Main {
	
	public static int[][] map;
	public static int[][] distance;
	public static File f_req;
	public static File f_win;
	public static File f_move;
	public static long Time;
	
	public static void main(String[] args) {
		try{
			f_req=new File("D:\\ReqInfo.txt");
			f_win=new File("D:\\WinInfo.txt");
			f_move=new File("D:\\MoveInfo.txt");
			f_req.delete();f_req.createNewFile();
			f_win.delete();f_win.createNewFile();
			f_move.delete();f_move.createNewFile();
			TaxiGUI TG = new TaxiGUI();
			MapMatrix m = new MapMatrix();
			TG.LoadMap(m.GetMap(),80);
			map=TG.getMap();
			distance=TG.getDistance();
			RequestList rlist = new RequestList();
			MapShot MS = new MapShot();
			TaxiSquad TS = new TaxiSquad(TG,MS);
			InputHandler IH = new InputHandler(rlist,TG);
			Scheduler S = new Scheduler(rlist,TS,MS);
			IH.start();
			TS.start();
			S.start();	
		}catch(Throwable e){
			System.out.println("Error");
			System.exit(0);
		}
	}
}