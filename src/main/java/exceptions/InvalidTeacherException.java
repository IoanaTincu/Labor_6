package exceptions;

/**
 * class representing custom exception. This exception will be thrown when the object of type Course contains a teacher who doesn't exist in teacherRepository
 */
public class InvalidTeacherException extends Exception {

    public InvalidTeacherException(String str) { super(str); }
}
