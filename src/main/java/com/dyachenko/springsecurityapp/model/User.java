package com.dyachenko.springsecurityapp.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Простой JavaBean объект, представляющий самого пользователя
 * transient private String confirmPassword; - не обязательно писать transient,
 * т.к. мы используем аннотацию @Transient, так что и не пишем
 * @author Yaroslav Dyachenko
 * @version 1.0
 */

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Transient
    private String confirmPassword;

    @ManyToMany(cascade=CascadeType.REMOVE)
    @JoinTable(name="user_roles", joinColumns=@JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

}
