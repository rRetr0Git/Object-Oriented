package Code;

public class scheduler_re extends scheduler {

	public scheduler_re(request_list r) {
		super(r);
	}
	//constructor
	 
		public request schedule(){
			return this.r_list.get(0);
		}
		
		public void command(request r){
			r.remove_repeat(e,r_list,0);
			int p = e.FindBest(r, r_list,1);
			int q = e.FindBest(r, r_list,1);
			request b = new request("");
			request c = new request("");
			int FLAG=0;
			while(p!=999){
				FLAG=0;
				System.out.println("#Find:"+r_list.get(p).get_str());
				//����ִ�������ڵ�ǰ¥���Ŀ��¥��֮��������Ӵ�
					r_list.top(p);
					r_list.get(0).remove_repeat(e,r_list,0);	
					q= e.FindBest(r,r_list,2);
					if(q!=999&&p!=q&&r_list.get(0).get_floor()==r_list.get(q).get_floor()){
						FLAG=1;//�����Ӵ�ͬʱ���
						r_list.top(q);
						r_list.get(0).remove_repeat(e, r_list, 0);
						b=r_list.get(0);
						r_list.remove(0);
						if(r_list.get(0).get_floor()==r.get_floor()){
							FLAG=3;//�����Ӵ���һ��������ͬʱ���
							c=r;
							r_list.remove(1);
						}
					}
					else if(r_list.get(0).get_floor()==r.get_floor()){
						FLAG=2;//һ���Ӵ���һ��������ͬʱ���
						b=r;
						r_list.remove(1);
					}
					//�ö�����Ӵ�����	
					e.run(r_list.get(0),r,1);
					if(FLAG==1||FLAG==2){
						if(r_list.get(0).get_order()<b.get_order()){
							System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
							System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
						}
						else{
							System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
							System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
						}
					}
					else if(FLAG==3){
						if(r_list.get(0).get_order()>b.get_order()){
							if(b.get_order()>c.get_order()){
								System.out.println("["+c.get_str().substring(1, c.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
							}
							else{
								if(r_list.get(0).get_order()>c.get_order()){
									System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+c.get_str().substring(1, c.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								}
								else{
									System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+c.get_str().substring(1, c.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								}
							}
						}
						else{
							if(b.get_order()<c.get_order()){
								System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								System.out.println("["+c.get_str().substring(1, c.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
							}
							else{
								if(r_list.get(0).get_order()<c.get_order()){
									System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+c.get_str().substring(1, c.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								}
								else{
									System.out.println("["+c.get_str().substring(1, c.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
									System.out.println("["+b.get_str().substring(1, b.get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
								}
							}
						}
					}
					else{
						System.out.println("["+r_list.get(0).get_str().substring(1, r_list.get(0).get_str().length()-1)+"]/("+e.get_floor()+","+e.get_direction()+","+String.format("%.1f)", e.get_Time()-1));
					}
					if(r_list.get_size()>0){
						r_list.get(0).remove_repeat(e,r_list,1);
					}
					r_list.remove(0);
					r_list.get(0).remove_repeat(e,r_list,0);
					p = e.FindBest(r, r_list,1);
			}
			if(FLAG!=2&&FLAG!=3){
				e.run(r);
				r.remove_repeat(e,r_list,1);
				r_list.remove(0);
			}
			p=e.FindBest(r, r_list, "");
			if(p!=999){
				System.out.println("#Find:"+r_list.get(p).get_str());
				//�ö�δ��ɵ�����Ӵ�����
				r_list.top(p);
				this.command(this.schedule());
			}
		}
		
}
