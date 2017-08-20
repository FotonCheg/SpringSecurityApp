package com.dyachenko.springsecurityapp.controller;

import com.dyachenko.springsecurityapp.dao.ProjectDao;
import com.dyachenko.springsecurityapp.dao.TaskDao;
import com.dyachenko.springsecurityapp.dao.UserDao;
import com.dyachenko.springsecurityapp.model.Project;
import com.dyachenko.springsecurityapp.model.Task;
import com.dyachenko.springsecurityapp.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 2017 on 13.07.2017.
 */
@Controller
public class MainController {

    @Autowired
    UserDao userDao;

    @Autowired
    ProjectDao projectDao;

    @Autowired
    TaskDao taskDao;

    @RequestMapping(value = {"/projects"}, method = RequestMethod.GET, produces= "application/json; charset=utf-8")
    @Transactional
    @ResponseBody
    public String projects(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        List<Project> list = user.getProjects();

        for(Project p : list){
            List<Task> taskList = p.getTasks();
            Collections.sort(taskList, Task.COMPARE_BY_POSITION);
        }

        String serialized="qwerty";
        try {
            serialized = new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return serialized;
    }

    //После авторизации или регистрации браузер перенаправляет на root страничку, где показывается файл index.html
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String welcome(){
        return "../../resources/html/index.html";
    }

        //При старте приложения после авторизации пользователь будет видеть только кнопку добавить проэкт и поле для ввода имени проэкта, по нажатию на кнопку должен отправляться ajax-запрос POST на этот метод для добавления юзеру его проэкта
    @RequestMapping(value = "/projects", method = RequestMethod.POST, produces= "application/json; charset=utf-8")
    @ResponseBody
    public String addNewProject(@RequestParam String name){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);
        //Вытаскиваем текущего юзера, чтобы связать его с проэктом в БД
        Project p = new Project();
        p.setId(null);
        p.setName(name);
        p.setTasks(null);
        p.setUser(user);

        Project returnedProject = projectDao.save(p);

        String serialized="qwsderty";
        try {
            serialized = new ObjectMapper().writeValueAsString(returnedProject.getId());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return serialized;
    }

    //Удаление проэкта на верхних hover-кнопочках справа. У кнопочки должен где-то храниться id проэкта.
    @RequestMapping(value = "/projects/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteProject(@PathVariable Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Project p = projectDao.findOne(id);

        if(user.getUsername().equals(p.getUser().getUsername()))
        {
            projectDao.delete(id);
        }else{
            System.out.println("Вы не имеете прав доступа к этим данным");
        }
    }

    //Теперь обработаем изменение названия проэкта с конкретным id
    @RequestMapping(value = "/projects/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public void editProject(@PathVariable Long id, @RequestParam String new_name){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Project p = projectDao.findOne(id);

        if(user.getUsername().equals(p.getUser().getUsername()))
        {
            p.setName(new_name);
            projectDao.save(p);
        }else{
            System.out.println("Вы не имеете прав доступа к этим данным");
        }

    }
//-------------------------------------------------Тут будут тасочки
    //Тут мы добавляем новую таску в определенный проэкт, отсылая соответственно её имя и id проэкта, то есть мы ajac - запросом должны каким-то образом при нажатии на кнопку "добавить", должны знать на каком id - проэкта мы нажимаем, видимо это надо как-то записать в кнопочку
    @RequestMapping(value = "/tasks", method = RequestMethod.POST)
    @ResponseBody
    public String addNewTaskToProject(@RequestParam String name, @RequestParam Long project_id, @RequestParam Integer position_id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Project p = projectDao.findOne(project_id);

        if (user.getUsername().equals(p.getUser().getUsername())) {
            Task task = new Task();
            task.setId(null);
            task.setName(name);
            task.setStatus(0);
            task.setPosition(position_id);
            task.setProject(p);

            Task returnedTask = taskDao.save(task);

            String serialized="qwsderty";
            try {
                serialized = new ObjectMapper().writeValueAsString(returnedTask.getId());

            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return serialized;
        } else {
            return "no rights";
        }
    }

    //Теперь обработаем изменение содержания таски с конкретным id
    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public void editTask(@PathVariable Long id, @RequestParam String new_name){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Task task = taskDao.findOne(id);


        if(user.getUsername().equals(task.getProject().getUser().getUsername()))
        {
            task.setName(new_name);
            taskDao.save(task);
        }else{
            System.out.println("Вы не имеете прав доступа к этим данным");
        }
    }

    //Изменение позиции перетаскивания
    @RequestMapping(value = "/tasks/position/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public void editTask(@PathVariable Long id, @RequestParam Integer position){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Task task = taskDao.findOne(id);


        if(user.getUsername().equals(task.getProject().getUser().getUsername()))
        {
            task.setPosition(position);
            Task returnedTask = taskDao.save(task);
        }else{
            System.out.println("Вы не имеете прав доступа к этим данным");
        }
    }

    //Тут обработаем изменение статуса таски done/not done
    @RequestMapping(value = "/tasks/{id}/complete", method = RequestMethod.PUT)
    @ResponseBody
    public void completeTask(@PathVariable Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Task task = taskDao.findOne(id);


        if(user.getUsername().equals(task.getProject().getUser().getUsername()))
        {
            if(task.getStatus()==0){
                task.setStatus(1);
            }else{
                task.setStatus(0);
            }
            taskDao.save(task);
        }else{
            System.out.println("Вы не имеете прав доступа к этим данным");
        }
    }

    @RequestMapping(value = "/tasks/{id}/expired", method = RequestMethod.PUT)
    @ResponseBody
    public void expiredTask(@PathVariable Long id, @RequestParam String date){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Task task = taskDao.findOne(id);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd.yyyy");

        if(user.getUsername().equals(task.getProject().getUser().getUsername()))
        {
            String formatted = dateFormat.format(new Date(date));
            task.setDeadline(formatted);
            taskDao.save(task);
        }else{
            System.out.println("Вы не имеете прав доступа к этим данным");
        }
    }

    //Удаление таскипо id
    @RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteTask(@PathVariable Long id){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userDao.findByUsername(username);

        Task task = taskDao.findOne(id);

        if(user.getUsername().equals(task.getProject().getUser().getUsername()))
        {
            taskDao.delete(id);
        }else{
            System.out.println("Вы не имеете прав доступа к этим данным");
        }
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public void setTaskDao(TaskDao taskDao) {
        this.taskDao = taskDao;
    }
}
