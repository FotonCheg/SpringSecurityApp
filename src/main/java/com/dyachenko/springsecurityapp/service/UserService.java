package com.dyachenko.springsecurityapp.service;

import com.dyachenko.springsecurityapp.model.User;

/**
 * Сервис класс для User'а
 * Тут у нас есть метод save для сохранения юзера и
 * метод findByUsername для поиска пользователя по его
 * имени
 */
public interface UserService {
    void save(User user);
    User findByUsername(String username);
}
