package com.opera.iotapiprojects;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class ProjectsApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProjectsApplication.class, args);
  }

  /**
   * Set the default time zone to UTC.
   */
  @PostConstruct
  public void started() {
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
  }

}
