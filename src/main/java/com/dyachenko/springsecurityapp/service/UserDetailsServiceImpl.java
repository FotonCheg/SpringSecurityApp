package com.dyachenko.springsecurityapp.service;

import com.dyachenko.springsecurityapp.dao.UserDao;
import com.dyachenko.springsecurityapp.model.Role;
import com.dyachenko.springsecurityapp.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Реализация интерфейса интерфейса UserDetailsService фреймворка SpringSecurity
 * Тут важно не промахнуться, не javax.Transactional, а спринговая аннотация
 */

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        //Создаем пользоваетеля, которого мы ищем в нашей БД
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        //Создаем разрешения

        for(Role role: user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        //Добавляем в разрешения все разрешения пользователя, которые хранятся в БД, то есть получаем все роли, которые есть у пользвателя и запихиваем их в разрешения(при чем не роли, а именно разрешения)

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);

    }
}
