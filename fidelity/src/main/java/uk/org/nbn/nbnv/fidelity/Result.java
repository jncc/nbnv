package uk.org.nbn.nbnv.fidelity;

/**
* Represents a result of a validation or verification check.
*/
public interface Result {
    public ResultLevel level();
    public String reference();
    public String message();
}
