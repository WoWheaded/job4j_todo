package ru.job4j.todo.model;

import lombok.Data;

import javax.persistence.*;
import java.util.TimeZone;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String login;

    private String password;

    @Column(name = "user_zone")
    private TimeZone timezone;
}