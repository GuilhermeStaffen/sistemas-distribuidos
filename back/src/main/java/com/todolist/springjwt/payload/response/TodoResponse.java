package com.todolist.springjwt.payload.response;

import java.util.Date;
import java.util.List;

public class TodoResponse {
  private Long id;
  private String title;
  private String description;
  private Date openDate = new Date();

  public TodoResponse(Long id, String title, String description, Date openDate) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.openDate = openDate;
  }


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

  public Date getOpenDate() {
    return openDate;
  }

  public void setOpenDate(Date openDate) {
    this.openDate = openDate;
  }

}
