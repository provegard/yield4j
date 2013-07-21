package org.yield4j.meta;

import static java.util.Arrays.asList;

//>> [1, 2]
public class EnumerationSanityCheck {
	public Iterable<Integer> method() {
		return asList(1, 2);
	}
}
