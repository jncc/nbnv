package nbn.common.bridging;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 26-Nov-2010
* @description	    :- This interface should be implemented by those classes which
* Bridge type I to type O. The implementation of the convert method could return a
* standard construction of type O (as shown below)
*
* public O convert(I toConvert) {
*   return new O(toConvert.getA(),toConvert.getB());
* }
*
* Or it could return an extended version of O which adapts toConvert.
*
*   public O convert(I toConvert) {
*	return new AdaptedO(toConvert);
*   }
*
*   public class AdaptedO extends O {
*	private I toConvert;
*	public AdaptedO(I toConvert) {
*	    this.toConvert = toConvert;
*   }
*
*   public A getA() {
*	return toConvert.getA();
*   }
*
*   public B getB() {
*	return toConvert.getB();
*   }
*
* The choice between these to methods is dependant of the requirements of the
* application.
*
* It should be noted that the traditional bridge method will not reflect updates in the toConvert (I) object
* in the resultant object (O) which are made after the conversion. Whereas the Adapted version will.
*/
public interface Bridge<I,O> {
    /**
     * This method will take the parameter toConvert and convert it into an equivilant
     * object of type O.
     * @param toConvert
     * @return toConvert in the form of type O
     */
    public O convert(I toConvert) throws BridgingException;
}
