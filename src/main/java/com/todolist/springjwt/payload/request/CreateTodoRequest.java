package com.todolist.springjwt.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateTodoRequest {
  @NotBlank
  @Size(min = 3, max = 20)
  private String title;

  @NotBlank
  @Size(max = 50)
  private String description;

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

  @Override
	public String toString() {
		return "{" +
				"title='" + title + '\'' +
				", description='" + description + '\'' +
				'}';
	}

}
