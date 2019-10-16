package com.opera.iotapiprojects.entities;

import java.io.Serializable;
import java.time.LocalDate;
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
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Data entity <b>project<b>. Used for database hibernate mapping and JSON rest transformation.
 * 
 */
@Getter
@Setter
@Entity
@Table(name = "project", schema = "projects")
@DynamicUpdate
@Cacheable
@org.hibernate.annotations.Cache(region = "projectEntityCache",
    usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectEntity implements Serializable {
  private static final long serialVersionUID = 3561951486734L;

  @Id
  @Column(name = "id")
  @SequenceGenerator(allocationSize = 1, initialValue = 1, sequenceName = "project_id_seq",
      name = "project_id_seq", schema = "projects")
  @GeneratedValue(generator = "project_id_seq", strategy = GenerationType.SEQUENCE)
  @JsonIgnore
//  @JsonProperty("id")
  private Long id;

  @Column(name = "name", nullable = false)
  @JsonProperty("name")
  private String name;

  @Column(name = "project_manager", nullable = false)
  @JsonProperty("projectManager")
  private String projectManager;

  @Column(name = "start_date")
  @JsonProperty("startDate")
  private LocalDate startDate;

  @Column(name = "end_date")
  @JsonProperty("endDate")
  private LocalDate endDate;

  @Column(name = "progress")
  @JsonProperty("progress")
  private Integer progress;

  @Column(name = "projectid")
  @JsonProperty("projectId")
  private String projectId;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  @JsonIgnore
  private List<RoleEntity> roleEntityList = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  @JsonIgnore
  private List<RoleAssignmentEntity> roleAssignmentList = new ArrayList<>();
}
