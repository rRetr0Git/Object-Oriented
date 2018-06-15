package Code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SafeFile {
	private File file;
	
	public SafeFile(String path){
		file = new File(path);
	}
	public synchronized boolean exists(){
		return file.exists();
	}
	public synchronized String getAbsolutePath(){
		return file.getAbsolutePath();
	}
	public synchronized long length(){
		return file.length();
	}
	public synchronized long lastModified(){
		return file.lastModified();
	}
	public synchronized String getName(){
		return file.getName();
	}
	public synchronized String getParent(){
		return file.getParent();
	}
	public synchronized boolean isFile(){
		return file.isFile();
	}
	public synchronized boolean isDirectory(){
		return file.isDirectory();
	}
	public synchronized String[] list(){
		return file.list();
	}
	public synchronized boolean isAbsolute(){
		return this.file.isAbsolute();
	}
	public synchronized File getFile(){
		return this.file;
	}
	public synchronized boolean createNewFile(){
		try {
			return this.file.createNewFile();
		} catch (Throwable t) {
			return false;
		}
	}
	public synchronized boolean renameTo(SafeFile dest){
		try{
			return this.file.renameTo(dest.getFile());	
		}catch(Throwable t){
			return false;
		}
		
	}
	public synchronized boolean delete(){
		try{
			return this.file.delete();
		}catch(Throwable t){
			return false;
		}
	}
	public synchronized boolean move(SafeFile dest){
		try{
			return this.file.renameTo(dest.getFile());
		}catch(Throwable t){
			return false;
		}
	}
	public synchronized boolean changeTime(long time){
		try{
			return this.file.setLastModified(time);
		}catch(Throwable t){
			return false;
		}
	}
	public synchronized boolean changeSize(){
		try {
			OutputStream output = new FileOutputStream(this.file,true);
			String msg = "12345";
			byte data[] = msg.getBytes();
			try {
				output.write(data);
				output.close();
			} catch (IOException e) {
				return false;
			}
			return true;
		}catch(Throwable t){
			return false;
		}
	}
	public synchronized boolean mkdir(){
		try{
			return this.file.mkdir();
		}catch(Throwable t){
			return false;
		}
	}
}
