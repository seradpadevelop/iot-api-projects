package com.opera.iotapiprojects.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data entity <b>user<b>. Used for database hibernate mapping and JSON rest transformation.
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "user", schema = "projects")
@DynamicUpdate
@Cacheable
@org.hibernate.annotations.Cache(region = "userEntityCache",
    usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserEntity implements Serializable {
  private static final long serialVersionUID = 3561236156734L;

  @Id
  @Column(name = "id")
  @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "user_id_seq",
      name = "user_id_seq", schema = "projects")
  @GeneratedValue(generator = "user_id_seq", strategy = GenerationType.SEQUENCE)
  @JsonProperty("id")
  private Long id;

  @Column(name = "email", nullable = false)
  @JsonProperty("email")
  private String email;

  @Column(name = "user_name")
  @JsonProperty("userName")
  private String userName;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  @JsonIgnore
  private List<RoleAssignmentEntity> roleAssignmentEntityList = new ArrayList<>();

  @JsonProperty("roles")
  @Transient
  private List<RoleEntity> roles;

  // explicit setter for lowercase email due to naming conventions in AD
  public void setEmail(String email) {
    this.email = email.toLowerCase();
  }
}