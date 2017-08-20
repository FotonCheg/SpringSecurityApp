package com.dyachenko.springsecurityapp.service;

import com.dyachenko.springsecurityapp.dao.RoleDao;
import com.dyachenko.springsecurityapp.dao.UserDao;
import com.dyachenko.springsecurityapp.model.Role;
import com.dyachenko.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Реализация UserService. Тут ставим аннотация @Service, указывая, что это класс сервисов
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        //Получаем введенный пользователем пароль, кодируем его и записываем в юзера
        List<Role> roles = new ArrayList<Role>();
        //По-умолчанию у пользователя должна быть какая-то роль при добавлении его в базу данных, поэтому мы создаем сет ролей и добавляем в них роль с id=1, то есть ROLE_USER. То есть по-умолчанию каждый новый зарегестрированный юзер получает роль юзер
        roles.add(roleDao.getOne(1L));
        user.setRoles(roles);
        //Отдаем юзеру его сет ролей
        userDao.save(user);
        //Сохраняем всё, что только что сделали с юзером
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
