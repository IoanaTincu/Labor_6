package com.company;

import controller.CourseController;
import controller.StudentController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Course;
import model.Student;
import model.Teacher;
import repository.*;

import java.io.IOException;
import java.sql.SQLException;

public class GuiStudent {

    private String firstName;
    private String lastName;

    public void setNames(String firstName, String lastname) {
        this.firstName = firstName;
        this.lastName = lastname;
    }

    ICrudRepository<Student> studentJdbcRepo;
    ICrudRepository<Teacher> teacherJdbcRepo;
    ICrudRepository<Course> courseJdbcRepo;

    IJoinTablesRepo enrolledJdbcRepo;
    private StudentController studentController;
    private CourseController courseController;


    VBox mainLayout;
    HBox hBoxButtons, hBoxListView;
    Button buttonCredits, buttonRegister;
    ListView<String> listView;
    GridPane labels;
    Label labelCourseName;
    TextField courseNameInput;


    public Parent initialize() {
        studentJdbcRepo = new StudentJdbcRepository();
        teacherJdbcRepo = new TeacherJdbcRepository();
        courseJdbcRepo = new CourseJdbcRepository();

        enrolledJdbcRepo = new EnrolledJdbcRepository(studentJdbcRepo, courseJdbcRepo, teacherJdbcRepo);
        studentController = new StudentController(studentJdbcRepo, courseJdbcRepo, enrolledJdbcRepo);
        courseController = new CourseController(courseJdbcRepo, teacherJdbcRepo, enrolledJdbcRepo);


        listView = new ListView<>();
        listView.setPrefWidth(530);

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
        buttonCredits = new Button();
        buttonCredits.setText("See your total credits");
        buttonCredits.setOnAction(e -> buttonCreditsClicked()); // Connect to action

        buttonRegister = new Button();
        buttonRegister.setText("Register for course");
        buttonRegister.setOnAction(e -> {
            try {
                buttonRegisterClicked();
            } catch (Exception ex) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                alert.setTitle("Message");
                alert.setHeaderText("");
                alert.setContentText(ex.toString());
                alert.showAndWait();
            }
        });

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
        hBoxButtons.getChildren().addAll(buttonRegister, buttonCredits);
        labels.add(labelCourseName, 0, 0);
        labels.add(courseNameInput, 1, 0);

        /*GridPane.setConstraints(hBoxButtons, 0, 0);
        GridPane.setConstraints(hBoxListView, 0, 5);*/

        // Center stuff
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxListView.setAlignment(Pos.CENTER);
        labels.setAlignment(Pos.CENTER);
        mainLayout.setAlignment(Pos.CENTER);

        mainLayout.getChildren().addAll(hBoxButtons, labels, hBoxListView);
        return mainLayout;
    }


    private void buttonCreditsClicked() {
        listView.getItems().clear();
        courseNameInput.clear();
        try {
            Integer credits = studentController.getTotalCreditsOfStudent(firstName, lastName);
            listView.getItems().add("Your total credits: " + credits);

        } catch (SQLException | IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText(e.toString());
            alert.showAndWait();
        }
    }

    private void buttonRegisterClicked() throws Exception {
        try {
            Long courseId = courseController.searchCourse(courseNameInput.getText());
            Long studentId = studentController.searchPerson(firstName, lastName);
            studentController.registerStudentToCourse(studentId, courseId);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText("You have been registered!");
            alert.showAndWait();

            courseNameInput.clear();
            listView.getItems().clear();

        } catch (SQLException | IOException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Message");
            alert.setHeaderText("");
            alert.setContentText(e.toString());
            alert.showAndWait();
            courseNameInput.clear();
        }
    }
}
