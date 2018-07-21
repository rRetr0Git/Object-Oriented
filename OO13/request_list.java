package Code;

public class request_list {
	
	/**
	 *@OVERVIEW: request_list is a class like arraylist, which has some basic function.
	 */
	
	private request[] list;
	private int size;
	 
	/**
	*@MODIFIES:this;this.size;
	*@EFFECTS: 
	*		true==>(A new request_list has been constructed);
	*/	
	public request_list(){
		this.repOK();
		list = new request[2];
		size=-1;
		this.repOK();
		size=0;
	}

	/**
	*@REQUIRES:req!=null;req.time!=99999999;req.floor!=11;req.type!="NULL";type.direction!="NULL";
	*@MODIFIES:this;this.size;system.out;
	*@EFFECTS: 
	*		(the request is in time sort and right format)==>(list.contains(r)&& this.size ==\old(this.size)+1);
	*		(not enough space)==>(make a new list)
	*/
	public void add(request req){
		if(size!=0){
			if(list[size-1].get_time()<=req.get_time()){
				list[size] = req;
				size++;
				if(size==list.length){
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
			if(req.get_type().equals("FR")&&req.get_direction().equals("UP")&&req.get_floor()==1&&req.get_time()==0){
				list[size] = req;
				size++;
			}
			else{
				System.out.println("INVALID["+req.get_str()+"]");
			}
		}
	}
	/**
	*@REQUIRES:index!=null;index>=0;index<this.size;
	*@EFFECTS: 
	*		\result==list.get(index);
	*/	
	public request get(int index){
		return list[index];
	}
	/**
	*@REQUIRES:index!=null;0<=index<\old(this.size);
	*@MODIFIES:this;this.size;
	*@EFFECTS: 
	*		!list.contains(\old(list).get(index))
	*		this.size==\old(this.size)-1;
	*/	
	public void remove(int index){
		for(int i=index;i<size;i++){
			list[i]=list[i+1];
		}
		size--;
	}
	/**
	*@REQUIRES:index!=null;0<=index<\old(this.size));
	*@MODIFIES:this;;
	*@EFFECTS: 
	*		change the order of the list and make a request to the top;
	*/
	public void top(int index){
		request a = list[index];
		for(int i=index;i>0;i--){
			list[i]=list[i-1];
		}
		list[0]=a;
	}
	/**
	*@EFFECTS: 
	*		\result==this.size;
	*/
	public int get_size(){
		return size;
	}
	/**
	 * @Effects: \result==invariant(this);
	 */
	public boolean repOK(){
		if(list==null)return false;
		if(size<0)return false;
		return true;
	}
}

