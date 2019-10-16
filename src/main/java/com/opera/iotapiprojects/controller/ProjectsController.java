package com.opera.iotapiprojects.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.opera.iotapiprojects.dto.ProjectsDto;
import com.opera.iotapiprojects.entities.ProjectEntity;
import com.opera.iotapiprojects.entities.RoleEntity;
import com.opera.iotapiprojects.model.SearchStringModel;
import com.opera.iotapiprojects.model.UserModel;
import com.opera.iotapiprojects.service.ProjectsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;



/**
 * Contains operations for user and project related role functions.
 *
 */
@Api
@RestController
@Slf4j
public class ProjectsController {

  @Autowired
  protected ProjectsService projectsService;

  /**
   * Get a list of all projects.
   *
   * @return list of projects
   */
  @ApiOperation(value = "List all projects.", response = ProjectEntity.class,
      responseContainer = "List")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Projects found"),
      @ApiResponse(code = 404, message = "No projects found."),
      @ApiResponse(code = 401, message = "Unauthorized.")})
  @GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<ProjectEntity>> getProjectList() {
    final List<ProjectEntity> allProjects = projectsService.getAllProjects();
    return allProjects.isEmpty() ? new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT)
        : new ResponseEntity<>(allProjects, HttpStatus.OK);
  }
  
  /*
  @ApiOperation(value = "List of all projects for authorized user.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "List of projects available."),
      @ApiResponse(code = 404, message = "No projects found."),
      @ApiResponse(code = 401, message = "Unauthorized.")})
  @GetMapping(value = "/projects")
  public ResponseEntity<List<ProjectsDto>> getProjectListForAuthorizedUser(
      @RequestHeader(value = "Authorization") String authorizationHeader) {

    String upn = getUpnFromBearerToken(authorizationHeader);
    log.debug("getProjectListForAuthorizedUser({})", upn);
    List<ProjectsDto> projects = projectsService.getAllProjectsForUser(upn);
    

    return !projects.isEmpty() ? new ResponseEntity<>(projects, HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
   */

  private String getUpnFromBearerToken(String authorizationHeader) {
    String decodedToken =
        new String(Base64.decodeBase64(authorizationHeader.replace("Bearer ", "")));
    Pattern pattern = Pattern.compile("\"upn\":\"(.*?)\",");
    Matcher matcher = pattern.matcher(decodedToken);
    return matcher.find() ? matcher.group(1) : null;
  }

  /**
   * Create a project if not already existing.
   *
   * @param project project to add
   */
  @ApiOperation(value = "Create an project.", response = ProjectEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Project created"),
      @ApiResponse(code = 400, message = "Project not created"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 409, message = "Project already exists")})
  @PostMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<ProjectEntity> createProject(@RequestBody ProjectEntity project) {
    if (projectsService.projectExisting(project.getName())) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    final boolean saved = projectsService.saveProject(project);

    return saved ? new ResponseEntity<>(project, HttpStatus.CREATED)
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  /**
   * Get the project entity by id.
   *
   * @param projectId project id
   *
   * @return project entity data
   */
  @ApiOperation(value = "Return an project.", response = ProjectEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Project found"),
      @ApiResponse(code = 401, message = "Unauthorized."),
      @ApiResponse(code = 404, message = "Project not found")})
  @GetMapping(value = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<ProjectEntity> getProject(@PathVariable("projectId") long projectId) {
    final ProjectEntity project = projectsService.findProjectById(projectId);

    return project == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
        : new ResponseEntity<>(project, HttpStatus.OK);
  }

  /**
   * Update a project.
   *
   * @param project project data to update
   * @param projectId project id to update
   *
   * @return project data if update was successful
   */
  @ApiOperation(value = "Update an project.", response = ProjectEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Project updated"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 404, message = "Project not found")})
  @PutMapping(value = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<ProjectEntity> updateProject(@RequestBody ProjectEntity project,
      @PathVariable("projectId") long projectId) {
    if (projectsService.findProjectById(projectId) == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    projectsService.updateProject(project);
    return new ResponseEntity<>(project, HttpStatus.OK);
  }

  /**
   * Delete a project by id.
   *
   * @param projectId project id to delete
   */
  @ApiOperation(value = "Delete a project.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Project deleted"),
      @ApiResponse(code = 304, message = "Project not deleted"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 404, message = "Project not found")})
  @DeleteMapping(value = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<ProjectEntity> deleteProject(@PathVariable("projectId") long projectId) {
    ProjectEntity project = projectsService.findProjectById(projectId);
    if (project == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (projectsService.deleteProjectById(projectId)) {
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  /**
   * Get a list of all users of a project (with roles attached).
   *
   * @return list of users of a project with roles
   */
  @ApiOperation(value = "List all users.", response = UserModel.class, responseContainer = "List")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Users found"),
      @ApiResponse(code = 204, message = "No users found."),
      @ApiResponse(code = 401, message = "Unauthorized.")})
  @GetMapping(value = "/projects/{projectId}/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<UserModel>> getUserList(@PathVariable("projectId") long projectId) {
    final List<UserModel> allUsers = projectsService.getAllUsersByProject(projectId);
    return allUsers.isEmpty() ? new ResponseEntity<>(new ArrayList<>(), HttpStatus.NOT_FOUND)
        : new ResponseEntity<>(allUsers, HttpStatus.OK);
  }

  /**
   * Create a user in a project
   *
   * @param user user to add to a project
   */
  @ApiOperation(value = "Create an user.", response = UserModel.class)
  @ApiResponses(value = {@ApiResponse(code = 201, message = "User created"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 409, message = "User not created")})
  @PostMapping(value = "/projects/{projectId}/users", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserModel> createUser(@PathVariable("projectId") long projectId,
      @RequestBody UserModel user) {
    final boolean saved = projectsService.saveUser(projectId, user);

    return saved ? new ResponseEntity<>(user, HttpStatus.CREATED)
        : new ResponseEntity<>(HttpStatus.CONFLICT);
  }

  /**
   * Get the user entity by id in a project.
   *
   * @param projectId project id
   * @param userId user id
   *
   * @return user model data
   */
  @ApiOperation(value = "Return an user.", response = UserModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "User found"),
      @ApiResponse(code = 204, message = "User not found"),
      @ApiResponse(code = 401, message = "Unauthorized.")})
  @GetMapping(value = "/projects/{projectId}/users/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserModel> getUser(@PathVariable("projectId") long projectId, @PathVariable("userId") long userId) {
    final UserModel user = projectsService.findUserById(projectId, userId);

    return user == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
        : new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Update a user by id in a project.
   *
   * @param user user data to update
   * @param projectId project id
   * @param userId user id to update
   *
   * @return user data if update was successful
   */
  @ApiOperation(value = "Update an user.", response = UserModel.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "User updated"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 404, message = "User not found")})
  @PutMapping(value = "/projects/{projectId}/users/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserModel> updateUser(@RequestBody UserModel user,
      @PathVariable("projectId") long projectId, @PathVariable("userId") long userId) {
    if (projectsService.findUserById(projectId, userId) == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    projectsService.updateUser(user);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /**
   * Delete an user by id in a project.
   *
   * @param projectId project id
   * @param userId user id to delete
   */
  @ApiOperation(value = "Delete an user.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "User deleted"),
      @ApiResponse(code = 304, message = "User not deleted"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 404, message = "User not found")})
  @DeleteMapping(value = "/projects/{projectId}//users/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserModel> deleteUser(@PathVariable("projectId") long projectId,
      @PathVariable("userId") long userId) {
    UserModel user = projectsService.findUserById(projectId, userId);
    if (user == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (projectsService.deleteUserById(userId)) {
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  /**
   * Get a list of all roles of a project.
   *
   * @return list of roles of a project
   */
  @ApiOperation(value = "List all roles.", response = RoleEntity.class, responseContainer = "List")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Roles found"),
      @ApiResponse(code = 204, message = "No roles found."),
      @ApiResponse(code = 401, message = "Unauthorized.")})
  @GetMapping(value = "/projects/{projectId}/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<RoleEntity>> getRoleList(@PathVariable("projectId") long projectId) {
    final List<RoleEntity> allRoles = projectsService.getAllRolesByProject(projectId);
    return allRoles.isEmpty() ? new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT)
        : new ResponseEntity<>(allRoles, HttpStatus.OK);
  }

  /**
   * Create a role in a project.
   *
   * @param projectId project id
   * @param role role to add
   */
  @ApiOperation(value = "Create an role.", response = RoleEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 201, message = "Role created"),
      @ApiResponse(code = 400, message = "Role not created"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 409, message = "Role already exists")})
  @PostMapping(value = "/projects/{projectId}/roles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<RoleEntity> createRole(@PathVariable("projectId") long projectId,
      @RequestBody RoleEntity role) {
    if (projectsService.roleExisting(projectId, role.getName())) {
      return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    final boolean saved = projectsService.saveRole(projectId, role);

    return saved ? new ResponseEntity<>(role, HttpStatus.CREATED)
        : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  /**
   * Get the role entity by id in a project.
   *
   * @param projectId project id
   * @param roleId role id
   *
   * @return role entity data
   */
  @ApiOperation(value = "Return an role.", response = RoleEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Role found"),
      @ApiResponse(code = 404, message = "Role not found"),
      @ApiResponse(code = 401, message = "Unauthorized.")})
  @GetMapping(value = "/projects/{projectId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<RoleEntity> getRole(@PathVariable("projectId") long projectId,
      @PathVariable("roleId") long roleId) {
    final RoleEntity role = projectsService.findRoleById(projectId, roleId);

    return role == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
        : new ResponseEntity<>(role, HttpStatus.OK);
  }

  /**
   * Update a role in a project.
   *
   * @param role role data to update
   * @param projectId project id
   * @param roleId role id to update
   *
   * @return role data if update was successful
   */
  @ApiOperation(value = "Update an role.", response = RoleEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Role updated"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 404, message = "Role not found")})
  @PutMapping(value = "/projects/{projectId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<RoleEntity> updateRole(@RequestBody RoleEntity role,
      @PathVariable("projectId") long projectId, @PathVariable("roleId") long roleId) {
    if (projectsService.findRoleById(projectId, roleId) == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    projectsService.updateRole(projectId, role);
    return new ResponseEntity<>(role, HttpStatus.OK);
  }

  /**
   * Delete a role by id in a project.
   *
   * @param projectId project id
   * @param roleId role id to delete
   */
  @ApiOperation(value = "Delete a role.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Role deleted"),
      @ApiResponse(code = 400, message = "Role not deleted"),
      @ApiResponse(code = 401, message = "Unauthorized. Sign on user is not administrator"),
      @ApiResponse(code = 404, message = "Role not found")})
  @DeleteMapping(value = "/projects/{projectId}/roles/{roleId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<RoleEntity> deleteRole(@PathVariable("projectId") long projectId,
      @PathVariable("roleId") long roleId) {
    RoleEntity role = projectsService.findRoleById(projectId, roleId);
    if (role == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    if (projectsService.deleteRoleById(roleId)) {
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  /**
   * Find a project entity by name.
   *
   * @param projectId project name
   *
   * @return project entity data
   */
  @ApiOperation(value = "Return an project.", response = ProjectEntity.class)
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Project found"),
      @ApiResponse(code = 401, message = "Unauthorized."),
      @ApiResponse(code = 404, message = "Project not found"),
      @ApiResponse(code = 412, message = "Property search string not set.")})
  @PostMapping(value = "/projects/search", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<ProjectEntity> getProjectbyName(
      @RequestBody SearchStringModel searchStringModel) {
    if (searchStringModel == null || searchStringModel.getSearchString() == null) {
      return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
    }
    final ProjectEntity project =
        projectsService.findProjectByName(searchStringModel.getSearchString());

    return project == null ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
        : new ResponseEntity<>(project, HttpStatus.OK);
  }

  /**
   *
   * @param projectId id of the project to check the user is allowed to
   * @return OK is user is allowed for the projectId, otherwise UNAUTHORIZED
   */
  @ApiOperation(value = "Checks if a user is authorized for a project.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "Authorized"),
      @ApiResponse(code = 401, message = "Unauthorized.")})
  @GetMapping(value = "/projects/{projectId}")
  public ResponseEntity<Void> checkAuthorization(@PathVariable("projectId") long projectId,
      @RequestHeader(value = "Authorization") String authorizationHeader) {

    Boolean exists = projectsService.isUserAllowedByProject(projectId,
        getUpnFromBearerToken(authorizationHeader));

    return exists ? new ResponseEntity<>(HttpStatus.OK)
        : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
  }

}
