package nbn.common.util;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 20-Sep-2010
* @description	    :- A generic class which represents a linked Pair of objects
*/
public class Pair<A,B> {
    private A a;
    private B b;

    public Pair(A a, B b) {
	this.a = a;
	this.b = b;
    }

    public A getA() {
	return a;
    }

    public B getB() {
	return b;
    }
}
