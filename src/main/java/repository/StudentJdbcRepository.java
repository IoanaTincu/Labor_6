package repository;

import exceptions.NullValueException;
import model.Student;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentJdbcRepository extends JdbcRepository<Student> {

    @Override
    public List<Student> readDataFromDatabase(Connection connection) {
        List<Student> databaseStudents = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM students");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                int totalCredits = resultSet.getInt("totalCredits");

                Student student = new Student(id, firstName, lastName, totalCredits);
                databaseStudents.add(student);
            }
            return databaseStudents;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return databaseStudents;
    }

    @Override
    public Student findOne(Long id) throws NullValueException, SQLException, IOException, ClassNotFoundException {
        if (id == null)
            throw new NullValueException("Invalid ID");

        String sqlQuery = "SELECT * FROM students WHERE id=?";
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

        long studentId = resultSet.getLong("id");
        String firstName = resultSet.getString("firstName");
        String lastName = resultSet.getString("lastName");
        int totalCredits = resultSet.getInt("totalCredits");

        Student student = new Student(studentId, firstName, lastName, totalCredits);
        statement.close();
        resultSet.close();
        closeConnection(connection);
        return student;
    }

    @Override
    public Student save(Student entity) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (entity == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "INSERT INTO students (id, firstName, lastName, totalCredits) VALUES (?, ?, ?, ?)";
        Connection connection = openConnection();
        List<Student> databaseStudents = readDataFromDatabase(connection);

        for (Student student : databaseStudents)
            if (student.getId() == entity.getId()) {
                closeConnection(connection);
                return entity;
            }

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setLong(1, entity.getId());
        statement.setString(2, entity.getFirstName());
        statement.setString(3, entity.getLastName());
        statement.setInt(4, entity.getTotalCredits());

        statement.executeUpdate();
        statement.close();
        closeConnection(connection);
        return null;

    }

    @Override
    public Student delete(Long id) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (id == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "DELETE FROM students WHERE id=?";
        Connection connection = openConnection();
        List<Student> databaseStudents = readDataFromDatabase(connection);

        for (Student student : databaseStudents)
            if (student.getId() == id) {
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setLong(1, id);

                statement.executeUpdate();
                statement.close();
                closeConnection(connection);
                return student;
            }

        closeConnection(connection);
        return null;
    }

    @Override
    public Student update(Student entity) throws NullValueException, IOException, SQLException, ClassNotFoundException {
        if (entity == null)
            throw new NullValueException("Invalid entity");

        String sqlQuery = "UPDATE students SET firstName=?, lastName=?, totalCredits=? WHERE id=?";
        Connection connection = openConnection();
        List<Student> databaseStudents = readDataFromDatabase(connection);

        for (Student student : databaseStudents)
            if (student.getId() == entity.getId()) {
                PreparedStatement statement = connection.prepareStatement(sqlQuery);
                statement.setString(1, entity.getFirstName());
                statement.setString(2, entity.getLastName());
                statement.setInt(3, entity.getTotalCredits());

                statement.executeUpdate();
                statement.close();
                closeConnection(connection);
                return null;
            }

        closeConnection(connection);
        return entity;
    }


    public Long searchPerson(String firstName, String lastName) throws SQLException, IOException, ClassNotFoundException {
        String sqlQuery = "SELECT id FROM students WHERE firstName=? AND lastName=?";
        Connection connection = openConnection();

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Long studentId = resultSet.getLong("id");
            statement.close();
            resultSet.close();
            closeConnection(connection);
            return studentId;
        }

        statement.close();
        resultSet.close();
        closeConnection(connection);
        return null;
    }

    public Integer getTotalCreditsOfStudent(String firstName, String lastName) throws SQLException, IOException, ClassNotFoundException {
        String sqlQuery = "SELECT totalCredits FROM students WHERE firstName=? AND lastName=?";
        Connection connection = openConnection();

        PreparedStatement statement = connection.prepareStatement(sqlQuery);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            Integer totalCredits = resultSet.getInt("totalCredits");
            statement.close();
            resultSet.close();
            closeConnection(connection);
            return totalCredits;
        }

        statement.close();
        resultSet.close();
        closeConnection(connection);
        return null;
    }

    @Override
    public Long searchCourse(String courseName) {
        return null;
    }
}
