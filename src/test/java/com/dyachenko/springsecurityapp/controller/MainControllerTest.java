package com.dyachenko.springsecurityapp.controller;

import com.dyachenko.springsecurityapp.model.Project;
import com.dyachenko.springsecurityapp.model.Task;
import com.dyachenko.springsecurityapp.service.SecurityService;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.dyachenko.springsecurityapp.dao.ProjectDao;
import com.dyachenko.springsecurityapp.dao.TaskDao;
import com.dyachenko.springsecurityapp.dao.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations = { "file:src/main/webapp/WEB-INF/appconfig-root.xml",
        "file:src/main/webapp/WEB-INF/appconfig-data.xml",
        "file:src/main/webapp/WEB-INF/appconfig-mvc.xml" })
@WebAppConfiguration
public class MainControllerTest {

    MockMvc mockMvc;

    @Autowired
    MainController mainController;

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    private SecurityService securityService;



    @Autowired
    UserDao userDaoMock;

    @Autowired
    ProjectDao projectDaoMock;

    @Autowired
    TaskDao taskDaoMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MainController mainController = new MainController();
        mainController.setProjectDao(projectDaoMock);
        mainController.setTaskDao(taskDaoMock);
        mainController.setUserDao(userDaoMock);

        mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
        securityService.autoLogin("Yaroslav", "A0b7p0b7r0f7");
    }

    @Test
    @Transactional
    @Sql(scripts = "classpath:database.sql")
    public void projectsGet() throws Exception {
        //Должен вернуть JSON объекты проэктов
        assertNotNull(mockMvc);
        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Мой первый проэкт")));


    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void welcome() throws Exception {

    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void addNewProject() throws Exception {
        mockMvc.perform(post("/projects").param("name", "TestProjectInsertion"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string("2"));
    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void deleteProject() throws Exception {
        mockMvc.perform(delete("/projects/{id}", 1))
                .andExpect(status().isOk());
        Project project = projectDaoMock.findOne(1L);
        assertNull(project);
    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void editProject() throws Exception {
        mockMvc.perform(put("/projects/{id}", 1).param("new_name", "NewName"))
                .andExpect(status().isOk());
        Project project = projectDaoMock.findOne(1L);
        assertEquals(project.getName(), "NewName");
    }

    //tasks
    @Test
    @Sql(scripts = "classpath:database.sql")
    public void addNewTaskToProject() throws Exception {
        mockMvc.perform(post("/projects")
                .param("name", "FirstTestTask")
                .param("project_id", "2")
                .param("position_id", "1"))

                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void editTask() throws Exception {
        mockMvc.perform(put("/tasks/{id}", 1)
                .param("new_name", "NewNameOfTask"))

                .andExpect(status().isOk());

        Task task = taskDaoMock.findOne(1L);
        assertEquals(task.getName(), "NewNameOfTask");
    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void editTaskPosition() throws Exception {
        mockMvc.perform(put("/tasks/position/{id}", 1)
                .param("position", "7"))

                .andExpect(status().isOk());

        Task task = taskDaoMock.findOne(1L);
        assertEquals(task.getPosition().toString(), "7");
    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void completeTask() throws Exception {
        mockMvc.perform(put("/tasks/{id}/complete", 1))
                .andExpect(status().isOk());

        Task task = taskDaoMock.findOne(1L);
        assertEquals(task.getStatus().toString(), "1");

        mockMvc.perform(put("/tasks/{id}/complete", 1))
                .andExpect(status().isOk());

        Task taskNotComplete = taskDaoMock.findOne(1L);
        assertEquals(taskNotComplete.getStatus().toString(), "0");
    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void expiredTask() throws Exception {
        mockMvc.perform(put("/tasks/{id}/expired", 1)
                .param("date","12/31/2017"))
                .andExpect(status().isOk());

        Task task = taskDaoMock.findOne(1L);
        assertEquals(task.getDeadline(), "12.31.2017");
    }

    @Test
    @Sql(scripts = "classpath:database.sql")
    public void deleteTask() throws Exception {
        mockMvc.perform(delete("/tasks/{id}", 1))
                .andExpect(status().isOk());
        Task task = taskDaoMock.findOne(1L);
        assertNull(task);
    }



}
