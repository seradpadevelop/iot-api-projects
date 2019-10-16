package com.opera.iotapiprojects.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.opera.iotapiprojects.dto.ProjectsDto;
import com.opera.iotapiprojects.entities.ProjectEntity;
import com.opera.iotapiprojects.entities.RoleAssignmentEntity;
import com.opera.iotapiprojects.entities.RoleEntity;
import com.opera.iotapiprojects.entities.UserEntity;
import com.opera.iotapiprojects.model.UserModel;
import com.opera.iotapiprojects.repository.ProjectRepository;
import com.opera.iotapiprojects.repository.RoleAssignmentRepository;
import com.opera.iotapiprojects.repository.RoleRepository;
import com.opera.iotapiprojects.repository.UserRepository;

/**
 * This class contains all methods for user and project related role management.
 *
 */
@Service
public class ProjectsService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  ProjectRepository projectRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  RoleAssignmentRepository roleAssignmentRepository;

  /**
   *
   * @param projectId id of the project
   * @param userId unique id of the user, email here
   * @return true is userId is assigned to the projectId, otherwise false
   */
  public Boolean isUserAllowedByProject(long projectId, String userId) {
    if (userId == null || userId.isBlank()) {
      return false;
    }
    Optional<UserEntity> user = userRepository.findByEmailAndProject(projectId, userId);

    return user.isPresent();
  }

  public List<ProjectsDto> getAllProjectsForUser(String userId) {
    if (userId == null || userId.isBlank()) {
      return null;
    }
    List<ProjectsDto> projects = new ArrayList<>();
    List<ProjectEntity> data = projectRepository.getProjectsForUser(userId);

    for (ProjectEntity projectEntity : data) {
      ProjectsDto projectInfo = new ProjectsDto();
      projectInfo.setProjectId(projectEntity.getProjectId());
      projectInfo.setSelfLink("/projects/" + projectEntity.getProjectId());
      projectInfo.setName(projectEntity.getName());
      projectInfo.setProjectManager(projectEntity.getProjectManager());
      projectInfo.setStart(projectEntity.getStartDate().toString());
      projectInfo.setEnd(projectEntity.getEndDate().toString());
      projectInfo.setProgress(Float.valueOf(projectEntity.getProgress()));

      projects.add(projectInfo);
    }

    return projects;
  }

  /**
   * Find all users
   *
   * @return list of users
   */
  public List<UserModel> getAllUsersByProject(long projectId) {
    final List<UserEntity> allUsers = userRepository.findAllByProject(projectId);
    final List<UserModel> response = new ArrayList<>();
    for (UserEntity user : allUsers) {
      response.add(mapUserEntityToModel(user));
    }
    return response;
  }

  /**
   * Find all roles
   *
   * @return list of roles
   */
  public List<RoleEntity> getAllRolesByProject(long projectId) {
    return roleRepository.getAllByProject(projectId);
  }

  /**
   * Find all projects
   *
   * @return list of projects
   */
  public List<ProjectEntity> getAllProjects() {
    return projectRepository.findAll();
  }

  /**
   * Fetch an user by database id.
   *
   * @param projectId project id
   * @param userId user id
   *
   * @return user or null
   */
  public UserModel findUserById(long projectId, long userId) {
    final Optional<UserEntity> userEntity = userRepository.findWithIdByProject(userId, projectId);
    return userEntity.isPresent() ? mapUserEntityToModel(userEntity.get()) : null;
  }

  /**
   * Fetch an role by database id.
   *
   * @param projectId project id
   * @param roleId role id
   *
   * @return role or null
   */
  public RoleEntity findRoleById(long projectId, long roleId) {
    final Optional<RoleEntity> roleEntity = roleRepository.findWithIdByProject(roleId, projectId);
    return roleEntity.orElse(null);
  }

  /**
   * Fetch an project by database id.
   *
   * @param id database id
   *
   * @return project or null
   */
  public ProjectEntity findProjectById(long id) {
    final Optional<ProjectEntity> projectEntity = projectRepository.findById(id);
    return projectEntity.orElse(null);
  }

  /**
   * Fetch an project by name.
   *
   * @param name project name
   *
   * @return project or null
   */
  public ProjectEntity findProjectByName(String name) {
    final Optional<ProjectEntity> projectEntity = projectRepository.findByName(name);
    return projectEntity.orElse(null);
  }

  /**
   * Check if user is existing by email
   *
   * @param email email of user
   *
   * @return true if found, false if not
   */
  public boolean userExisting(String email) {
    final Optional<UserEntity> user = userRepository.findByEmail(email);

    return user.isPresent() ? true : false;
  }

  /**
   * Save user entity
   *
   * @param projectId project id
   * @param user user entity to be saved
   *
   * @return true if saved, false if not
   */
  public boolean saveUser(long projectId, UserModel user) {
    final Optional<ProjectEntity> project = projectRepository.findById(projectId);
    if (project.isEmpty()) {
      return false;
    } else {
      final ProjectEntity projectEntity = project.get();
      final UserEntity newUser = mapUserModelToEntity(user);
      final UserEntity savedUser = userRepository.save(newUser);
      if (newUser.getRoles() != null) {
        for (RoleEntity role : newUser.getRoles()) {
          RoleAssignmentEntity roleAssignment = new RoleAssignmentEntity();
          roleAssignment.setUser(savedUser);
          roleAssignment.setProject(projectEntity);
          roleAssignment.setRole(role);
          roleAssignmentRepository.save(roleAssignment);
        }
      }

      return savedUser == null ? false : true;
    }
  }

  /**
   * Update user entity
   *
   * @param user user entity to be updated
   */
  public void updateUser(UserModel user) {
    userRepository.save(mapUserModelToEntity(user));

    return;
  }

  /**
   * Check if project is existing by name
   *
   * @param name name of project
   *
   * @return true if found, false if not
   */
  public boolean projectExisting(String name) {
    final Optional<ProjectEntity> project = projectRepository.findByName(name);

    return project.isPresent() ? true : false;
  }

  /**
   * Save project entity
   *
   * @param project project entity to be saved
   *
   * @return true if saved, false if not
   */
  public boolean saveProject(ProjectEntity project) {
    // upon new projects ignore eventually provided id and create a new
    project.setId(null);
    final ProjectEntity savedProject = projectRepository.save(project);

    return savedProject == null ? false : true;
  }

  /**
   * Update project entity
   *
   * @param project project entity to be updated
   */
  public void updateProject(ProjectEntity project) {
    projectRepository.save(project);

    return;
  }

  /**
   * Check if role is existing by name
   *
   * @param projectId project id
   * @param name name of role
   *
   * @return true if found, false if not
   */
  public boolean roleExisting(long projectId, String name) {
    final RoleEntity role = roleRepository.findByNameWithProject(name, projectId);

    return role == null ? false : true;
  }

  /**
   * Save role entity
   *
   * @param projectId project id
   * @param role role entity to be saved
   *
   * @return true if saved, false if not
   */
  public boolean saveRole(long projectId, RoleEntity role) {
    // upon new roles ignore eventually provided id and create a new
    role.setId(null);
    final Optional<ProjectEntity> project = projectRepository.findById(projectId);
    if (project.isEmpty()) {
      return false;
    } else {
      final ProjectEntity projectEntity = project.get();
      role.setProject(projectEntity);
      final RoleEntity savedRole = roleRepository.save(role);

      return savedRole == null ? false : true;
    }
  }

  /**
   * Update role entity
   *
   * @param projectId project id
   * @param role role entity to be updated
   */
  public boolean updateRole(long projectId, RoleEntity role) {
    final Optional<ProjectEntity> project = projectRepository.findById(projectId);
    if (project.isEmpty()) {
      return false;
    } else {
      final ProjectEntity projectEntity = project.get();
      role.setProject(projectEntity);
      RoleEntity updatedRole = roleRepository.save(role);

      return updatedRole == null ? false : true;
    }
  }

  /**
   * Delete user entity
   *
   * @param userId user id to be deleted
   *
   * @return true if user has been deleted, else false
   */
  public boolean deleteUserById(long userId) {
    UserEntity user = userRepository.findById(userId).get();
    for (RoleAssignmentEntity roleAssignmt : user.getRoleAssignmentEntityList()) {
      roleAssignmentRepository.deleteById(roleAssignmt.getId());
    }
    userRepository.deleteById(userId);

    return userRepository.findById(userId).isEmpty() ? true : false;
  }

  /**
   * Delete project entity
   *
   * @param projectId project id to be deleted
   *
   * @return true if project has been deleted, else false
   */
  public boolean deleteProjectById(long projectId) {
    projectRepository.deleteById(projectId);

    return projectRepository.findById(projectId).isEmpty() ? true : false;
  }

  /**
   * Delete role entity
   *
   * @param roleId role id to be deleted
   *
   * @return true if role has been deleted, else false
   */
  public boolean deleteRoleById(long roleId) {
    roleRepository.deleteById(roleId);

    return roleRepository.findById(roleId).isEmpty() ? true : false;
  }

  private UserModel mapUserEntityToModel(UserEntity userEntity) {
    UserModel userModel = new UserModel();
    userModel.setName(userEntity.getUserName());
    userModel.setEmail(userEntity.getEmail());
    userModel.setId(userEntity.getId());
    final List<RoleEntity> roles = new ArrayList<>();
    userEntity.getRoleAssignmentEntityList()
        .forEach(roleAssignMt -> roles.add(roleAssignMt.getRole()));
    userModel.setRoles(roles);
    return userModel;
  }

  private UserEntity mapUserModelToEntity(UserModel userModel) {
    UserEntity userEntity = new UserEntity();
    userEntity.setUserName(userModel.getName());
    userEntity.setEmail(userModel.getEmail());
    userEntity.setId(userModel.getId());
    userEntity.setRoles(userModel.getRoles());
    return userEntity;
  }
}
