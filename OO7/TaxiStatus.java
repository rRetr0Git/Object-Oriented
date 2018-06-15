package Code;

public enum TaxiStatus {
	SERVING(1),TAKING(3),WAITING(2),STOP(0);
	private int num;
	private TaxiStatus(int num){
		this.num=num;
	}
	public int GetNum(){
		return this.num;
	}
}
