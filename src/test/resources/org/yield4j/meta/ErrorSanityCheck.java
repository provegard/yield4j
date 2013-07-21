package org.yield4j.meta;

//!! ERROR(6) not a statement
public class ErrorSanityCheck {
	public Iterable<Integer> method() {
		gork;
	}
}
