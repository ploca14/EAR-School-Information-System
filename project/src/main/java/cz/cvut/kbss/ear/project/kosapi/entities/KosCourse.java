package cz.cvut.kbss.ear.project.kosapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KosCourse {

    private String code;

    private String name;

    private String credits;

    private String description;


}
