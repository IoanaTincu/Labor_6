package repository;

import exceptions.InvalidCourseException;
import exceptions.NullValueException;
import model.Teacher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherJdbcRepository extends JdbcRepository<Teacher> {

    @Override
    public List<Teacher> readDataFromDatabase(Connection connection) {
        List<Teacher> databaseTeachers = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM teachers");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");

                Teacher teacher = new Teacher(id, firstName, lastName);
                databaseTeachers.add(teacher);
            }
            return databaseTeachers;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databaseTeachers;
    }

    @Override
    public Long searchPerson(String firstName, String lastName) throws SQLException, IOException, ClassNotFoundException {
        String sqlQuery = "SELECT id FROM teachers WHERE firstName=? AND lastName=?";
        Connection connection = openConnection();

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Long teacherId = resultSet.getLong("id");
            statement.close();
            resultSet.close();
            closeConnection(connection);
            return teacherId;
        }

        statement.close();
        resultSet.close();
        closeConnection(connection);
        return null;
    }

    @Override
    public Integer getTotalCreditsOfStudent(String firstName, String lastName) {
        return null;
    }

    @Override
    public Long searchCourse(String courseName) {
        return null;
    }


    @Override
    public Teacher findOne(Long id) throws NullValueException, SQLException, IOException, ClassNotFoundException {
        if (id == null)
            throw new NullValueException("Invalid ID");

        String sqlQuery = "SELECT * FROM teachers WHERE id=?";
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

        long teacherId = resultSet.getLong("id");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");

        Teacher teacher = new Teacher(teacherId, firstName, lastName);
        statement.close();
        resultSet.close();
        closeConnection(connection);
        return teacher;
    }

    @Override
    public Teacher save(Teacher entity) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (entity == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "INSERT INTO teachers (id, firstName, lastName) VALUES (?, ?, ?)";
        Connection connection = openConnection();
        List<Teacher> databaseTeachers = readDataFromDatabase(connection);

        for (Teacher teacher : databaseTeachers)
            if (teacher.getId() == entity.getId()) {
                closeConnection(connection);
                return entity;
            }

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());

        statement.executeUpdate();
        statement.close();
        closeConnection(connection);
        return null;

    }

    @Override
    public Teacher delete(Long id) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (id == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "DELETE FROM teachers WHERE id=?";
        Connection connection = openConnection();
        List<Teacher> databaseTeachers = readDataFromDatabase(connection);

        for (Teacher teacher : databaseTeachers)
            if (teacher.getId() == id) {
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setLong(1, id);

                statement.executeUpdate();
                statement.close();
                closeConnection(connection);
                return teacher;
            }

        closeConnection(connection);
        return null;
    }

    @Override
    public Teacher update(Teacher entity) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (entity == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "UPDATE teachers SET firstName=?, lastName=? WHERE id=?";
        Connection connection = openConnection();
        List<Teacher> databaseTeachers = readDataFromDatabase(connection);

        for (Teacher teacher : databaseTeachers)
            if (teacher.getId() == entity.getId()) {
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.setLong(3, entity.getId());

                statement.executeUpdate();
                statement.close();
                closeConnection(connection);
                return null;
            }

        closeConnection(connection);
        return entity;
    }

}
