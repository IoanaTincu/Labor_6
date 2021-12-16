package repository;

import exceptions.InvalidCourseException;
import exceptions.InvalidStudentException;
import exceptions.InvalidTeacherException;
import exceptions.NullValueException;
import model.Course;
import model.Student;
import model.Teacher;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class EnrolledJdbcRepository implements IJoinTablesRepo {
    ICrudRepository<Student> studentJdbcRepo;
    ICrudRepository<Course> courseJdbcRepo;
    ICrudRepository<Teacher> teacherJdbcRepo;

    public EnrolledJdbcRepository(ICrudRepository<Student> studentJdbcRepo, ICrudRepository<Course> courseJdbcRepo, ICrudRepository<Teacher> teacherJdbcRepo) {
        this.studentJdbcRepo = studentJdbcRepo;
        this.courseJdbcRepo = courseJdbcRepo;
        this.teacherJdbcRepo = teacherJdbcRepo;
    }

    public void registerStudentToCourse(Long studentId, Long courseId) throws Exception {
        if (studentId == null || courseId == null)
            throw new NullValueException("Invalid parameter!");

        Connection connection = courseJdbcRepo.openConnection();

        if (studentJdbcRepo.findOne(studentId) == null)
            throw new InvalidStudentException("Invalid student!");
        if (courseJdbcRepo.findOne(courseId) == null)
            throw new InvalidCourseException("Invalid course!");
        if (checkExistenceOfEnrollment(studentId, courseId))
            throw new Exception("Already enrolled in course!");

        PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO studentsCourses VALUES (?, ?)"
        );
        statement.setLong(1, studentId);
        statement.setLong(2, courseId);

        PreparedStatement otherStatement = connection.prepareStatement(
                "UPDATE students " +
                        "SET totalCredits = totalCredits + ? " +
                        "WHERE id = ?"
        );
        otherStatement.setLong(1, getCreditsOfCourse(courseId));
        otherStatement.setLong(2, studentId);


        otherStatement.executeUpdate();
        otherStatement.close();
        statement.executeUpdate();
        statement.close();
        courseJdbcRepo.closeConnection(connection);
    }


    public boolean checkExistenceOfEnrollment(Long studentId, Long courseId) throws NullValueException, SQLException, IOException, ClassNotFoundException, InvalidStudentException, InvalidCourseException {
        if (studentId == null || courseId == null)
            throw new NullValueException("Invalid entity");

        Connection connection = courseJdbcRepo.openConnection();

        if (studentJdbcRepo.findOne(studentId) == null)
            throw new InvalidStudentException("Invalid student");
        if (courseJdbcRepo.findOne(courseId) == null)
            throw new InvalidCourseException("Invalid course");

        PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM studentsCourses WHERE (idStudent=? AND idCourse=?)"
        );
        statement.setLong(1, studentId);
        statement.setLong(2, courseId);

        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            courseJdbcRepo.closeConnection(connection);
            return false;
        }

        statement.close();
        resultSet.close();
        courseJdbcRepo.closeConnection(connection);
        return true;
    }


    public void deleteEnrollment(Long studentId, Long courseId) throws SQLException, IOException, ClassNotFoundException {
        Connection connection = courseJdbcRepo.openConnection();

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM studentsCourses WHERE idStudent = ? AND idCourse = ?"
        );
        statement.setLong(1, studentId);
        statement.setLong(2, courseId);
        statement.executeUpdate();

        statement.close();
        courseJdbcRepo.closeConnection(connection);
    }


    public void registerTeacherToCourse(Long teacherId, Long courseId) throws NullValueException, SQLException, IOException, ClassNotFoundException, InvalidTeacherException, InvalidCourseException {
        if (teacherId == null || courseId == null)
            throw new NullValueException("Invalid entity");

        Connection connection = courseJdbcRepo.openConnection();

        if (teacherJdbcRepo.findOne(teacherId) == null)
            throw new InvalidTeacherException("Invalid teacher");
        if (courseJdbcRepo.findOne(courseId) == null)
            throw new InvalidCourseException("Invalid course");

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE courses SET teacherId=? WHERE id=?"
        );
        statement.setLong(1, teacherId);
        statement.setLong(2, courseId);

        statement.executeUpdate();
        statement.close();
        courseJdbcRepo.closeConnection(connection);
    }


    public Long getCreditsOfCourse(Long courseId) throws SQLException, IOException, ClassNotFoundException {
        Connection connection = courseJdbcRepo.openConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT credits FROM courses WHERE id = ?"
        );
        statement.setLong(1, courseId);

        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            long credits = resultSet.getLong("credits");
            statement.close();
            resultSet.close();
            connection.close();
            return credits;
        }

        statement.close();
        resultSet.close();
        courseJdbcRepo.closeConnection(connection);
        return null;
    }


    public List<Long> getStudentsEnrolledInCourse(Course course) throws SQLException, IOException, ClassNotFoundException {
        List<Long> students = new ArrayList<>();

        Connection connection = courseJdbcRepo.openConnection();

        PreparedStatement statement = connection.prepareStatement(
                "SELECT idStudent " +
                        "FROM ((students S " +
                        "INNER JOIN studentsCourses Sc ON S.id=Sc.idStudent) " +
                        "INNER JOIN courses C ON Sc.idCourse=C.id)" +
                        "WHERE C.id=?");
        statement.setLong(1, course.getId());
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            long id = resultSet.getLong("idStudent");
            students.add(id);
        }

        statement.close();
        courseJdbcRepo.closeConnection(connection);
        return students;
    }


    public void deleteEnrolledStudentsFromCourse(Long courseId) throws NullValueException, SQLException, IOException, ClassNotFoundException {
        if (courseId == null)
            throw new NullValueException("Invalid entity");

        Connection connection = courseJdbcRepo.openConnection();

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM studentsCourses WHERE idCourse=?"
        );
        statement.setLong(1, courseId);
        statement.execute();

        statement.close();
        courseJdbcRepo.closeConnection(connection);
    }


    public void deleteCoursesAttendedByStudent(Long studentId) throws SQLException, IOException, ClassNotFoundException {
        Connection connection = studentJdbcRepo.openConnection();

        PreparedStatement statement = connection.prepareStatement(
                "DELETE FROM studentsCourses WHERE idStudent=?"
        );
        statement.setLong(1, studentId);
        statement.execute();

        statement.close();
        studentJdbcRepo.closeConnection(connection);
    }


    public void deleteTeacherFromCourse(Long teacherId) throws SQLException, IOException, ClassNotFoundException {
        Connection connection = teacherJdbcRepo.openConnection();

        PreparedStatement statement = connection.prepareStatement(
                "UPDATE courses SET teacherId=NULL WHERE teacherId=?"
        );
        statement.setLong(1, teacherId);
        statement.executeUpdate();

        statement.close();
        teacherJdbcRepo.closeConnection(connection);
    }

    public boolean verifyTeacherTeachesCourse(Long teacherId, Long courseId) throws SQLException, IOException, ClassNotFoundException, NullValueException, InvalidCourseException {
        if (teacherId == null || courseId == null)
            throw new NullValueException("Invalid parameter!");

        String sqlQuery = "SELECT teacherId FROM courses WHERE id=?";
        Connection connection = teacherJdbcRepo.openConnection();

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setLong(1, courseId);
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            teacherJdbcRepo.closeConnection(connection);
            throw new InvalidCourseException("Course does not exist in the database!");
        }

        if (resultSet.getLong("teacherId") == teacherId) {
            statement.close();
            resultSet.close();
            teacherJdbcRepo.closeConnection(connection);
            return true;
        }

        statement.close();
        resultSet.close();
        teacherJdbcRepo.closeConnection(connection);
        return false;
    }

}
