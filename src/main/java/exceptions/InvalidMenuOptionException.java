package exceptions;

/**
 * class representing custom exception. This exception will be thrown when the menu option doesn't exist among the options
 */
public class InvalidMenuOptionException extends Exception {

    public InvalidMenuOptionException(String str) { super(str); }
}
