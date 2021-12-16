package repository;

import exceptions.NullValueException;
import model.Course;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseJdbcRepository extends JdbcRepository<Course> {

    @Override
    public List<Course> readDataFromDatabase(Connection connection) {
        List<Course> databaseCourses = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM courses");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                long teacherId = resultSet.getLong("teacherId");
                int maxEnrollment = resultSet.getInt("maxEnrollment");
                int credits = resultSet.getInt("credits");

                Course course = new Course(id, name, teacherId, maxEnrollment, credits);
                databaseCourses.add(course);
            }
            return databaseCourses;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databaseCourses;
    }

    @Override
    public Long searchPerson(String firstName, String lastName) {
        return null;
    }

    @Override
    public Integer getTotalCreditsOfStudent(String firstName, String lastName) throws SQLException, IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public Long searchCourse(String courseName) throws SQLException, IOException, ClassNotFoundException {
        String sqlQuery = "SELECT id FROM courses WHERE name=?";
        Connection connection = openConnection();

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, courseName);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Long courseId = resultSet.getLong("id");
            statement.close();
            resultSet.close();
            closeConnection(connection);
            return courseId;
        }

        statement.close();
        resultSet.close();
        closeConnection(connection);
        return null;
    }

    @Override
    public Course findOne(Long id) throws NullValueException, SQLException, IOException, ClassNotFoundException {
        if (id == null)
            throw new NullValueException("Invalid ID");

        String sqlQuery = "SELECT * FROM courses WHERE id=?";
        Connection connection = openConnection();

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();

        if (!resultSet.next()) {
            statement.close();
            resultSet.close();
            closeConnection(connection);
            return null;
        }

        long courseId = resultSet.getLong("id");
        String name = resultSet.getString("name");
        long teacherId = resultSet.getLong("teacherId");
        int maxEnrollment = resultSet.getInt("maxEnrollment");
        int credits = resultSet.getInt("credits");

        Course course = new Course(courseId, name, teacherId, maxEnrollment, credits);
        statement.close();
        resultSet.close();
        closeConnection(connection);
        return course;
    }

    @Override
    public Course save(Course entity) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (entity == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "INSERT INTO courses (id, name, teacherId, maxEnrollment, credits) VALUES (?, ?, ?, ?, ?)";
        Connection connection = openConnection();
        List<Course> databaseCourses = readDataFromDatabase(connection);

        for (Course course : databaseCourses)
            if (course.getId() == entity.getId()) {
                closeConnection(connection);
                return entity;
            }

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getName());
        statement.setLong(3, entity.getTeacherId());
        statement.setInt(4, entity.getMaxEnrollment());
        statement.setInt(5, entity.getCredits());

        statement.executeUpdate();
        statement.close();
        closeConnection(connection);
        return null;

    }

    @Override
    public Course delete(Long id) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (id == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "DELETE FROM courses WHERE id=?";
        Connection connection = openConnection();
        List<Course> databaseCourses = readDataFromDatabase(connection);

        for (Course course : databaseCourses)
            if (course.getId() == id) {
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setLong(1, id);

                statement.executeUpdate();
                statement.close();
                closeConnection(connection);
                return course;
            }

        closeConnection(connection);
        return null;
    }

    @Override
    public Course update(Course entity) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (entity == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "UPDATE courses SET name=?, teacherId=?, maxEnrollment=?, credits=? WHERE id=?";
        Connection connection = openConnection();
        List<Course> databaseCourses = readDataFromDatabase(connection);

        for (Course course : databaseCourses)
            if (course.getId() == entity.getId()) {
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setString(1, entity.getName());
                statement.setLong(2, entity.getTeacherId());
                statement.setInt(3, entity.getMaxEnrollment());
                statement.setInt(4, entity.getCredits());
                statement.setLong(5, entity.getId());

                statement.executeUpdate();
                statement.close();
                closeConnection(connection);
                return null;
            }

        closeConnection(connection);
        return entity;
    }

}
