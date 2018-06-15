package Code;

public class IFTTT {
	private String path;
	private String triger;
	private String task;
	
	public IFTTT(String path,String triger,String task){
		this.path=path;
		this.triger=triger;
		this.task=task;
	}
	
	public String getPath(){
		return this.path;
	}
	
	public String getTriger(){
		return this.triger;
	}
	
	public String getTask(){
		return this.task;
	}
}
