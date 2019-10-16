package com.opera.iotapiprojects.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.opera.iotapiprojects.entities.ProjectEntity;

/**
 * Database repository for entity <b>Project</b>.
 * 
 */
@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

  /**
   * Get project by name.
   * 
   * @param name name
   * 
   * @return project or null
   */
  @Query("select p from ProjectEntity p where LOWER(p.name) = LOWER(:name)")
  Optional<ProjectEntity> findByName(@Param("name") String name);

  @Query(
      value = "select * from projects.project p where p.id in (select project_id from projects.role_assignment where user_id = (select u.id from projects.user u where u.email = :email));",
      nativeQuery = true)
  List<ProjectEntity> getProjectsForUser(@Param("email") String email);
}
