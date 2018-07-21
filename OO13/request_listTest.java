package Code;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class request_listTest {
	
	private request_list rl;
	private request r1;
	private request r2;
	private request r3;
	private request r4;
	private elevator e;
	private floor f;
	@Before
	
	public void setUp() throws Exception {
		rl = new request_list();
		r1 = new request("(FR,2,DOWN,0)");
		r2 = new request("(ER,5,5)");
		r3 = new request("(ER,6,2)");
		r4 = new request("(FR,6,UP,10)");
		e = new elevator(r1.get_str());
		f = new floor(r1.get_str());
		rl.repOK();
	}
	
	@Test
	public void testAdd() {
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		//首条不匹配
		assertNotEquals(1,rl.get_size());
		r1.set_str("(ER,2,0)");
		e.set(r1.get_str());r1.check();e.check();rl.add(e.analysis());
		//首条不匹配
		assertNotEquals(1,rl.get_size());
		r1.set_str("(FR,1,UP,4)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		//首条不匹配
		assertNotEquals(1,rl.get_size());
		r1.set_str("(FR,1,UP,0)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		//FR正常add
		assertEquals(r1.get_str(),rl.get(0).get_str());
		e.set(r2.get_str());r2.check();e.check();rl.add(e.analysis());
		//ER正常add
		assertEquals(r2.get_str(),rl.get(1).get_str());
		e.set(r3.get_str());r3.check();e.check();rl.add(e.analysis());
		//时间乱序add
		assertNotEquals(3,rl.get_size());
		f.set(r4.get_str());r4.check();f.check();rl.add(f.analysis());
		assertEquals(r4.get_str(),rl.get(2).get_str());
		rl.repOK();
	}

	@Test
	public void testRemove() {
		testAdd();
		rl.remove(2);
		assertEquals(2,rl.get_size());
		rl.remove(0);
		assertEquals(r2.get_str(),rl.get(0).get_str());
		rl.repOK();
	}

	@Test
	public void testTop() {
		testAdd();
		rl.top(2);
		assertEquals(r4.get_str(),rl.get(0).get_str());
		rl.top(2);
		assertEquals(r2.get_str(),rl.get(0).get_str());
		rl.repOK();
	}

}
