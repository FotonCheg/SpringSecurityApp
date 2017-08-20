package com.dyachenko.springsecurityapp.controller;

import com.dyachenko.springsecurityapp.model.User;
import com.dyachenko.springsecurityapp.service.SecurityService;
import com.dyachenko.springsecurityapp.service.UserService;
import com.dyachenko.springsecurityapp.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Контроллер для сущности User (то есть для страниц пользователя)
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model){
        model.addAttribute("userForm", new User());

        return "registration.jsp";
    }
    //Метод в котором мы хотим только получить, т.е. GET

    @RequestMapping(value="/registration", method=RequestMethod.POST)
    public String registration(@ModelAttribute("userForm")User userForm, BindingResult bindingResult, Model model){
      userValidator.validate(userForm, bindingResult);

      if(bindingResult.hasErrors()){
          return "registration.jsp";
      }
      //Если резултат содержит ошибки, то перенаправляем опять на страницу регистрации, никуда не идем
      userService.save(userForm);
      //Если же нет ошибок, то сохраняем пользователя

        securityService.autoLogin(userForm.getUsername(), userForm.getConfirmPassword());
        //Передаем этому методу имя пользователя и пароль подтверждения

        return "redirect:/";
        //И редиректим пользоваетеля на страницу приветствия
    }
    //Тут мы уже хотим передавать значения при регистрации. Мы тут биндим наш результат, то есть связываем

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout){
        if(error!=null){
            model.addAttribute("error", "Username or password is incorrect.");
        }

        if(logout != null){
            model.addAttribute("message", "Logged out succesfully.");
        }

        return "login.jsp";
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String admin(Model model){
        return"admin.jsp";
    }
}
