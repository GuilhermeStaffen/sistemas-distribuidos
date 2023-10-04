package com.todolist.springjwt.repository;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.todolist.springjwt.models.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
  Optional<Todo> findById(Long id);

  @Query(value = "SELECT t.id, t.title, t.description, t.active FROM todos t " +
      "JOIN access_todo at ON t.id = at.todo_id " +
      "JOIN users u ON at.user_id = u.id " +
      "WHERE t.active = true AND u.username = :username order by t.id asc", nativeQuery = true)
  ArrayList<Object[]> findActiveTodosWithAccess(@Param("username") String username);

    @Query(value = "SELECT t.id, t.title, t.description, t.active FROM todos t " +
      "JOIN access_todo at ON t.id = at.todo_id " +
      "JOIN users u ON at.user_id = u.id " +
      "WHERE t.active = false AND u.username = :username order by t.id asc", nativeQuery = true)
  ArrayList<Object[]> findInactiveTodosWithAccess(@Param("username") String username);
}
