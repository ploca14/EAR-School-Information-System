package cz.cvut.kbss.ear.project.kosapi.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import cz.cvut.kbss.ear.project.kosapi.links.TeacherLink;

public class KosCourseInstance {

    @JacksonXmlProperty(localName = "instructors")
    private TeacherLink[] instructors;

    @JacksonXmlProperty(localName = "lecturers")
    private TeacherLink[] lecturers;

    public TeacherLink[] getInstructors() {
        return instructors;
    }

    public void setInstructors(TeacherLink[] instructors) {
        this.instructors = instructors;
    }

    public TeacherLink[] getLecturers() {
        return lecturers;
    }

    public void setLecturers(TeacherLink[] lecturers) {
        this.lecturers = lecturers;
    }
}
