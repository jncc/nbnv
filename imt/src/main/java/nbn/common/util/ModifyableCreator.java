package nbn.common.util;

/**
*
* @author	    :- Christopher Johnson
* @date		    :- 03-Sep-2010
* @description	    :- This interface defines a generic clone operation.
*
* It enforces the return value of the clone operation to return the same type as is specified to be cloned
*
* It is expected that all underlying elements are cloned so that if the return value of the clone operation is modified,
* it will not have any effect on the parameter toClone
*/
public interface ModifyableCreator<T extends ModifyableCreator<T>> {
    public T getModifiable();
}
