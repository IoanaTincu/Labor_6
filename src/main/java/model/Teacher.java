package model;

import java.util.List;
import java.util.Objects;

public class Teacher extends Person {

    public Teacher(long id, String firstName, String lastName) {
        super(id, firstName, lastName);
    }

    public Teacher() {
    }

    @Override
    public String toString() {
        return "Teacher{" +
                super.toString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
