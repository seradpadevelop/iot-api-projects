package com.opera.iotapiprojects.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.opera.iotapiprojects.entities.RoleEntity;

/**
 * Database repository for entity <b>Role</b>.
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

  /**
   * Get role by name.
   * 
   * @param name name
   * 
   * @return role or null
   */
  @Query("SELECT r FROM RoleEntity r WHERE r.name = :name AND r.project.id = :projectId")
  RoleEntity findByNameWithProject(@Param("name") String name,
      @Param("projectId") long projectId);

  /**
   * get role list by project id
   * 
   * @param projectId project id
   * 
   * @return list of all roles
   */
  @Query("SELECT r FROM RoleEntity r WHERE r.project.id = :projectId")
  List<RoleEntity> getAllByProject(@Param("projectId") long projectId);

  /**
   * get role by id with project id
   * 
   * @param roleId role id
   * @param projectId project id
   * 
   * @return role entity
   */
  @Query("SELECT r FROM RoleEntity r WHERE r.id = :userId AND r.project.id = :projectId")
  Optional<RoleEntity> findWithIdByProject(@Param("userId") long roleId,
      @Param("projectId") long projectId);
}
