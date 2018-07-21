package Code;

public enum TaxiStatus {
	SERVING(1),TAKING(3),WAITING(2),STOP(0);
	private int num;
		/**
		*@REQUIRES: num!=null;
		*@MODIFIES: this;
		*@EFFECTS: 
		*		true==>(initial the num to TaxiStatus);
		*/	
	private TaxiStatus(int num){
		this.num=num;
	}
		/**
		*@EFFECTS: 
		*		\result==num;
		*/	
	public int GetNum(){
		return this.num;
	}
}
