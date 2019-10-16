package com.opera.iotapiprojects.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.opera.iotapiprojects.entities.RoleEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * User model for API data transfer.
 * 
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserModel {

  @JsonProperty("userId")
  private Long id;

  @JsonProperty("name")
  private String name;

  @JsonProperty("email")
  private String email;

  @JsonProperty("roles")
  private List<RoleEntity> roles;

}
