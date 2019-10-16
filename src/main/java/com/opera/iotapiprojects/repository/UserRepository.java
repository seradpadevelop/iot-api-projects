package com.opera.iotapiprojects.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.opera.iotapiprojects.entities.UserEntity;

/**
 * Database repository for entity <b>User</b>.
 *
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  /**
   * Get user by email.
   * 
   * @param email email
   * 
   * @return user or null
   */
  @Query("select u from UserEntity u where u.email = :email")
  Optional<UserEntity> findByEmail(@Param("email") String email);

  /**
   * get user list by project id
   * 
   * @param projectId project id
   * 
   * @return list of all users
   */
  @Query("SELECT u FROM UserEntity u WHERE EXISTS( SELECT 1 FROM RoleAssignmentEntity ra WHERE ra.user.id = u.id AND ra.project.id = :projectId)")
  List<UserEntity> findAllByProject(@Param("projectId") long projectId);

  /**
   * Get user by email.
   * 
   * @param email email
   * 
   * @return user or null
   */
  @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND EXISTS( SELECT 1 FROM RoleAssignmentEntity ra WHERE ra.user.id = u.id AND ra.project.id = :projectId)")
  Optional<UserEntity> findByEmailAndProject(@Param("projectId") String projectId,
      @Param("email") String email);

  /**
   * Get user by email.
   * 
   * @param email email
   * 
   * @return user or null
   */
  @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND EXISTS( SELECT 1 FROM RoleAssignmentEntity ra WHERE ra.user.id = u.id AND ra.project.id = :projectId)")
  Optional<UserEntity> findByEmailAndProject(@Param("projectId") long projectId,
      @Param("email") String email);

  /**
   * get user by id with project id
   * 
   * @param userId user id
   * @param projectId project id
   * 
   * @return user entity
   */
  @Query("SELECT u FROM UserEntity u WHERE u.id = :userId AND EXISTS( SELECT 1 FROM RoleAssignmentEntity ra WHERE ra.user.id = u.id AND ra.project.id = :projectId)")
  Optional<UserEntity> findWithIdByProject(@Param("userId") long userId,
      @Param("projectId") long projectId);
}
