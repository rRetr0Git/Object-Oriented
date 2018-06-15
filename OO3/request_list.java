package Code;

public class request_list {
	
	private request[] list;
	private int size;
	
	public request_list(int size){
		list = new request[size];
		this.size=0;
	}
	 
	public request_list(){
		list = new request[16];
		size=0;
	}
	
	public void add(request req){
		if(size!=0){
			if(list[size-1].get_time()<=req.get_time()){
				list[size] = req;
				size++;
				if(size>=list.length){
					int newCapacity = list.length*2;
					request[] newlist = new request[newCapacity];
					for(int i=0;i<list.length;i++){
						newlist[i] = list[i];
					}
					list = newlist;
				}
			}
			else{
				System.out.println("INVALID["+req.get_str()+"]");
			}
		}
		else{
			if(req.get_str().equals("(FR,1,UP,0)")){
				list[size] = req;
				size++;
				if(size>=list.length){
					int newCapacity = list.length*2;
					request[] newlist = new request[newCapacity];
					for(int i=0;i<list.length;i++){
						newlist[i] = list[i];
					}
					list = newlist;
				}
			}
			else{
				System.out.println("INVALID["+req.get_str()+"]");
			}
		}
	}
	
	public request get(int index){
		return list[index];
	}
	
	public void remove(int index){
		for(int i=index;i<size;i++){
			list[i]=list[i+1];
		}
		size--;
	}
	
	public void top(int index){
		request a = list[index];
		for(int i=index;i>0;i--){
			list[i]=list[i-1];
		}
		list[0]=a;
	}
	
	public int get_size(){
		return size;
	}
}

