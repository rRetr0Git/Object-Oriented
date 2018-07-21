package Code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapMatrix {
	
	private File file;
	int[][] Matrix;
	private final String regEx = "^[0,1,2,3]{80}$";
		/**
		*@MODIFIES: this.Matrix,this.file;
		*@EFFECTS: 
		*		true==>(A new MapMatrix has been constructed)==>(Analysis(file));
		*/	
	public MapMatrix(){

		Matrix = new int[80][80];
		this.file=new File("D:\\Map.txt");	
		this.Analysis(file);
	}
		/**
		*@REQUIRES: f!=null;
		*@MODIFIES: this.Matrix,System.out;
		*@EFFECTS: 
		*		((String s=readLine())!=null==>Check()==>Matrix.update());
		*/	
	public void Analysis(File f){
		try {
			InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
			BufferedReader br = new BufferedReader(reader);
			String line = "";
			line=br.readLine();
			int count=0;
			while(line!=null){
				line=line.replaceAll(" ","");
				line=line.replaceAll("\t","");
				this.Check(line);
				for(int i=0;i<80;i++){
					Matrix[count][i]=line.charAt(i)-'0';
				}
				line=br.readLine();
				count++;
			}
			System.out.println("Analysis Finished");
			System.out.println("Please wait for initialization");
		} catch (Throwable e) {
			System.out.println("Error");
			System.exit(0);
		}
	}
		/**
		*@REQUIRES:s!=null;
		*@MODIFIES:this;
		*@EFFECTS:
		*		(s.matches(regEx))==>(System.exit());
		*/	
	public void Check(String s){

		Pattern p = Pattern.compile(this.regEx);
		Matcher m = p.matcher(s);
		if(m.matches()){
			return;
		}
		else{
			System.out.println("Invalid input in Map.txt");
			System.exit(0);
		}
	}
		/**
		*@EFFECTS: 
		*		\result==Matrix;
		*/	
	public int[][] GetMap(){

		return this.Matrix;
	}
}
