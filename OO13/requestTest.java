package Code;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class requestTest {
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
		r1 = new request("(FR,2,DOWN,010)");
		r2 = new request("(ER,5,15)");
		r3 = new request("123123123asdsa");
		r4 = new request("(FR,+6,UP,111110)");
		e = new elevator(r1.get_str());
		f = new floor(r1.get_str());
	}

	@Test
	public void testCheck() {
		//request部分的格式检查（仅判断正则正确性）
		assertEquals(true,r1.check());
		assertEquals(true,r2.check());
		assertEquals(false,r3.check());
		assertEquals(true,r4.check());
	}

	@Test
	public void testRemove_repeat() {
		r1.set_str("(FR,1,UP,0)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		r1.set_str("(FR,1,UP,0)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		r1.set_str("(FR,1,UP,0)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		r1.set_str("(FR,2,DOWN,0)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		rl.get(0).remove_repeat(e, rl, 1);
		//测试以当前电梯时间去除SAME请求
		assertEquals(rl.get_size(),2);
		r2.set_str("(ER,5,1.5)");
		e.set(r2.get_str());r2.check();e.check();rl.add(e.analysis());
		r3.set_str("(ER,5,2)");
		e.set(r3.get_str());r3.check();e.check();rl.add(e.analysis());
		r4.set_str("(ER,5,2.5)");
		e.set(r4.get_str());r4.check();e.check();rl.add(e.analysis());
		r4.set_str("(ER,4,3)");
		e.set(r4.get_str());r4.check();e.check();rl.add(e.analysis());
		r1.set_str("(FR,1,UP,3)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		r1.set_str("(FR,1,UP,6)");
		f.set(r1.get_str());r1.check();f.check();rl.add(f.analysis());
		rl.remove(0);	rl.remove(0);
		rl.get(0).remove_repeat(e, rl, 0);
		//测试以将到达时间去除SAME请求
		assertEquals(rl.get_size(),4);
		//测试这两个方法没有调用到的函数正确性
		r4.set_order(5);
		assertEquals(5,r4.get_order());
	}

}
