package exceptions;

/**
 * class representing custom exception. This exception will be thrown when the object of type Course contains an enrolled student who doesn't exist in studentRepository
 */
public class InvalidStudentException extends Exception{

    public InvalidStudentException(String str) { super(str); }
}
