package com.iaskdata.test;

import water.DKV;
import water.H2O;
import water.H2OApp;
import water.Iced;
import water.Key;

public class H2OJavaDroplet {

	public static final String MSG = "Hello %s!";

	/** Simple Iced-object which will be serialized over network */
	public static final class StringHolder extends Iced {
		final String msg;

		public StringHolder(String msg) {
			this.msg = msg;
		}

		public String hello(String name) {
			return String.format(msg, name);

		}
	}

	/**
	 * Creates a key and value holding a simple message in
	 * {@link water.droplets.H2OJavaDroplet.StringHolder}.
	 * 
	 * @return key referencing stored value.
	 */
	public static final Key hello() {
		Key vkey = Key.make("hello.key");
		StringHolder value = new StringHolder(MSG);
		DKV.put(vkey, value);

		return vkey;
	}

	/** Application Entry Point */
	public static void main(String[] args) {
		// Run H2O and build a cloud of 1 member
		H2OApp.main(args);
		H2O.waitForCloudSize(1, 10 * 1000 /* ms */);
	}

}
