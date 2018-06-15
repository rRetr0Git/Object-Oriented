package Code;

public interface SchedulableCarrier {
	public void MoveUp(Request r);
	public void MoveDown(Request r);
	public void Stay(Request r);
}
