package com.opera.iotapiprojects.service;

import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import com.opera.iotapiprojects.dto.ProjectsDto;
import com.opera.iotapiprojects.entities.ProjectEntity;
import com.opera.iotapiprojects.repository.ProjectRepository;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
public class ProjectsServiceTest extends TestCase {

  @InjectMocks
  ProjectsService projectService;

  @Mock
  ProjectRepository projectRepository;

  String userId = "max.mustermann@example.com";

  @Test
  public void getAllProjectsForUser_Returns_ResultSet() throws Exception {
    List<ProjectEntity> projectData = mockProjectData();

    when(projectRepository.getProjectsForUser(userId)).thenReturn(projectData);

    List<ProjectsDto> dataList = projectService.getAllProjectsForUser(userId);

    Assert.assertNotNull(dataList);
  }

  @Test
  public void getAllProjectsForUser_Returns_EmptyResultSet() throws Exception {
    List<ProjectEntity> projectData = new ArrayList<>();

    when(projectRepository.getProjectsForUser(userId)).thenReturn(projectData);

    List<ProjectsDto> dataList = projectService.getAllProjectsForUser(userId);

    Assert.assertNotNull(dataList);
    Assert.assertTrue(dataList.isEmpty());
  }

  @Test
  public void getAllProjectsForUser_NoUserId() throws Exception {
    List<ProjectEntity> projectData = new ArrayList<>();

    when(projectRepository.getProjectsForUser(null)).thenReturn(projectData);

    List<ProjectsDto> dataList = projectService.getAllProjectsForUser(null);

    Assert.assertNull(dataList);
  }

  private List<ProjectEntity> mockProjectData() {
    ProjectEntity data = new ProjectEntity();
    List<ProjectEntity> dataList = new ArrayList<>();

    data.setId(1L);
    data.setName("Project 1");
    data.setProjectId("1");
    data.setProjectManager("Manager 1");
    data.setStartDate(LocalDate.of(2019, 1, 1));
    data.setEndDate(LocalDate.of(2020, 12, 31));
    data.setProgress(30);
    dataList.add(data);

    return dataList;
  }
}
