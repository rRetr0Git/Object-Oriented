package Code;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

public class elevatorTest {
	private elevator e;
	private request r;
	private request_list rl;
	private floor f;
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
		e.repOK();
	}

	@Test
	public void testCheck() {
		assertEquals(e.get_floor(),1);
		assertEquals(e.get_direction(),"UP");
		e=new elevator("(ER,99999,1)");
		assertEquals(e.check(),false);
		e.set("(ER,0,1)");
		assertEquals(e.check(),false);
		e.set("(ER,2,1000000000)");
		assertEquals(e.check(),true);
		e.set("(ER,2,9000000000)");
		assertEquals(e.check(),false);
		e.repOK();
	}

	@Test
	public void testAnalysis() {
		e.set("(ER,10,1)");
		e.check();
		r=e.analysis();
		assertEquals(r.get_floor(),10);
		assertEquals(r.get_str(),"(ER,10,1)");
		assertEquals(r.get_type(),"ER");
		e.repOK();
	}

	@Test
	public void testFindBestRequestRequest_listInt() {
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
		e=new elevator();
		f=new floor();
		r.set_str("(FR,1,UP,0)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,DOWN,1)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,UP,5)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(ER,5,5)");
		e.set(r.get_str());e.check();rl.add(e.analysis());
		rl.remove(0);
		for(int i=0;i<rl.get_size();i++){
			rl.get(i).set_order(i+1);
		}
		scheduler = new scheduler(rl);
		scheduler.command(scheduler.schedule());
		e.repOK();
	}

	@Test
	public void testFindBestRequestRequest_listString() {
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
		e=new elevator();
		f=new floor();
		r.set_str("(FR,1,UP,0)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,DOWN,1)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,UP,5)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(ER,5,5)");
		e.set(r.get_str());e.check();rl.add(e.analysis());
		rl.remove(0);
		for(int i=0;i<rl.get_size();i++){
			rl.get(i).set_order(i+1);
		}
		scheduler = new scheduler(rl);
		scheduler.command(scheduler.schedule());
		e.repOK();
	}

	@Test
	public void testRunRequest() {
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
		e=new elevator();
		f=new floor();
		r.set_str("(FR,1,UP,0)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,DOWN,1)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,UP,5)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(ER,5,5)");
		e.set(r.get_str());e.check();rl.add(e.analysis());
		rl.remove(0);
		for(int i=0;i<rl.get_size();i++){
			rl.get(i).set_order(i+1);
		}
		scheduler = new scheduler(rl);
		scheduler.command(scheduler.schedule());
		e.repOK();
	}

	@Test
	public void testRunRequestRequestInt() {
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
		e=new elevator();
		f=new floor();
		r.set_str("(FR,1,UP,0)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,DOWN,1)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(FR,5,UP,5)");
		f.set(r.get_str());f.check();rl.add(f.analysis());
		r.set_str("(ER,5,5)");
		e.set(r.get_str());e.check();rl.add(e.analysis());
		rl.remove(0);
		for(int i=0;i<rl.get_size();i++){
			rl.get(i).set_order(i+1);
		}
		scheduler = new scheduler(rl);
		scheduler.command(scheduler.schedule());
		e.repOK();
	}

}
