package Code;

public interface elevator_interface {
	
	public boolean check();
	public request analysis();
	public int get_floor();
	public void run(request r);
	public int FindBest(request r,request_list rl,int index);
	
}
