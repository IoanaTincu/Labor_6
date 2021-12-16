package controller;

import com.sun.org.glassfish.gmbal.Description;
import exceptions.NullValueException;
import model.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.EnrolledJdbcRepository;
import repository.TeacherJdbcRepository;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class TeacherControllerTest {

    TeacherController teacherController;
    TeacherJdbcRepository teacherJdbcRepo = Mockito.mock(TeacherJdbcRepository.class);
    EnrolledJdbcRepository enrolledJdbcRepo = Mockito.mock(EnrolledJdbcRepository.class);

    Teacher teacher1 = new Teacher(1200, "Mugur", "Acu");
    Teacher teacher2 = new Teacher(1216, "Matei", "Stroia");
    Teacher teacher3 = new Teacher(113, "Luca", "Tompea");

    Connection connection;

    @BeforeEach
    void setUp() throws SQLException, IOException, ClassNotFoundException, NullValueException {
        teacherController = new TeacherController(teacherJdbcRepo, enrolledJdbcRepo);
        Mockito.when(teacherJdbcRepo.openConnection()).thenReturn(connection);
        Mockito.when(teacherJdbcRepo.save(teacher1)).thenReturn(teacher1);
        Mockito.when(teacherJdbcRepo.save(teacher3)).thenReturn(null);
        Mockito.when(teacherJdbcRepo.delete(null)).thenThrow(NullValueException.class);
        Mockito.when(teacherJdbcRepo.delete(1217L)).thenReturn(null);
        Mockito.when(teacherJdbcRepo.findOne(1216L)).thenReturn(teacher2);
        Mockito.when(teacherJdbcRepo.delete(1216L)).thenReturn(teacher2);
    }

    @Test
    @Description("Should return the teacher because the id already exists in the repository")
    void save_teacher_id_exists() throws SQLException, IOException, ClassNotFoundException, NullValueException {
        assertEquals(teacherController.save(teacher1), teacher1);
    }

    @Test
    @Description("Should return null because the teacher has been saved")
    void save_teacher_not_exists() throws SQLException, IOException, ClassNotFoundException, NullValueException {
        assertNull(teacherController.save(teacher3));
    }

    @Test
    @Description("Should throw a NullValueException because the parameter teacher is null")
    void delete_teacher_null() {
        assertThrows(NullValueException.class, () -> teacherController.delete(null));
    }

    @Test
    @Description("Should return null because there is no teacher in the repository with the specified id")
    void delete_teacher_not_exists() throws IOException, NullValueException, SQLException, ClassNotFoundException {
        assertNull(teacherController.delete(1217L));
    }

    @Test
    @Description("Should return the teacher because it has been removed from the repoList")
    void delete_student_exists() throws SQLException, IOException, ClassNotFoundException, NullValueException {
        Teacher teacher = teacherController.findOne(1216L);
        assertEquals(teacherController.delete(1216L), teacher);
    }
}