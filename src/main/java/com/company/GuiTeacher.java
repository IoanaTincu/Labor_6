package com.company;

import controller.CourseController;
import controller.StudentController;
import controller.TeacherController;
import exceptions.InvalidCourseException;
import exceptions.NullValueException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Course;
import model.Student;
import model.Teacher;
import repository.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class GuiTeacher extends Application {

    private String firstName = "Luca";
    private String lastName = "Tompea";

    ICrudRepository<Student> studentJdbcRepo = new StudentJdbcRepository();
    ICrudRepository<Teacher> teacherJdbcRepo = new TeacherJdbcRepository();
    ICrudRepository<Course> courseJdbcRepo = new CourseJdbcRepository();

    IJoinTablesRepo enrolledJdbcRepo = new EnrolledJdbcRepository(studentJdbcRepo, courseJdbcRepo, teacherJdbcRepo);
    private CourseController courseController = new CourseController(courseJdbcRepo, teacherJdbcRepo, enrolledJdbcRepo);
    private StudentController studentController = new StudentController(studentJdbcRepo, courseJdbcRepo, enrolledJdbcRepo);
    private TeacherController teacherController = new TeacherController(teacherJdbcRepo, enrolledJdbcRepo);


    VBox mainLayout;
    HBox hBoxButtons, hBoxListView;
    Button buttonShowStudents, buttonRefresh;
    ListView<String> listView;
    GridPane labels;
    Label labelCourseName;
    TextField courseNameInput;


    /*public GuiTeacher() {
        ICrudRepository<Student> studentJdbcRepo = new StudentJdbcRepository();
        ICrudRepository<Teacher> teacherJdbcRepo = new TeacherJdbcRepository();
        ICrudRepository<Course> courseJdbcRepo = new CourseJdbcRepository();

        IJoinTablesRepo enrolledJdbcRepo = new EnrolledJdbcRepository(studentJdbcRepo, courseJdbcRepo, teacherJdbcRepo);
        this.courseController = new CourseController(courseJdbcRepo, teacherJdbcRepo, enrolledJdbcRepo);
        this.studentController = new StudentController(studentJdbcRepo, courseJdbcRepo, enrolledJdbcRepo);
    }*/


    public void launchGuiTeacher(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        listView = new ListView<>();
        listView.setPrefWidth(530);

        // Create window
        primaryStage.setTitle("Teacher Menu");

        // Create VBox
        mainLayout = new VBox();
        mainLayout.setPrefWidth(300);
        mainLayout.setSpacing(70);
        mainLayout.setPadding(new Insets(0, 0, 0, 0));

        // Create HBox for Buttons
        hBoxButtons = new HBox();
        hBoxButtons.setPrefWidth(300);
        hBoxButtons.setSpacing(48); //the spacing of the elements
        hBoxButtons.setPadding(new Insets(0, 0, 0, 0));

        // Create HBox for List View
        hBoxListView = new HBox();
        hBoxListView.setPrefWidth(672);
        hBoxListView.setSpacing(15); //the spacing of the elements
        hBoxListView.setPadding(new Insets(0, 0, 0, 0));

        // Create buttons
        buttonShowStudents = new Button();
        buttonShowStudents.setText("See students enrolled");
        buttonShowStudents.setOnAction(e -> buttonShowStudentsClicked()); // Connect to action

        buttonRefresh = new Button();
        buttonRefresh.setText("Refresh");
        buttonRefresh.setOnAction(e -> buttonShowStudentsClicked());

        // Create GridPane for labels
        labels = new GridPane();
        labels.setPadding(new Insets(10, 10, 10, 10));
        labels.setVgap(8);
        labels.setHgap(15);

        // Create labels
        labelCourseName = new Label("Course name:");
        courseNameInput = new TextField(); //create a blank input
        courseNameInput.setPromptText("course name");

        // Add elements to GUI
        hBoxListView.getChildren().add(listView);
        hBoxButtons.getChildren().addAll(buttonShowStudents, buttonRefresh);
        labels.add(labelCourseName, 0, 0);
        labels.add(courseNameInput, 1, 0);

        /*GridPane.setConstraints(hBoxButtons, 0, 0);
        GridPane.setConstraints(hBoxListView, 0, 5);*/

        // Center stuff
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxListView.setAlignment(Pos.CENTER);
        labels.setAlignment(Pos.CENTER);
        mainLayout.setAlignment(Pos.CENTER);

        // Show window
        mainLayout.getChildren().addAll(hBoxButtons, labels, hBoxListView);
        Scene scene = new Scene(mainLayout, 672, 672);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void buttonShowStudentsClicked() {
        try {
            listView.getItems().clear();
            Long courseId = courseController.searchCourse(courseNameInput.getText());
            Long teacherId = teacherController.searchPerson(firstName, lastName);

            if (!teacherController.verifyTeacherTeachesCourse(teacherId, courseId))
                throw new InvalidCourseException("Invalid course!");

            Course course = new Course(courseId, courseNameInput.getText(), -1, -1, -1);
            List<Long> studentIds = courseController.getStudentsEnrolledInCourse(course);

            if (studentIds.size() == 0)
                listView.getItems().add("No students enrolled!");
            else
                for (Long id : studentIds)
                    listView.getItems().add(studentController.findOne(id).toString());

            courseNameInput.clear();

        } catch (SQLException | IOException | ClassNotFoundException | NullValueException | InvalidCourseException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText(e.toString());
            alert.showAndWait();
            courseNameInput.clear();
        }

    }
}
