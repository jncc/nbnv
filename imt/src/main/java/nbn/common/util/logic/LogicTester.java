package nbn.common.util.logic;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 16-Sep-2010
* @description	    :- This is a Logic tester which is to be used when it is
* appropriate to test logic and if something is wrong an exception can be raised
*/
public class LogicTester {
    public static void performLogicTest(boolean testResult) {
	if(!testResult)
	    throw new LogicalException("A logical test has failed");
    }
}
