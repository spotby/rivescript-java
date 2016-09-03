package com.rivescript;

/**
 * Interface for object macros written in Java.
 */

public interface ObjectMacro {
	/**
	 * The implementation of your object macro function.
	 *
	 * This code is executed when a `call` tag in a RiveScript reply
	 * wants to call your object macro.
	 *
	 * @param rs   A reference to the parent RiveScript engine instance.
	 * @param args An array of the word-arguments from the call tag.
	 * @return A string result of your macro.
	 */
	public String call (com.rivescript.RiveScriptEngine engine, Client client, String[] args);
}
