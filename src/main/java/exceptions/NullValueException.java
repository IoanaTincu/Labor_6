package exceptions;

/**
 * class representing custom exception. This exception will be thrown when the received parameter is null
 */
public class NullValueException extends Exception {

    public NullValueException(String str) {
        super(str);
    }
}
