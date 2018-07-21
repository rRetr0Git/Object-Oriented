package Code;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
	
	public static int[][] map;
	public static int[][] oldmap;
	public static int[][] light;
	public static File f_req;
	public static File f_win;
	public static File f_move;
	public static long Time=500;
	public static TaxiGUI TG;
	public static MapMatrix m;
	public static TaxiSquad TS;
	public static long interval;
	public static LightChange LC;
	public static ArrayList<String> loadflow;
		/**
		*@MODIFIES: this;
		*@EFFECTS: 
		*		true==>(The variable and Threads have been constructed);
		*/
	public static void main(String[] args) {
		/**
		 *@OVERVIEW:the Main class have some static variable for interplay between classes
		 *			it can create every necessary object or thread to promise the program's proceeding
		 *			when initializing finishes, threads will start to receive orders;
		 */
		try{
			f_req=new File("D:\\ReqInfo.txt");
			f_win=new File("D:\\WinInfo.txt");
			f_move=new File("D:\\MoveInfo.txt");
			f_req.delete();f_req.createNewFile();
			f_win.delete();f_win.createNewFile();
			f_move.delete();f_move.createNewFile();
			TG = new TaxiGUI();
			m = new MapMatrix();
			light=new int[81][81];
			loadflow=new ArrayList<String>();
			guigv.lightmap=new int[81][81];
			for(int i=0;i<80;i++){
				for(int j=0;j<80;j++){
					light[i][j]=0;
					guigv.lightmap[i][j]=0;
					Main.TG.SetLightStatus(new Point(i,j),0);
				}
			}
			RequestList rlist = new RequestList();
			MapShot MS = new MapShot();
			TS = new TaxiSquad(TG,MS);
			LC = new LightChange();
			InputHandler IH = new InputHandler(rlist,TG);
			Scheduler S = new Scheduler(rlist,TS,MS);
			LoadFile L = new LoadFile(rlist,TS);
			L.start();
			L.join();
			TG.LoadMap(m.GetMap(),80);
			map=TG.getMap();
			oldmap=new int[6405][6405];
			for(int i=0;i<6400;i++){
				for(int j=0;j<6400;j++){
					oldmap[i][j]=map[i][j];
				}
			}
			//TestThread TT = new TestThread();
			LC.start();
			IH.start();
			TS.start();
			S.start();	
			//TT.start();
		}catch(Throwable e){
			System.out.println("Error");
			System.exit(0);
		}
	}
}