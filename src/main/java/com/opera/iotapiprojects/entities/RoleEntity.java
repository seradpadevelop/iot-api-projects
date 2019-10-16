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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
 * Data entity <b>role<b>. Used for database hibernate mapping and JSON rest transformation.
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "role", schema = "projects")
@DynamicUpdate
@Cacheable
@org.hibernate.annotations.Cache(region = "roleEntityCache",
    usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleEntity implements Serializable {
  private static final long serialVersionUID = 4569236151543L;

  @Id
  @Column(name = "id")
  @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "role_id_seq",
      name = "role_id_seq", schema = "projects")
  @GeneratedValue(generator = "role_id_seq", strategy = GenerationType.SEQUENCE)
  @JsonProperty("id")
  private Long id;

  @Column(name = "name", nullable = false)
  @JsonProperty("name")
  private String name;

  @Column(name = "permission_id")
  @JsonProperty("permissionId")
  private Long permissionId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "project_id")
  @JsonIgnore
  private ProjectEntity project;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "role_id", insertable = false, updatable = false, nullable = false)
  @JsonIgnore
  private List<RoleAssignmentEntity> roleAssignmentEntityList = new ArrayList<>();
}
