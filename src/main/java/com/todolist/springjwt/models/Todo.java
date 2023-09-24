package com.todolist.springjwt.models;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 500, nullable = false)
    private String description;

    @Column(columnDefinition = "boolean default true")
    private boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "access_todo",
        joinColumns = @JoinColumn(name = "todo_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> usersWithAccess = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<User> getUsersWithAccess() {
        return usersWithAccess;
    }

    public void setUsersWithAccess(Set<User> usersWithAccess) {
        this.usersWithAccess = usersWithAccess;
    }
}
