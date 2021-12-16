package model;

import java.util.List;
import java.util.Objects;

public class Student extends Person {

    private int totalCredits;

    public Student(long id, String firstName, String lastName, int totalCredits) {
        super(id, firstName, lastName);
        this.totalCredits = totalCredits;
    }

    public Student() {
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(int totalCredits) {
        this.totalCredits = totalCredits;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", totalCredits=" + totalCredits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return totalCredits == student.totalCredits;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), totalCredits);
    }
}
