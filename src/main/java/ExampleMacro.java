/**
 * An example object macro written in Java.
 *
 * To define a Java object macro, you must implement the interface
 * com.rivescript.ObjectMacro and register it using setSubroutine().
 *
 * This macro does two things: returns their message reversed, and sets
 * a user variable named `java`.
 *
 * This implements the `reverse` object macro used in Aiden/obj-java.rive
 *
 * See RSBot.java for more details.
 */

import com.rivescript.Client;
import com.rivescript.ObjectMacro;
import com.rivescript.RiveScriptEngine;

public class ExampleMacro implements ObjectMacro {
	public String call (RiveScriptEngine rs, Client client, String[] args) {
		String message = String.join(" ", args);

		client.set("java", "This variable was set by Java "
			+ "when you said 'reverse " + message + "'");

		// Reverse their message and return it.
		return new StringBuilder(message).reverse().toString();
	}
}
