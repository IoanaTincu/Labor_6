package exceptions;

/**
 * class representing custom exception. This exception will be thrown when the parameter of type Student contains a course that doesn't exist in courseRepository
 */
public class InvalidCourseException extends Exception {

    public InvalidCourseException(String str) { super(str); }
}
