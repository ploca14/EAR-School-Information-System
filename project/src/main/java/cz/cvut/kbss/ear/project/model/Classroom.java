package cz.cvut.kbss.ear.project.model;

import javax.persistence.Entity;

@Entity
public class Classroom extends AbstractEntity{

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
