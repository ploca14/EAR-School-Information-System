package cz.cvut.kbss.ear.project.model;

import javax.persistence.Entity;

@Entity
public class User {

    private String username;

    private String firstName;

    private String lastName;

    private String password;

    private String permanentResidence;

    private String mailingAddress;

    private String email;
}
