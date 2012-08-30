package nbn.webmapping.json.entity;

public class EntityResolvingException extends Exception {
    public EntityResolvingException() {
	super();
    }

    public EntityResolvingException(String mess) {
	super(mess);
    }

    public EntityResolvingException(String mess, Throwable cause) {
	super(mess,cause);
    }

    public EntityResolvingException(Throwable cause) {
	super(cause);
    }
}