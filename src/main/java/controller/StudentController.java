package controller;

import exceptions.InvalidCourseException;
import exceptions.NullValueException;
import model.Course;
import model.Student;
import repository.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class StudentController {

    private final ICrudRepository<Student> studentJdbcRepo;
    private final ICrudRepository<Course> courseJdbcRepo;
    private final IJoinTablesRepo enrolledJdbcRepo;

    public StudentController(ICrudRepository<Student> studentJdbcRepo, ICrudRepository<Course> courseJdbcRepo, IJoinTablesRepo enrolledJdbcRepo) {
        this.studentJdbcRepo = studentJdbcRepo;
        this.courseJdbcRepo = courseJdbcRepo;
        this.enrolledJdbcRepo = enrolledJdbcRepo;
    }

    /**
     * sorts students descending by the number of total credits
     *
     * @return sorted list of students
     */
    public List<Student> sortStudentsByTotalCredits() throws SQLException, IOException, ClassNotFoundException {
        Connection connection = studentJdbcRepo.openConnection();

        List<Student> students = studentJdbcRepo.readDataFromDatabase(connection).stream()
                //.sorted((student, otherStudent) -> otherStudent.getTotalCredits() - student.getTotalCredits())
                .sorted(Comparator.comparingInt(Student::getTotalCredits).reversed())
                .collect(Collectors.toList());

        studentJdbcRepo.closeConnection(connection);
        return students;
    }

    /**
     * filters the students who attend the course with the given id
     *
     * @param courseId id of the course
     * @return list of students who attend the course
     */
    public List<Student> filterStudentsAttendingCourse(Long courseId) throws SQLException, IOException, ClassNotFoundException, NullValueException {
        Connection connection = studentJdbcRepo.openConnection();
        Course course = courseJdbcRepo.findOne(courseId);

        List<Student> students = studentJdbcRepo.readDataFromDatabase(connection).stream()
                .filter(student -> {
                    try {
                        return enrolledJdbcRepo.getStudentsEnrolledInCourse(course).contains(student.getId());
                    } catch (SQLException | IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                // .filter(student -> {
                //    for (Long id : student.getEnrolledCourses()) if (id == courseId) return true;
                //    return false;
                // })
                .collect(Collectors.toList());

        studentJdbcRepo.closeConnection(connection);
        return students;
    }

    public Student findOne(Long id) throws NullValueException, SQLException, IOException, ClassNotFoundException {
        return studentJdbcRepo.findOne(id);
    }

    public List<Student> findAll() throws SQLException, IOException, ClassNotFoundException {
        return studentJdbcRepo.findAll();
    }

    /**
     * saves the parameter object in repoList. Returns the result of method save in PersonRepository<T>
     *
     * @param student to be saved
     * @return result of method save in PersonRepository<T>
     * @throws NullValueException     if the parameter object is null
     * @throws IOException            if the file is invalid
     * @throws InvalidCourseException student has a course in courseList that doesn't exist in courseRepoList
     */
    public Student save(Student student) throws NullValueException, IOException, InvalidCourseException, SQLException, ClassNotFoundException {
        if (student == null)
            throw new NullValueException("Invalid entity");

        return studentJdbcRepo.save(student);
    }

    /**
     * deletes the object with the parameter id from repoList. Returns the result of method delete in PersonRepository<T>
     * <p>
     * //@param id of the object to be deleted
     *
     * @return result of method delete in PersonRepository<T>
     * @throws IOException        if the file is invalid
     * @throws NullValueException if the parameter object is null
     */
    public Student delete(Long id) throws IOException, NullValueException, SQLException, ClassNotFoundException {
        if (id == null)
            throw new NullValueException("Invalid entity");

        Student result = studentJdbcRepo.findOne(id);
        if (result == null)
            return null;

        enrolledJdbcRepo.deleteCoursesAttendedByStudent(id);
        studentJdbcRepo.delete(id);
        return result;
    }

    public Student update(Student student) throws IOException, NullValueException, SQLException, ClassNotFoundException {
        return studentJdbcRepo.update(student);
    }

    public int size() throws SQLException, IOException, ClassNotFoundException {
        return studentJdbcRepo.size();
    }

    public Long searchPerson(String firstName, String lastName) throws SQLException, IOException, ClassNotFoundException {
        return studentJdbcRepo.searchPerson(firstName, lastName);
    }

    public Integer getTotalCreditsOfStudent(String firstName, String lastName) throws SQLException, IOException, ClassNotFoundException {
        return studentJdbcRepo.getTotalCreditsOfStudent(firstName, lastName);
    }

    public void registerStudentToCourse(Long studentId, Long courseId) throws Exception {
        enrolledJdbcRepo.registerStudentToCourse(studentId, courseId);
    }
}
