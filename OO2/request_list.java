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
				System.out.println("ERROR\n#time unsorted");
			}
		}
		else{
			if(req.get_time()==0){
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
				System.out.println("ERROR\n#first t should be 0");
			}
		}
	}
	
	public request get(int index){
		if(index<0||index>size-1){
			System.out.println("invalid index");
			System.exit(0);
		}
		return list[index];
	}
	
	public int get_size(){
		return size;
	}
}

