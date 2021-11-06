package cz.cvut.kbss.ear.project.kosapi.entities;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Objects;

public class TeacherLink implements Comparable<TeacherLink>{
    @JacksonXmlProperty(isAttribute = true, localName = "href", namespace = "xlink")
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherLink that = (TeacherLink) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "TeacherLink{" +
                "url='" + url + '\'' +
                '}';
    }

    @Override
    public int compareTo(TeacherLink o) {
        return this.url.equals(o.url) ? 0 : 1;
    }
}
