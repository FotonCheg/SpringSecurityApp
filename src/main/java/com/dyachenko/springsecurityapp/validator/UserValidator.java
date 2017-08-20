package com.dyachenko.springsecurityapp.validator;

import com.dyachenko.springsecurityapp.model.User;
import com.dyachenko.springsecurityapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Валидатор для класса User
 * Это компонент, так что ставим аннотацию
 * Имплементит спринговый валидатор
 * Забыл про аннотацию, в итоге не автовайрилось(требовало бин)
 */
@Component
public class UserValidator implements Validator{

    @Autowired
    private UserService userService;

    public boolean supports(Class<?> aClass) {

        return User.class.equals(aClass);
        //Подтверждаем, что это является экземпляром юзер
    }

    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "Required");
        if(user.getUsername().length()<8 || user.getUsername().length()>32){
            errors.rejectValue("username", "Size.userForm.username");
        }

        if(userService.findByUsername(user.getUsername())!=null){
            errors.rejectValue("username", "Duplicate.userForm.username");
            //Если у нас уже есть такой пользователь, то отвергаем
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "Required");
        if(user.getPassword().length()<8 || user.getPassword().length()>32){
            errors.rejectValue("password", "Size.userForm.password");
        }

        if(!user.getConfirmPassword().equals(user.getPassword())){
            errors.rejectValue("confirmPassword", "Diggerent.userForm.password");
            //Если ввели пароль, а подтвердили его не правильно, то выдаем ошибочку
        }
    }
}
