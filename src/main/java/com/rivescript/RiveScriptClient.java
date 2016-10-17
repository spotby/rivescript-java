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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A RiveScript interpreter written in Java.<p>
 *
 * SYNOPSIS<p>
 *
 * import com.rivescript.RiveScript;<p>
 *
 * // Create a new interpreter<br>
 * RiveScript rs = new RiveScript();<p>
 *
 * // Load a directory full of replies in *.rive files<br>
 * rs.loadDirectory("./replies");<p>
 *
 * // Sort replies<br>
 * rs.sortReplies();<p>
 *
 * // Get a reply for the user<br>
 * String reply = rs.reply("user", "Hello bot!");
 */

public class RiveScriptClient {
	
	private RiveScriptEngine engine = new RiveScriptEngine();
	
	private String         error = "";           // Last error text
	private boolean debug = false;
	
	// Bot's users' data structure.
	private com.rivescript.ClientManager clients = new com.rivescript.ClientManager();


	// The current user ID when reply() is called.
	private String currentUser = null;

	/*-------------------------*/
	/*-- Constructor Methods --*/
	/*-------------------------*/

	/**
	 * Create a new RiveScript interpreter object, specifying the debug mode.
	 *
	 * @param debug Enable debug mode (a *lot* of stuff is printed to the terminal)
	 */
	public RiveScriptClient (boolean debug) {
		// set debug modes
		this.debug = debug;
		engine.setDebug(debug);
		com.rivescript.Topic.setDebug(debug);

		// Set the default Java macro handler.
		
		//engine.setHandler("java", new com.rivescript.lang.Java(this));
	}

	/**
	 * Create a new RiveScript interpreter object.
	 */
	public RiveScriptClient () {
		this(false);
	}

	/*-------------------*/
	/*-- Error Methods --*/
	/*-------------------*/

	/**
	 * Return the text of the last error message given.
	 */
	public String lastError () {
		return this.error;
	}

	/**
	 * Set the error message.
	 *
	 * @param message The new error message to set.
	 */
	protected void error(String message) {
		this.error = message;
	}


	/**
	 * Set a variable for one of the bot's users. A null value will delete a
	 * variable.
	 *
	 * @param user  The user's ID.
	 * @param name  The name of the variable to set.
	 * @param value The value to set.
	 */

	public boolean setUservar (String user, String name, String value) {
		if (value == null || value.equals("<undef>") ) {
			clients.client(user).delete(name);
		}
		else {
			clients.client(user).set(name, value);
		}

		return true;
	}
	
	

	

	/**
	 * Set -all- user vars for a user. This will replace the internal hash for
	 * the user. So your hash should at least contain a key/value pair for the
	 * user's current "topic". This could be useful if you used getUservars to
	 * store their entire profile somewhere and want to restore it later.
	 *
	 * @param user  The user's ID.
	 * @param data  The full hash of the user's data.
	 */
	public boolean setUservars (String user, HashMap<String, String> data) {
		// TODO: this should be handled more sanely. ;)
		clients.client(user).setData(data);
		return true;
	}

	/**
	 * Get a list of all the user IDs the bot knows about.
	 */
	public String[] getUsers () {
		// Get the user list from the clients object.
		return clients.listClients();
	}

	/**
	 * Retrieve a listing of all the uservars for a user as a HashMap.
	 * Returns null if the user doesn't exist.
	 *
	 * @param user The user ID to get the vars for.
	 */
	public Map<String, String> getUservars (String user) {
		if (clients.clientExists(user)) {
			return clients.client(user).getData();
		}
		else {
			return null;
		}
	}

	/**
	 * Retrieve a single variable from a user's profile.
	 *
	 * Returns null if the user doesn't exist. Returns the string "undefined"
	 * if the variable doesn't exist.
	 *
	 * @param user The user ID to get data from.
	 * @param name The name of the variable to get.
	 */
	public String getUservar (String user, String name) {
		if (clients.clientExists(user)) {
			return clients.client(user).get(name);
		}
		else {
			return null;
		}
	}

	/**
	 * Get the current user's ID from within an object macro.
	 *
	 * This is useful within a (Java) object macro to get the ID of the user
	 * currently executing the macro (for example, to get/set variables for
	 * them).
	 *
	 * This function is only available during a reply context; outside of
	 * that it will return null.
	 *
	 * @return string user ID or null.
	 */
	public String currentUser () {
		return this.currentUser;
	}

	/**
	 * Return the last trigger that the user matched.
	 */
	public String lastMatch (String user) {
		return this.getUservar(user, "__lastmatch__");
	}



