package com.opera.iotapiprojects.entities;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data entity <b>role_assignment<b>. Used for database hibernate mapping and JSON rest transformation.
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "role_assignment", schema = "projects")
@DynamicUpdate
@Cacheable
@org.hibernate.annotations.Cache(region = "roleAssignmentEntityCache",
    usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleAssignmentEntity implements Serializable {
  private static final long serialVersionUID = 4563236174524L;

  @Id
  @Column(name = "id")
  @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "role_assignment_id_seq",
      name = "role_assignment_id_seq", schema = "projects")
  @GeneratedValue(generator = "role_assignment_id_seq", strategy = GenerationType.SEQUENCE)
  @JsonProperty("id")
  private Long id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private UserEntity user;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "project_id")
  @JsonIgnore
  private ProjectEntity project;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "role_id")
  @JsonIgnore
  private RoleEntity role;
}