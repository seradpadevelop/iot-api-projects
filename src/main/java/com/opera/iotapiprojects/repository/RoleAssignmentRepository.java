package com.opera.iotapiprojects.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.opera.iotapiprojects.entities.RoleAssignmentEntity;

/**
 * Database repository for entity <b>RoleAssignment</b>.
 *
 */
@Repository
public interface RoleAssignmentRepository extends JpaRepository<RoleAssignmentEntity, Long> {

}
