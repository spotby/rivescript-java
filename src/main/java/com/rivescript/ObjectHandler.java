package com.rivescript;

/**
 * Interface for object handlers.
 */

public interface ObjectHandler {
	/**
	 * Handler for when object code is read (loaded) by RiveScript.
	 * Should return true for success or false to indicate error.
	 *
	 * @param name The name of the object.
	 * @param code The source code inside the object.
	 */
	public boolean onLoad (String name, String[] code);

	/**
	 * Handler for when a user invokes the object. Should return the text
	 * reply from the object.
	 *
	 * @param name The name of the object being called.
	 * @param client The user profile
	 * @param args The argument list from the call tag.
	 */
	public String onCall (String name, Client client, String[] args);

	/**
	 * Set a Java class to handle the macro directly.
	 *
	 * This is only useful to the built-in Java handler; other handlers
	 * do not need to implement this function.
	 *
	 * @param name The name of the object macro.
	 * @param impl An implementation class of com.rivescript.ObjectMacro.
	 */
	public void setClass (String name, com.rivescript.ObjectMacro impl);
}
