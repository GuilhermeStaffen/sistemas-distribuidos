package com.todolist.springjwt.payload.request;

import jakarta.validation.constraints.NotBlank;

public class ShareTodoRequest {
  private Long id;

  @NotBlank
  private String email;

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
