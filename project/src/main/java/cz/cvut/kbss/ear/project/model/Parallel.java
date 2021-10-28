package cz.cvut.kbss.ear.project.model;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
public class Parallel {

    private String name;

    private Timestamp startTime;

    private Timestamp endTime;

    private String note;
}
