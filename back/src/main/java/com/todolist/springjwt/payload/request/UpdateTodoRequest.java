package com.todolist.springjwt.payload.request;

import java.util.Date;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateTodoRequest {
  @NotBlank
  @Size(max = 100)
  private String title;

  @NotBlank
  private String description;

  @NotBlank
  @Temporal(TemporalType.TIMESTAMP)
  private Date changeDate;


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

  public Date getChangeDate() {
    return changeDate;
  }

  public void setChangeDate(Date changedate) {
    this.changeDate = changedate;
  }

  @Override
	public String toString() {
		return "{" +
				"title='" + title + '\'' +
				", description='" + description + '\'' +
        ", changeDate='" + changeDate + '\'' +
				'}';
	}

}
