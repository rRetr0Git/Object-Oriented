package Code;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

public class schedulerTest {
	private request r;
	private floor f;
	private elevator e;
	private request_list rl;
	@Before
	public void setUp() throws Exception {
		r=new request("");
		f=new floor();
		e=new elevator();
		rl=new request_list();
		File file = new File("5w.txt");
		BufferedReader reader=new BufferedReader(new FileReader(file)); 
		String temp=reader.readLine();
		while(!temp.equals("RUN")){
			if(temp.charAt(1)=='F'){
				r.set_str(temp);
				f.set(r.get_str());f.check();rl.add(f.analysis());
			}
			else{
				r.set_str(temp);
				e.set(r.get_str());e.check();rl.add(e.analysis());
			}
			temp=reader.readLine();
		}
	}

	@Test
	public void testCommand() {
		for(int i=0;i<rl.get_size();i++){
			rl.get(i).set_order(i+1);
		}
		scheduler scheduler = new scheduler(rl);
		while(rl.get_size()>0){
			scheduler.command(scheduler.schedule());
		}
		assertEquals(scheduler.e.get_direction(),"UP");
		assertEquals(scheduler.e.get_floor(),9);
		assertEquals(rl.get_size(),0);
		e=new elevator();
		f=new floor();
		r.set_str("(FR,1,UP,0)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,DOWN,1)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,UP,1)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(ER,5,1)");
		e.set(r.get_str());e.check();rl.add(e.analysis());
		rl.remove(0);
		for(int i=0;i<rl.get_size();i++){
			rl.get(i).set_order(i+1);
		}
		scheduler = new scheduler(rl);
		scheduler.command(scheduler.schedule());	
		assertEquals(scheduler.e.get_direction(),"UP");
		assertEquals(scheduler.e.get_floor(),5);
		assertEquals(rl.get_size(),0);
		scheduler.repOK();
	}

}
