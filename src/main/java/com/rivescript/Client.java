package com.rivescript;

import java.util.HashMap;
import java.util.Map;

public interface Client {

	/**
	 * Set a variable for the client.
	 *
	 * @param name  The name of the variable.
	 * @param value The value to set in the variable.
	 */
	void set(String name, String value);

	String getLastMatch();

	String getId();

	/**
	 * Get a variable from the client. Returns the text "undefined" if it doesn't
	 * exist.
	 *
	 * @param name The name of the variable.
	 */
	String get(String name);

	/**
	 * Delete a variable for the client.
	 *
	 * @param name The name of the variable.
	 */
	void delete(String name);

	/**
	 * Retrieve a hashmap of all the user's vars and values.
	 */
	Map<String, String> getData();

	/**
	 * Replace the internal hashmap with this new data (dangerous!).
	 */
	boolean setData(HashMap<String, String> newdata);

	/**
	 * Add a line to the user's input history.
	 */
	void addInput(String text);

	/**
	 * Add a line to the user's reply history.
	 */
	void addReply(String text);

	/**
	 * Get a specific input value by index.
	 *
	 * @param index The index of the input value to get (1-9).
	 */
	String getInput(int index) throws java.lang.IndexOutOfBoundsException;

	/**
	 * Get a specific reply value by index.
	 *
	 * @param index The index of the reply value to get (1-9).
	 */
	String getReply(int index) throws java.lang.IndexOutOfBoundsException;

}