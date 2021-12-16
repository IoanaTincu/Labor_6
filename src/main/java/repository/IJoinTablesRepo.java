package repository;

import exceptions.InvalidCourseException;
import exceptions.InvalidStudentException;
import exceptions.InvalidTeacherException;
import exceptions.NullValueException;
import model.Course;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface IJoinTablesRepo {

    /**
     * enrolls a given student in a given course
     *
     * @param studentId the id of the student that will be enrolled in the course
     * @param courseId  the id of the course in which the student is being enrolled
     * @throws Exception will be thrown when the student is already enrolled in the course
     */
    void registerStudentToCourse(Long studentId, Long courseId) throws Exception;


    /**
     * returns true if the student is already enrolled in the course or false otherwise
     *
     * @param studentId the student for which we check enrollment
     * @param courseId  the course for which we check enrollment
     * @return true if the enrollment already exists or false otherwise
     * @throws NullValueException      if the parameter object is null
     * @throws SQLException            (getConnection) if a database access error occurs or the url is null
     * @throws IOException             (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException  (forName) if the class cannot be located
     * @throws InvalidStudentException will be thrown when the id of the student doesn't exist in studentRepository
     * @throws InvalidCourseException  will be thrown when the id of the course doesn't exist in courseRepository
     */
    boolean checkExistenceOfEnrollment(Long studentId, Long courseId) throws NullValueException, SQLException, IOException, ClassNotFoundException, InvalidStudentException, InvalidCourseException;


    /**
     * deletes the enrollment of a given student in a given course
     *
     * @param studentId the id of the student for which the enrollment will be deleted
     * @param courseId  the id of the course for which the enrollment will be deleted
     * @throws SQLException           (getConnection) if a database access error occurs or the url is null
     * @throws IOException            (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException (forName) if the class cannot be located
     */
    void deleteEnrollment(Long studentId, Long courseId) throws SQLException, IOException, ClassNotFoundException;


    /**
     * sets the teacher of a course
     *
     * @param teacherId the id of the teacher who teaches the course
     * @param courseId  the id of the course
     * @throws NullValueException      if the parameter object is null
     * @throws SQLException            (getConnection) if a database access error occurs or the url is null
     * @throws IOException             (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException  (forName) if the class cannot be located
     * @throws InvalidTeacherException will be thrown when the id of the teacher doesn't exist in studentRepository
     * @throws InvalidCourseException  will be thrown when the id of the course doesn't exist in courseRepository
     */
    void registerTeacherToCourse(Long teacherId, Long courseId) throws NullValueException, SQLException, IOException, ClassNotFoundException, InvalidTeacherException, InvalidCourseException;


    /**
     * returns the number of credits for a given course
     *
     * @param courseId the course for which credits are returned
     * @return the number of credits
     * @throws SQLException           (getConnection) if a database access error occurs or the url is null
     * @throws IOException            (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException (forName) if the class cannot be located
     */
    Long getCreditsOfCourse(Long courseId) throws SQLException, IOException, ClassNotFoundException;


    /**
     * returns the students who attend the course. The course is given as a parameter
     *
     * @param course for which we want the attending students
     * @return list with all students who attend the course
     * @throws SQLException           (getConnection) if a database access error occurs or the url is null
     * @throws IOException            (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException (forName) if the class cannot be located
     */
    List<Long> getStudentsEnrolledInCourse(Course course) throws SQLException, IOException, ClassNotFoundException;


    /**
     * deletes the enrollments of all students for a given course
     *
     * @param courseId the id of the course for which the enrollments will be deleted
     * @throws NullValueException     will be thrown when the received parameter is null
     * @throws SQLException           (getConnection) if a database access error occurs or the url is null
     * @throws IOException            (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException (forName) if the class cannot be located
     */
    void deleteEnrolledStudentsFromCourse(Long courseId) throws NullValueException, SQLException, IOException, ClassNotFoundException;


    /**
     * deletes the enrollments for a given student
     *
     * @param studentId the id of the student for which the enrollments will be deleted
     * @throws SQLException           (getConnection) if a database access error occurs or the url is null
     * @throws IOException            (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException (forName) if the class cannot be located
     */
    void deleteCoursesAttendedByStudent(Long studentId) throws SQLException, IOException, ClassNotFoundException;


    /**
     * sets the teacher id of a course to NULL before the teacher is deleted from the database
     *
     * @param teacherId the id of the student who will be deleted
     * @throws SQLException           (getConnection) if a database access error occurs or the url is null
     * @throws IOException            (load) if an error occurred when reading from the input stream
     * @throws ClassNotFoundException (forName) if the class cannot be located
     */
    void deleteTeacherFromCourse(Long teacherId) throws SQLException, IOException, ClassNotFoundException;

    boolean verifyTeacherTeachesCourse(Long teacherId, Long courseId) throws SQLException, IOException, ClassNotFoundException, NullValueException, InvalidCourseException;
}
