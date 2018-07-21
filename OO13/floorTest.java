package Code;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class floorTest {
	private floor f;
	private request r;
	@Before
	public void setUp() throws Exception {
			f = new floor();
	}

	@Test
	public void testCheck() {
		f=new floor("(FR,99999,UP,1)");
		assertEquals(f.check(),false);
		f.set("(FR,0,UP,1)");
		assertEquals(f.check(),false);
		f.set("(FR,10,UP,1)");
		assertEquals(f.check(),false);
		f.set("(FR,10,DOWN,1)");
		assertEquals(f.check(),true);
		f.set("(FR,1,DOWN,1)");
		assertEquals(f.check(),false);
		f.set("(FR,1,UP,1)");
		assertEquals(f.check(),true);
		f.set("(FR,2,UP,9999999999)");
		assertEquals(f.check(),false);
		f.set("(FR,2,UP,1)");
		assertEquals(f.check(),true);
	}

	@Test
	public void testAnalysis() {
		f.set("(FR,10,DOWN,1)");
		f.check();
		r=f.analysis();
		assertEquals(r.get_direction(),"DOWN");
		assertEquals(r.get_floor(),10);
		assertEquals(r.get_str(),"(FR,10,DOWN,1)");
		assertEquals(r.get_type(),"FR");
	}

}