	/*---------------------*/
	/*-- Reply Methods   --*/
	/*---------------------*/

	/**
	 * Get a reply from the RiveScript interpreter.
	 *
	 * @param username A unique user ID for the user chatting with the bot.
	 * @param message  The user's message to the bot.
	 */
	public String reply (String username, String message) {
		String reply = engine.reply(clients.client(username), message);
		
		// Save their chat history.
		clients.client(username).addInput(message);
		clients.client(username).addReply(reply);

		// Clear the current user.
		this.currentUser = null;
		
		return reply;
	}
				
				
	
	/*-----------------------*/
	/*-- Developer Methods --*/
	/*-----------------------*/

	/**
	 * DEVELOPER: Dump the trigger sort buffers to the terminal.
	 */
	public void dumpSorted() {
		String[] topics = engine.getTopics().listTopics();
		for (int t = 0; t < topics.length; t++) {
			String topic = topics[t];
			String[] triggers = engine.getTopics().topic(topic).listTriggers();

			// Dump.
			println("Topic: " + topic);
			for (int i = 0; i < triggers.length; i++) {
				println("       " + triggers[i]);
			}
		}
	}

	/**
	 * DEVELOPER: Dump the entire topic/trigger/reply structure to the terminal.
	 */
	public void dumpTopics () {
		// Dump the topic list.
		println("{");
		String[] topicList = engine.getTopics().listTopics();
		for (int t = 0; t < topicList.length; t++) {
			String topic = topicList[t];
			String extra = "";

			// Includes? Inherits?
			String[] includes = engine.getTopics().topic(topic).includes();
			String[] inherits = engine.getTopics().topic(topic).inherits();
			if (includes.length > 0) {
				extra = "includes ";
				for (int i = 0; i < includes.length; i++) {
					extra += includes[i] + " ";
				}
			}
			if (inherits.length > 0) {
				extra += "inherits ";
				for (int i = 0; i < inherits.length; i++) {
					extra += inherits[i] + " ";
				}
			}
			println("  '" + topic + "' " + extra + " => {");

			// Dump the trigger list.
			String[] trigList = engine.getTopics().topic(topic).listTriggers();
			for (int i = 0; i < trigList.length; i++) {
				String trig = trigList[i];
				println("    '" + trig + "' => {");

				// Dump the replies.
				String[] reply = engine.getTopics().topic(topic).trigger(trig).listReplies();
				if (reply.length > 0) {
					println("      'reply' => [");
					for (int r = 0; r < reply.length; r++) {
						println("        '" + reply[r] + "',");
					}
					println("      ],");
				}

				// Dump the conditions.
				String[] cond = engine.getTopics().topic(topic).trigger(trig).listConditions();
				if (cond.length > 0) {
					println("      'condition' => [");
					for (int r = 0; r < cond.length; r++) {
						println("        '" + cond[r] + "',");
					}
					println("      ],");
				}

				// Dump the redirects.
				String[] red = engine.getTopics().topic(topic).trigger(trig).listRedirects();
				if (red.length > 0) {
					println("      'redirect' => [");
					for (int r = 0; r < red.length; r++) {
						println("        '" + red[r] + "',");
					}
					println("      ],");
				}

				println("    },");
			}

			println("  },");
		}
	}

	/*-------------------*/
	/*-- Debug Methods --*/
	/*-------------------*/

	protected void println (String line) {
		System.out.println(line);
	}

	/**
	 * Print a line of debug text to the terminal.
	 *
	 * @param line The line of text to print.
	 */
	protected void say (String line) {
		if (this.debug) {
			System.out.println("[RS] " + line);
		}
	}

	/**
	 * Print a line of warning text to the terminal.
	 *
	 * @param line The line of warning text.
	 */
	protected void cry (String line) {
		System.out.println("<RS> " + line);
	}

	/**
	 * Print a line of warning text including a file name and line number.
	 *
	 * @param text The warning text.
	 * @param file The file name.
	 * @param line The line number.
	 */
	protected void cry (String text, String file, int line) {
		System.out.println("<RS> " + text + " at " + file + " line " + line + ".");
	}

	/**
	 * Print a stack trace to the terminal when debug mode is on.
	 *
	 * @param e The IOException object.
	 */
	protected void trace (IOException e) {
		if (this.debug) {
			e.printStackTrace();
		}
	}

	public RiveScriptEngine getEngine() {
		return engine;
	}

	public void setEngine(RiveScriptEngine engine) {
		this.engine = engine;
	}
}
