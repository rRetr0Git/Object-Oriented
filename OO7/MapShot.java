package Code;

import java.awt.Point;

public class MapShot {
	
	private int[][][] Shot;
	public MapShot(){
		this.Shot=new int[80][80][100];
		this.Clear();
	}
	
	public void Clear(){
		for(int i=0;i<80;i++){
			for(int j=0;j<80;j++){
				for(int k=0;k<100;k++){
					if(k!=-1){
						this.Shot[i][j][k]=-1;
					}
					else{
						break;
					}
				}
			}
		}
	}
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
	public void Set(int[] Info){
		int x = Info[1];
		int y = Info[2];
		int id = Info[0];
		int length=this.ValidLength(x,y);
		this.Shot[x][y][length]=id;
	}
	public int GetId(int x,int y,int z){
		return this.Shot[x][y][z];
	}
}
