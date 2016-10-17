/*
	com.rivescript.RiveScript - The Official Java RiveScript Interpreter

	Copyright (c) 2016 Noah Petherbridge

	Permission is hereby granted, free of charge, to any person obtaining a copy
	of this software and associated documentation files (the "Software"), to deal
	in the Software without restriction, including without limitation the rights
	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	copies of the Software, and to permit persons to whom the Software is
	furnished to do so, subject to the following conditions:

	The above copyright notice and this permission notice shall be included in all
	copies or substantial portions of the Software.

	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	SOFTWARE.
*/

package com.rivescript;

import java.lang.String;
import java.util.HashMap;

/**
 * An object to represent an individual user's data.
 */

public class SimpleClient implements Client  {
	private String id;
	private HashMap<String, String> data = new HashMap<String, String>(); // User data
	private String[] input = new String [10]; // User's inputs
	private String[] reply = new String [10]; // Bot's replies

	/**
	 * Create a new client object.
	 *
	 * @param id A unique ID for this client.
	 */
	public SimpleClient (String id) {
		this.id = id;

		// Set default vars.
		set("topic","random");

		// Initialize the user's history.
		for (int i = 0; i < input.length; i++) {
			input[i] = "undefined";
			reply[i] = "undefined";
		}
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#set(java.lang.String, java.lang.String)
	 */
	@Override
	public void set (String name, String value) {
		data.put(name, value);
	}
	
	/* (non-Javadoc)
	 * @see com.rivescript.Client#getLastMatch()
	 */
	@Override
	public String getLastMatch () {
		return this.get( "__lastmatch__");
	}
	
	/* (non-Javadoc)
	 * @see com.rivescript.Client#getId()
	 */
	@Override
	public String getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#get(java.lang.String)
	 */
	@Override
	public String get (String name) {
		if (data.containsKey(name)) {
			return data.get(name);
		}
		return "undefined";
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#delete(java.lang.String)
	 */
	@Override
	public void delete (String name) {
		if (data.containsKey(name)) {
			data.remove(name);
		}
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#getData()
	 */
	@Override
	public HashMap<String, String> getData () {
		return data;
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#setData(java.util.HashMap)
	 */
	@Override
	public boolean setData (HashMap<String, String> newdata) {
		this.data = newdata;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#addInput(java.lang.String)
	 */
	@Override
	public void addInput (String text) {
		// Push this onto the front of the input array.
		input = unshift(input, text);
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#addReply(java.lang.String)
	 */
	@Override
	public void addReply (String text) {
		// Push this onto the front of the reply array.
		reply = unshift(reply, text);
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#getInput(int)
	 */
	@Override
	public String getInput (int index) throws java.lang.IndexOutOfBoundsException {
		if (index >= 1 && index <= 9) {
			return this.input[index-1];
		}
		else {
			throw new java.lang.IndexOutOfBoundsException();
		}
	}

	/* (non-Javadoc)
	 * @see com.rivescript.Client#getReply(int)
	 */
	@Override
	public String getReply (int index) throws java.lang.IndexOutOfBoundsException {
		if (index >= 1 && index <= 9) {
			return this.reply[index-1];
		}
		else {
			throw new java.lang.IndexOutOfBoundsException();
		}
	}

	/**
	 * Shift an item to the beginning of an array and rotate.
	 */
	public String[] unshift (String[] array, String addition) {
		// First rotate all entries from 0 to the end.
		for (int i = array.length - 1; i > 0; i--) {
			array[i] = array[i - 1];
		}

		// Now set the first item.
		array[0] = addition;
		return array;
	}
}
