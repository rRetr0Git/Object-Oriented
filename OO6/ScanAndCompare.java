package Code;

import java.util.HashMap;

public class ScanAndCompare extends Thread{
	private SafeFile file;
	private HashMap<String, String[]> OldMap;
	private HashMap<String, String[]> NewMap;
	private String ScanRange;
	private String Triger;
	private String Task;
	private boolean isFile;
	private boolean Miss;
	private OutputSummary OS;
	private OutputDetail OD;
	
	public ScanAndCompare(String Path,String Type,String Task,OutputSummary OS,OutputDetail OD){
		this.file=new SafeFile(Path);
		if(this.file.isDirectory()){
			this.ScanRange=this.file.getAbsolutePath();
			this.isFile=false;
			this.Miss=false;
		}
		else{
			this.ScanRange=this.file.getParent();
			this.isFile=true;
			this.Miss=true;
		}
		this.OldMap=new HashMap<String, String[]>();
		this.NewMap=new HashMap<String, String[]>();
		this.Scan(ScanRange);
		this.Triger=Type;
		this.Task=Task;
		this.OS=OS;
		this.OD=OD;
	}
	
	@SuppressWarnings("unchecked")
	public void run(){
		try{
			while(true){
				this.OldMap=(HashMap<String, String[]>) this.NewMap.clone();
				this.NewMap.clear();
				Scan(ScanRange);
//				System.out.println("OLD");
//				for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
//			        System.out.println(e.getKey());
//			        for(int i=0;i<e.getValue().length;i++){
//			        	System.out.print(e.getValue()[i]+" ");
//			        }
//			        System.out.println();
//				}	
//				System.out.println("NEW");
//				for(HashMap.Entry<String,String[]> e: NewMap.entrySet() ){
//			        System.out.println(e.getKey());
//			        for(int i=0;i<e.getValue().length;i++){
//			        	System.out.print(e.getValue()[i]+" ");
//			        }
//			        System.out.println();
//				}
				Compare();
				if(this.Miss){
					System.out.println(this.file.getAbsolutePath()+" for "+this.Triger+" is Missing!");
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}	
		}catch(Throwable t){
			System.out.println("Error in SAC");
		}
	}
	
	public void Scan(String Path){
		String[] fileList = (new SafeFile(Path)).list();
		for(int i=0;i<fileList.length;i++){
			String str = fileList[i];
			SafeFile f = new SafeFile(Path+"\\"+str);
			String AbsPath = f.getAbsolutePath();
			if(f.isDirectory()){
				Scan(f.getAbsolutePath());
			}
			else{
				String[] info = {f.getName(),String.valueOf(f.length()),String.valueOf(f.lastModified())};
				NewMap.put(AbsPath,info);
			}
		}
	}
	
	public void Compare(){
		switch(this.Triger){
			case "renamed":renamed();break;
			case "modified":modified();break;
			case "path-changed":path_changed();break;
			case "size-changed":size_changed();break;
			default:break;
		}
	}
	
	public void renamed(){
		String path="";
        String size="";
        String time="";
		if(isFile){
			this.Miss=true;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
		        if(file.getAbsolutePath().equals(e.getKey())){
		        	path=e.getKey();
		        	size=e.getValue()[1];
		        	time=e.getValue()[2];
		        }
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(path.equals(e.getKey())){
					this.Miss=false;
					return;
				}
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(size.equals(e.getValue()[1])&&time.equals(e.getValue()[2])
						&&(new SafeFile(path)).getParent().equals(new SafeFile(e.getKey()).getParent())&&!OldMap.containsKey(e.getKey())){
					this.file=new SafeFile(e.getKey());
					this.Miss=false;
					switch(this.Task){
						case "record-summary":this.OS.add(0);break;
						case "record-detail":{
							this.OD.add(path+" renamed"+System.getProperty("line.separator")
						+path+"->"+e.getKey()+System.getProperty("line.separator")
						+(new SafeFile(path)).getName()+"->"+e.getValue()[0]+System.getProperty("line.separator")
						+size+"->"+e.getValue()[1]+System.getProperty("line.separator")
						+time+"->"+e.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
							break;
						}
						case "recover":{
							SafeFile from = new SafeFile(e.getKey());
							SafeFile to = new SafeFile(path);
							from.renameTo(to);
							String[] info={to.getName(),String.valueOf(to.length()),String.valueOf(to.lastModified())};
							NewMap.put(path,info);
							NewMap.remove(e.getKey());
							this.file=new SafeFile(path);
							break;
						}
					}
					System.out.println(path+" renamed");
					return;
				}
			}
		}
		else{
			int exist=0;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
				exist=0;
		        path=e.getKey();
		        size=e.getValue()[1];
		        time=e.getValue()[2];
		        for(HashMap.Entry<String,String[]> f: NewMap.entrySet()){
					if(path.equals(f.getKey())){
						exist=1;
					}
				}
		        if(exist==0){
			        for(HashMap.Entry<String,String[]> g: NewMap.entrySet()){
			        	if(size.equals(g.getValue()[1])&&time.equals(g.getValue()[2])
								&&(new SafeFile(path)).getParent().equals(new SafeFile(g.getKey()).getParent())&&!OldMap.containsKey(g.getKey())){
			        		switch(this.Task){
								case "record-summary":this.OS.add(0);break;
								case "record-detail":{
									this.OD.add(path+" renamed"+System.getProperty("line.separator")
									+path+"->"+g.getKey()+System.getProperty("line.separator")
									+(new SafeFile(path)).getName()+"->"+g.getValue()[0]+System.getProperty("line.separator")
									+size+"->"+g.getValue()[1]+System.getProperty("line.separator")
									+time+"->"+g.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
									break;
								}
								case "recover":{
									SafeFile from = new SafeFile(g.getKey());
									SafeFile to = new SafeFile(path);
									from.renameTo(to);
									String[] info={to.getName(),String.valueOf(to.length()),String.valueOf(to.lastModified())};
									NewMap.put(path,info);
									NewMap.remove(g.getKey());
									break;
								}
							}
			        		System.out.println(path+" renamed");
			        		break;
						}
			        }	
		        } 
			}
		}
	}

	public void modified(){
		String path="";
		String size="";
        String time="";
		if(isFile){
			this.Miss=true;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
		        if(file.getAbsolutePath().equals(e.getKey())){
		        	path=e.getKey();
		        	size=e.getValue()[1];
		        	time=e.getValue()[2];
		        }
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(path.equals(e.getKey())&&time.equals(e.getValue()[2])){
					this.Miss=false;
					return;
				}
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(path.equals(e.getKey())&&!time.equals(e.getValue()[2])){
					this.Miss=false;
					switch(this.Task){
						case "record-summary":this.OS.add(1);break;
						case "record-detail":{
							this.OD.add(path+" modified"+System.getProperty("line.separator")
							+path+"->"+e.getKey()+System.getProperty("line.separator")
							+(new SafeFile(path)).getName()+"->"+e.getValue()[0]+System.getProperty("line.separator")
							+size+"->"+e.getValue()[1]+System.getProperty("line.separator")
							+time+"->"+e.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
							break;
						}
					}
					System.out.println(path+" modified");
					return;
				}
			}
		}
		else{
			int exist=0;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
				exist=0;
		        path=e.getKey();
		        size=e.getValue()[1];
		        time=e.getValue()[2];
		        for(HashMap.Entry<String,String[]> f: NewMap.entrySet()){
					if(path.equals(f.getKey())&&time.equals(f.getValue()[2])){
						exist=1;
					}
				}
		        if(exist==0){
			        for(HashMap.Entry<String,String[]> g: NewMap.entrySet()){
						if(path.equals(g.getKey())&&!time.equals(g.getValue()[2])){
							switch(this.Task){
								case "record-summary":this.OS.add(1);break;
								case "record-detail":{
									this.OD.add(path+" modified"+System.getProperty("line.separator")
									+path+"->"+g.getKey()+System.getProperty("line.separator")
									+(new SafeFile(path)).getName()+"->"+g.getValue()[0]+System.getProperty("line.separator")
									+size+"->"+g.getValue()[1]+System.getProperty("line.separator")
									+time+"->"+g.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
									break;
								}
							}
							System.out.println(path+" modified");
						}
			        }	
		        }
			}
		}
	}
	
	public void path_changed(){
		String path="";
		String name="";
        String size="";
        String time="";
		if(isFile){
			this.Miss=true;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
		        if(file.getAbsolutePath().equals(e.getKey())){
		        	path=e.getKey();
		        	name=e.getValue()[0];
		        	size=e.getValue()[1];
		        	time=e.getValue()[2];
		        }
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(path.equals(e.getKey())){
					this.Miss=false;
					return;
				}
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(name.equals(e.getValue()[0])&&size.equals(e.getValue()[1])&&time.equals(e.getValue()[2])
						&&!(new SafeFile(path)).getParent().equals(new SafeFile(e.getKey()).getParent())&&!OldMap.containsKey(e.getKey())){
					this.file=new SafeFile(e.getKey());
					this.Miss=false;
					this.ScanRange=(new SafeFile(e.getKey())).getParent();
					switch(this.Task){
						case "record-summary":this.OS.add(2);break;
						case "record-detail":{
							this.OD.add(path+" path-changed"+System.getProperty("line.separator")
							+path+"->"+e.getKey()+System.getProperty("line.separator")
							+(new SafeFile(path)).getName()+"->"+e.getValue()[0]+System.getProperty("line.separator")
							+size+"->"+e.getValue()[1]+System.getProperty("line.separator")
							+time+"->"+e.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
							break;
						}
						case "recover":{
							SafeFile from = new SafeFile(e.getKey());
							SafeFile to = new SafeFile(path);
							from.renameTo(to);
							String[] info={to.getName(),String.valueOf(to.length()),String.valueOf(to.lastModified())};
							NewMap.put(path,info);
							NewMap.remove(e.getKey());
							this.file=new SafeFile(path);
							this.ScanRange=(new SafeFile(path).getParent());
							break;
						}
					}
					System.out.println(path+" path-changed");
					return;
				}
			}
		}
		else{
			int exist=0;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
				exist=0;
		        path=e.getKey();
		        name=e.getValue()[0];
		        size=e.getValue()[1];
		        time=e.getValue()[2];
		        for(HashMap.Entry<String,String[]> f: NewMap.entrySet()){
					if(path.equals(f.getKey())){
						exist=1;
					}
				}
		        if(exist==0){
			        for(HashMap.Entry<String,String[]> g: NewMap.entrySet()){
			        	if(name.equals(g.getValue()[0])&&size.equals(g.getValue()[1])&&time.equals(g.getValue()[2])
								&&!(new SafeFile(path)).getParent().equals(new SafeFile(g.getKey()).getParent())&&!OldMap.containsKey(g.getKey())){
			        		switch(this.Task){
								case "record-summary":this.OS.add(2);break;
								case "record-detail":{
									this.OD.add(path+" path-changed"+System.getProperty("line.separator")
									+path+"->"+g.getKey()+System.getProperty("line.separator")
									+(new SafeFile(path)).getName()+"->"+g.getValue()[0]+System.getProperty("line.separator")
									+size+"->"+g.getValue()[1]+System.getProperty("line.separator")
									+time+"->"+g.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
									break;
								}
								case "recover":{
									SafeFile from = new SafeFile(g.getKey());
									SafeFile to = new SafeFile(path);
									from.renameTo(to);
									String[] info={to.getName(),String.valueOf(to.length()),String.valueOf(to.lastModified())};
									NewMap.put(path,info);
									NewMap.remove(g.getKey());
									break;
								}
							}
			        		System.out.println(path+" path-changed");
			        		break;
						}
			        }	
		        } 
			}
		}
	}
	
	public void size_changed(){
		String path="";
        String size="";
        String time="";
		if(isFile){
			this.Miss=true;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
		        if(file.getAbsolutePath().equals(e.getKey())){
		        	path=e.getKey();
		        	size=e.getValue()[1];
		        	time=e.getValue()[2];
		        }
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(path.equals(e.getKey())&&(size.equals(e.getValue()[1])||time.equals(e.getValue()[2]))){
					this.Miss=false;
					return;
				}
			}
			for(HashMap.Entry<String,String[]> e: NewMap.entrySet()){
				if(path.equals(e.getKey())&&!size.equals(e.getValue()[1])
						&&!time.equals(e.getValue()[2])){
					this.Miss=false;
					switch(this.Task){
						case "record-summary":this.OS.add(3);break;
						case "record-detail":{
							this.OD.add(path+" size-changed"+System.getProperty("line.separator")
							+path+"->"+e.getKey()+System.getProperty("line.separator")
							+(new SafeFile(path)).getName()+"->"+e.getValue()[0]+System.getProperty("line.separator")
							+size+"->"+e.getValue()[1]+System.getProperty("line.separator")
							+time+"->"+e.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
							break;
						}
					}
					System.out.println(path+" size-changed");
					return;
				}
			}
			return;
		}
		else{
			int exist=0;
			for(HashMap.Entry<String,String[]> e: OldMap.entrySet() ){
				exist=0;
		        path=e.getKey();
		        size=e.getValue()[1];
		        time=e.getValue()[2];
		        for(HashMap.Entry<String,String[]> f: NewMap.entrySet()){
					if(path.equals(f.getKey())&&(size.equals(f.getValue()[1])||time.equals(f.getValue()[2]))){
						exist=1;
					}
				}
		        if(exist==0){
			        for(HashMap.Entry<String,String[]> g: NewMap.entrySet()){
						if(path.equals(g.getKey())&&!size.equals(g.getValue()[1])
								&&!time.equals(g.getValue()[2])){
							switch(this.Task){
								case "record-summary":this.OS.add(3);break;
								case "record-detail":{
									this.OD.add(path+" size-changed"+System.getProperty("line.separator")
									+path+"->"+g.getKey()+System.getProperty("line.separator")
									+(new SafeFile(path)).getName()+"->"+g.getValue()[0]+System.getProperty("line.separator")
									+size+"->"+g.getValue()[1]+System.getProperty("line.separator")
									+time+"->"+g.getValue()[2]+System.getProperty("line.separator")+System.getProperty("line.separator"));
									break;
								}
							}
							System.out.println(path+" size-changed");
						}
			        }	
		        }   
			}
		}
	}
}
