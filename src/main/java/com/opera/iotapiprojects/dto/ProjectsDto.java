package com.opera.iotapiprojects.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectsDto {

  private String projectId;
  private String selfLink;
  private String name;
  private String projectManager;
  private String start;
  private String end;
  private Float progress;

}
