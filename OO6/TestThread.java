package Code;

public class TestThread extends Thread{

	public boolean addFile(String path){
		SafeFile sf = new SafeFile(path);
		return sf.createNewFile();
	}
	
	public boolean rename(String from,String to){
		SafeFile fromsf = new SafeFile(from);
		SafeFile tosf = new SafeFile(to);
		return fromsf.renameTo(tosf);
	}
	
	public boolean delete(String src){
		SafeFile sf = new SafeFile(src);
		return sf.delete();
	}
	
	public boolean move(String from,String to){
		SafeFile fromsf = new SafeFile(from);
		SafeFile tosf = new SafeFile(to);
		return fromsf.move(tosf);
	}
	
	public boolean changeSize(String file){
		SafeFile sf = new SafeFile(file);
		return sf.changeSize();
	}
	
	public boolean changeTime(String file,long time){
		SafeFile sf = new SafeFile(file);
		return sf.changeTime(time);
	}
	
	public boolean mkdir(String path){
		SafeFile sf = new SafeFile(path);
		return sf.mkdir();
	}
	
	public boolean testcase(){
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			return false;
//		}
//		if(!changeTime("D:\\Monitor\\test.txt",60))return false;
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			return false;
//		}
//		if(!move("D:\\Monitor\\test.txt","D:\\Monitor\\111\\test.txt"))return false;
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			return false;
//		}
//		if(!changeSize("D:\\Monitor\\333\\04.txt"))return false;
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			return false;
//		}
//		if(!changeSize("D:\\Monitor\\333\\111\\1111\\00.txt"))return false;
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			return false;
//		}
//		if(!rename("D:\\Monitor\\test.txt","D:\\Monitor\\1111.txt"))return false;
		return true;
	}
	
	public void run(){
		try{
			if(!testcase()){
				System.out.println("Wrong Operation");
			}
			try{
				Thread.sleep(10000);
				System.exit(0);
			}catch(Throwable t){
				System.out.println("Error in Test");
			}
		}catch(Throwable t){
			System.out.println("Error in Test");
		}
	}
}
