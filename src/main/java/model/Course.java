package model;

import java.util.List;
import java.util.Objects;

public class Course {

    private long id;
    private String name;
    private long teacherId;
    private int maxEnrollment;
    private int credits;

    public Course(long id, String name, long teacherId, int maxEnrollment, int credits) {
        this.id = id;
        this.name = name;
        this.teacherId = teacherId;
        this.maxEnrollment = maxEnrollment;
        this.credits = credits;
    }

    public Course() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public int getMaxEnrollment() {
        return maxEnrollment;
    }

    public void setMaxEnrollment(int maxEnrollment) {
        this.maxEnrollment = maxEnrollment;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", courseId=" + id +
                ", teacher=" + teacherId +
                ", maxEnrollment=" + maxEnrollment +
                ", credits=" + credits +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id && maxEnrollment == course.maxEnrollment && credits == course.credits && Objects.equals(name, course.name) && Objects.equals(teacherId, course.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, teacherId, maxEnrollment, credits);
    }
}
