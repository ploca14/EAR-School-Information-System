package cz.cvut.kbss.ear.project.model;

import com.sun.istack.NotNull;
import cz.cvut.kbss.ear.project.model.enums.SemesterState;
import cz.cvut.kbss.ear.project.model.enums.SemesterType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = "Semester.findByState", query = "SELECT s FROM Semester s WHERE s.state = :semesterState"),
    @NamedQuery(name = "Semester.findByCode", query = "SELECT s FROM Semester s WHERE s.code = :code"),
    @NamedQuery(name = "Semester.findByYear", query = "SELECT s FROM Semester s WHERE s.year = :year")
})
public class Semester extends AbstractEntity {

    @NotNull
    @Column(unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SemesterState state;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SemesterType type;

    @NotNull
    private String year;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public SemesterState getState() {
        return state;
    }

    public void setState(SemesterState state) {
        this.state = state;
    }

    public SemesterType getType() {
        return type;
    }

    public void setType(SemesterType type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Semester{" +
            "code='" + code + '\'' +
            ", state=" + state +
            ", type=" + type +
            ", year='" + year + '\'' +
            '}';
    }
}
