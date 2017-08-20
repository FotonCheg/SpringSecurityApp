package com.dyachenko.springsecurityapp.service;

/**
 * Сервис, который отвечает за безопасность нашего приложения
 */
public interface SecurityService {
    String findLoggedInUsername();
            //То есть мы будем искать залогиненых пользователей
    void autoLogin(String username, String password);
}
