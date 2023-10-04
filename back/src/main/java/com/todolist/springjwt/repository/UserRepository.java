package com.todolist.springjwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todolist.springjwt.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  User findByUsername(String username);

  User findByEmail(String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}