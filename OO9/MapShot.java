package Code;

import java.awt.Point;

public class MapShot {
	
	private int[][][] Shot;
		/**
		*@MODIFIES: this.Shot;
		*@EFFECTS: 
		*		true==>(A new MapShot has been constructed)==>(Clear());
		*		
		*/	
	public MapShot(){

		this.Shot=new int[80][80][100];
		this.Clear();
	}
		/**
		*@MODIFIES: this.Shot;
		*@EFFECTS: 
		*		(\all int i,j,k;0<=i,j<80,0<=k<100,Shot[i][j][k]!=-1)==>(Shot[i][j][k]==-1);
		*/	
	public void Clear(){

		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				for(int k=0;k<100;k++){
					if(this.Shot[i][j][k]!=-1){
						this.Shot[i][j][k]=-1;
					}
					else{
						break;
					}
				}
			}
		}
	}
		/**
		*@REQUIRES:x!=null,y!=null;
		*@EFFECTS: 
		*		\result==(A certain integer that represents the validlength of Shot[x][y]);
		*/	
	public int ValidLength(int x,int y){

		int length=0;
		for(int i=0;i<100;i++){
			if(this.Shot[x][y][i]!=-1){
				length++;
			}
			else{
				break;
			}
		}
		return length;
	}
		/**
		*@REQUIRES:Info!=null;
		*@MODIFIES:Shot;
		*@EFFECTS: 
		*		Shot[Info[1]][Info[2]][ValidLength()]==Info[0];
		*/	
	public void Set(int[] Info){

		int x = Info[1];
		int y = Info[2];
		int id = Info[0];
		int length=this.ValidLength(x,y);
		this.Shot[x][y][length]=id;
	}
		/**
		*@REQUIRES:x!=null,y!=null,z!=null;
		*@EFFECTS: 
		*		\result==Shot[x][y][z];
		*/	
	public int GetId(int x,int y,int z){

		return this.Shot[x][y][z];
	}
}
