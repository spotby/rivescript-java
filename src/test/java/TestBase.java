import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.rivescript.RiveScriptClient;

public class TestBase {
	protected RiveScriptClient rs;

	public String replies() {
		return "undefined";
	}

	public void setUp(String file) {
		this.rs = new RiveScriptClient();
		this.rs.getEngine().loadFile("src/test/resources/fixtures/" + this.replies() + "/" + file);
		this.rs.getEngine().sortReplies();
	}

	public void setUp(String file, boolean debug) {
		this.rs = new RiveScriptClient(debug);
		this.rs.getEngine().loadFile("src/test/resources/fixtures/" + this.replies() + "/" + file);
		this.rs.getEngine().sortReplies();
	}

	public void extend(String file) {
		this.rs.getEngine().loadFile("src/test/resources/fixtures/" + this.replies() + "/" + file);
		this.rs.getEngine().sortReplies();
	}

	public void uservar(String key, String value) {
		this.rs.setUservar("localuser", key, value);
	}

	public void reply(String input, String expect) {
		String reply = this.rs.reply("localuser", input);
		// System.out.println(input + ": " + expect + " <-> " + reply);
		assertEquals(expect, reply);
	}

	public void reply(String input, String[] expected) {
		String reply = this.rs.reply("localuser", input);
		for (String expect : expected) {
			if (reply.equals(expect)) {
				assertTrue(true);
				return;
			}
		}

		fail();
	}

	@Test
	public void testJunit() {
		String str = "Junit is working fine";
		assertEquals("Junit is working fine", str);
	}
}
