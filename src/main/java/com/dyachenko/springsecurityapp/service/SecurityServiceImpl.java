package com.dyachenko.springsecurityapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * реализация SecuriService. Аннотация сервис говорит о том,
 * что наш класс относится к сервис-слою и она позволит искать бины
 * автоматически
 */
@Service
public class SecurityServiceImpl implements SecurityService{

    private static final Logger logger = LoggerFactory.getLogger(SecurityServiceImpl.class);
    //Тут решили добавить немного логгирования

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    public String findLoggedInUsername() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if(userDetails instanceof UserDetails){
            return ((UserDetails) userDetails).getUsername();
            //Если детали юзера является экземпляром юзер дитейлс, то получаем имя залогиненного пользователя, это нужно будет например чтобы поприветствовать нашего пользователя, если же нет, то возвращаем null
        }
                //Создаем объект, который содержит информацию по пользователю
        return null;
    }

    public void autoLogin(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

        authenticationManager.authenticate(authenticationToken);
        if(authenticationToken.isAuthenticated()){
            SecurityContextHolder .getContext().setAuthentication(authenticationToken);

            logger.debug(String.format("Succesfully %s auto logged in", username));
        }
    }
}
