package com.dyachenko.springsecurityapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by 2017 on 13.07.2017.
 */
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    //cascadetype.REMOVE : Мы в sql специально ставим constraint для того, чтобы мы не могли удалить какой-нибудь project,  при том, что на него остались ссылки в тасках. То есть эти таски будут висеть в БД привязанными к проэкту, которого даже не существует. Поэтому мы ставим такой тип каскада, чтобы при удалении проэкта, удалялись все таски, которые в нем были. Точно также логично сделать и в сущности Юзер, когда удалили юзера, то и нет смысла хранить привязанные к нему данные в БД
    @OneToMany(mappedBy = "project", cascade=CascadeType.REMOVE)
    private List<Task> tasks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    User user;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

