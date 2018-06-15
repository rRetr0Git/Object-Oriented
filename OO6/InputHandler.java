package Code;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler extends Thread{
	private IFTTTLIST list;
	private int limit;
	private final String regEx="^IF \\S+ (modified|renamed|size-changed|path-changed) THEN (record-summary|record-detail|recover)$";
	public InputHandler(IFTTTLIST list){
		this.list=list;
		this.limit=0;
	}	
	public void run(){
		try{
			Scanner s = new Scanner(System.in);
			scan(s);	
		}catch(Throwable t){
			System.out.println("Error in Input");
		}
	}
	
	public void scan(Scanner s){
		try{
			int begin=1;
			String line = s.nextLine();
			while(!line.equals("END")){
				Pattern p1 = Pattern.compile(this.regEx);
				Matcher m1 = p1.matcher(line);
				if(m1.matches()){
					String[] elements = line.split(" ");
					String path = elements[1];
					String triger = elements[2];
					String task = elements[4];
					SafeFile sf = new SafeFile(path);
					if(task.equals("recover")&&(triger.equals("size-changed")||triger.equals("modified"))){
						System.out.println("Wrong Format");
						line=s.nextLine();
						continue;
					}
					if(begin==1){
						if(sf.exists()&&sf.isAbsolute()){
								path=sf.getAbsolutePath();
								IFTTT ifttt = new IFTTT(path,triger,task);
								System.out.println("add "+path+triger+task);
								begin=0;
								this.limit++;
								this.list.add(ifttt);
							}	
						else{
							System.out.println("#Invalid path");
						}
					}
					else{
						int mark=0;
						if(sf.exists()&&sf.isAbsolute()){
							int flag=0;
							path=sf.getAbsolutePath();
							for(int i=0;i<this.list.size();i++){
								if(path.equals(this.list.get(i).getPath())&&triger.equals(this.list.get(i).getTriger())
										&&task.equals(this.list.get(i).getTask())){
									System.out.println("#Same");
									flag=1;
									break;
								}
							}
							if(flag==0){
								for(int j=0;j<this.list.size();j++){
									if(path.equals(this.list.get(j).getPath())){
										mark=1;
										break;
									}
								}
								if(mark==0){
									if(this.limit<10){
										this.limit++;
									}
									else{
										System.out.println("More than 10");
										line=s.nextLine();
										continue;
									}
								}
								IFTTT ifttt = new IFTTT(path,triger,task);
								System.out.println("add "+path+triger+task);
								this.list.add(ifttt);
							}
						}
						else{
							System.out.println("#Invalid path");
						}
					}	
				}
				else{
					System.out.println("Wrong Format");
				}
				line=s.nextLine();
			}
		}catch(Throwable t){
			System.out.println("Error in Input");
		}
	}
}
