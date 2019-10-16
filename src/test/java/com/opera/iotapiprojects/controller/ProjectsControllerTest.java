package com.opera.iotapiprojects.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.opera.iotapiprojects.dto.ProjectsDto;
import com.opera.iotapiprojects.service.ProjectsService;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProjectsController.class)
@WebAppConfiguration
@AutoConfigureMockMvc
public class ProjectsControllerTest extends TestCase {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private WebApplicationContext wac;

  @MockBean
  private JwtDecoder decoder;

  private Principal principal;

  @MockBean
  private ProjectsService projectsService;

  private String userId = "janina.knecht@vsaoffice365.onmicrosoft.com";
  

  @Before
  public void setup() {
    mvc = MockMvcBuilders.webAppContextSetup(wac).build();

    Jwt mockJwt = mock(Jwt.class);
    Mockito.when(mockJwt.getClaimAsString(Mockito.matches("upn"))).thenReturn(userId);

    principal = new JwtAuthenticationToken(mockJwt);

  }

  @Test
  @WithMockUser()
  @Ignore("must be reworked")
  public void getProjectListForAuthorizedUser_Returns_ResultSet() throws Exception {

    List<ProjectsDto> projects = mockProjectsList();

    when(projectsService.getAllProjectsForUser(userId)).thenReturn(projects);

    mvc.perform(get("http://localhost:8888/api/projects").contextPath("/api")
        .accept(MediaType.APPLICATION_JSON).principal(principal).header("Authorization",
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6ImllX3FXQ1hoWHh0MXpJRXN1NGM3YWNRVkduNCIsImtpZCI6ImllX3FXQ1hoWHh0MXpJRXN1NGM3YWNRVkduNCJ9.eyJhdWQiOiI1NjViNTQ1NC01Njk1LTRiYmYtYmMxNC0wYTcxNzA2MDk2NjQiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yOGRlMzE1My03MWI1LTQxMjUtYmUwZC04ODg4NWQyZTQ0ZmEvIiwiaWF0IjoxNTY2OTgyNzEyLCJuYmYiOjE1NjY5ODI3MTIsImV4cCI6MTU2Njk4NjYxMiwiYWlvIjoiNDJGZ1lOaFNGYkJEcnN4NnBza010VDlpTjlTOVVxZXpkKzQ5bDM5WGZacXc4Qlg1OTBFQSIsImFtciI6WyJwd2QiXSwiZmFtaWx5X25hbWUiOiJLbmVjaHQiLCJnaXZlbl9uYW1lIjoiSmFuaW5hIiwiaXBhZGRyIjoiMTk1LjIxMi4yOS4xNjkiLCJuYW1lIjoiSmFuaW5hIEtuZWNodCIsIm5vbmNlIjoiZGJkMmExM2EtOWQ5OS00ODhlLTg4OGItZjk4MDg0MGI4NGE2Iiwib2lkIjoiMTc4ZTgzOTMtZTE2Zi00ZjQ1LWI0OGYtZTNmODE2Njc3NjM0Iiwic3ViIjoiX2FqZkl0UEx5VU92LXhJNER2dW96NXFrWDVuQTN6dXBqS0pNempNV1FfQSIsInRpZCI6IjI4ZGUzMTUzLTcxYjUtNDEyNS1iZTBkLTg4ODg1ZDJlNDRmYSIsInVuaXF1ZV9uYW1lIjoiamFuaW5hLmtuZWNodEB2c2FvZmZpY2UzNjUub25taWNyb3NvZnQuY29tIiwidXBuIjoiamFuaW5hLmtuZWNodEB2c2FvZmZpY2UzNjUub25taWNyb3NvZnQuY29tIiwidXRpIjoiWTRvcU94eE1fRTY5WFAxVGJnNDRBQSIsInZlciI6IjEuMCJ9."))
        .andExpect(status().isOk());

    Mockito.verify(projectsService, times(1)).getAllProjectsForUser(userId);
    Mockito.verifyNoMoreInteractions(projectsService);
  }

  @Test
  @WithMockUser()
  @Ignore("must be reworked")
  public void getProjectListForAuthorizedUser_Returns_EmptyResultSet() throws Exception {

    List<ProjectsDto> projects = new ArrayList<>();

    when(projectsService.getAllProjectsForUser(userId)).thenReturn(projects);

    mvc.perform(get("http://localhost:8888/api/projects").contextPath("/api")
        .accept(MediaType.APPLICATION_JSON).principal(principal).header("Authorization",
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6ImllX3FXQ1hoWHh0MXpJRXN1NGM3YWNRVkduNCIsImtpZCI6ImllX3FXQ1hoWHh0MXpJRXN1NGM3YWNRVkduNCJ9.eyJhdWQiOiI1NjViNTQ1NC01Njk1LTRiYmYtYmMxNC0wYTcxNzA2MDk2NjQiLCJpc3MiOiJodHRwczovL3N0cy53aW5kb3dzLm5ldC8yOGRlMzE1My03MWI1LTQxMjUtYmUwZC04ODg4NWQyZTQ0ZmEvIiwiaWF0IjoxNTY2OTgyNzEyLCJuYmYiOjE1NjY5ODI3MTIsImV4cCI6MTU2Njk4NjYxMiwiYWlvIjoiNDJGZ1lOaFNGYkJEcnN4NnBza010VDlpTjlTOVVxZXpkKzQ5bDM5WGZacXc4Qlg1OTBFQSIsImFtciI6WyJwd2QiXSwiZmFtaWx5X25hbWUiOiJLbmVjaHQiLCJnaXZlbl9uYW1lIjoiSmFuaW5hIiwiaXBhZGRyIjoiMTk1LjIxMi4yOS4xNjkiLCJuYW1lIjoiSmFuaW5hIEtuZWNodCIsIm5vbmNlIjoiZGJkMmExM2EtOWQ5OS00ODhlLTg4OGItZjk4MDg0MGI4NGE2Iiwib2lkIjoiMTc4ZTgzOTMtZTE2Zi00ZjQ1LWI0OGYtZTNmODE2Njc3NjM0Iiwic3ViIjoiX2FqZkl0UEx5VU92LXhJNER2dW96NXFrWDVuQTN6dXBqS0pNempNV1FfQSIsInRpZCI6IjI4ZGUzMTUzLTcxYjUtNDEyNS1iZTBkLTg4ODg1ZDJlNDRmYSIsInVuaXF1ZV9uYW1lIjoiamFuaW5hLmtuZWNodEB2c2FvZmZpY2UzNjUub25taWNyb3NvZnQuY29tIiwidXBuIjoiamFuaW5hLmtuZWNodEB2c2FvZmZpY2UzNjUub25taWNyb3NvZnQuY29tIiwidXRpIjoiWTRvcU94eE1fRTY5WFAxVGJnNDRBQSIsInZlciI6IjEuMCJ9."))
        .andExpect(status().isNoContent());

    Mockito.verify(projectsService, times(1)).getAllProjectsForUser(userId);
    Mockito.verifyNoMoreInteractions(projectsService);
  }

  private List<ProjectsDto> mockProjectsList() {
    List<ProjectsDto> projectsList = new ArrayList<>();
    ProjectsDto data = new ProjectsDto();
    data.setProjectId("1");
    data.setName("Project 1");
    data.setProjectManager("Manager 1");
    data.setStart("2019-01-01");
    data.setEnd("2020-12-31");
    data.setProgress(32f);

    projectsList.add(data);

    return projectsList;
  }
}
